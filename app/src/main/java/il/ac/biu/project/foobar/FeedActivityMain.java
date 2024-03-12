package il.ac.biu.project.foobar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import il.ac.biu.project.foobar.adapters.PostsListAdapter;
import il.ac.biu.project.foobar.entities.AddLikePostListener;
import il.ac.biu.project.foobar.entities.Comment;
import il.ac.biu.project.foobar.entities.GoToProfileListener;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.PostJsonDetails;
import il.ac.biu.project.foobar.entities.PostManager;
import il.ac.biu.project.foobar.entities.PostRemoveListener;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.repositories.PostsRepository;
import il.ac.biu.project.foobar.viewmodels.PostsViewModel;

public class FeedActivityMain extends AppCompatActivity {
    // Counter to keep track of posts
    private int postCounter = 0;

    // Layout to contain posts
    private RecyclerView layout;
    private PostsListAdapter postsListAdapter;

    UserDetails userDetails = UserDetails.getInstance();
    PostsViewModel postsViewModel;


    // Request codes for activities
    public static final int CREATE_POST_REQUEST = 1;
    public static final int SHARE_PAGE_REQUEST = 2;
    public static final int COMMENT_PAGE_REQUEST = 3;
    public static final int EDIT_POST_REQUEST = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ensure only signed-in users can access the FeedActivity
        protectFeedPage();

        // Set the layout for the activity
        setContentView(R.layout.activity_feed_main);

        // Initialize the adapter and view model for posts

        setPostsViewModel();

        setAddPostButton();

        // Show the home fragment initially
        showHome();

        initializeBottomNavigationView();
        // Initialize BottomNavigationView

    }

    private void initializeBottomNavigationView() {
        BottomNavigationView navigationView = findViewById(R.id.bootomnavigationid);
        navigationView.setItemIconTintList(null);
        // Set listener for BottomNavigationView items
        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        int itemId = item.getItemId();
                        // Handle navigation item clicks
                        if (itemId == R.id.MenuID) {
                            // Show the menu fragment
                            findViewById(R.id.profile_bar).setVisibility(View.GONE);
                            findViewById(R.id.scroll).setVisibility(View.GONE);
                            selectedFragment = new MenuFragment(postsViewModel);
                        } else if (itemId == R.id.myhome) {
                            // Show the home
                            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);
                            if (currentFragment != null) {
                                // Remove the currently displayed fragment
                                getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                            }
                            showHome();
                            item.setChecked(true);
                        }else if (itemId == R.id.FriendsId) {
                            // Show the friends fragment
                            findViewById(R.id.profile_bar).setVisibility(View.GONE);
                            findViewById(R.id.scroll).setVisibility(View.GONE);
                            selectedFragment = new FriendsFragment();
                        }else if (itemId == R.id.VideoId) {
                            // Show the video fragment
                            findViewById(R.id.profile_bar).setVisibility(View.GONE);
                            findViewById(R.id.scroll).setVisibility(View.GONE);
                            selectedFragment = new ProfileFragment(postsViewModel, UserDetails.getInstance().getUsername());
                        }else if (itemId == R.id.NotificationsId) {
                            findViewById(R.id.profile_bar).setVisibility(View.GONE);
                            findViewById(R.id.scroll).setVisibility(View.GONE);
                            selectedFragment = new NotificationFragment();
                        }


                        if (selectedFragment != null) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.framelayout, selectedFragment)
                                    .commit();
                            return true;
                        }
                        return false;
                    }

                });
        // Set user profile picture
        ImageView profileImage = findViewById(R.id.profile_image);

        // Set user profile picture
        Bitmap userProfilePicture = UserDetails.getInstance().getImg();
        if (userProfilePicture != null) {
            profileImage.setImageBitmap(userProfilePicture);
        }
    }

    // Method to show the home
    public void showHome(){
        findViewById(R.id.profile_bar).setVisibility(View.VISIBLE);
        findViewById(R.id.scroll).setVisibility(View.VISIBLE);
        // Check the "Home" button in the bottom navigation view
        BottomNavigationView navigationView = findViewById(R.id.bootomnavigationid);
        navigationView.getMenu().findItem(R.id.myhome).setChecked(true);

    }

    /**
     * Sets up the button for adding new posts. When clicked, a new post ID is generated,
     * and CreatePostActivity is started for creating a new post.
     */
    private void setAddPostButton() {
        TextView addPostButton = findViewById(R.id.whats_on_your_mind_textview);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment post counter
                postCounter++;

                // Start CreatePostActivity to create a new post
                Intent intent = new Intent(FeedActivityMain.this, CreatePostActivity.class);
                intent.putExtra("postID", String.valueOf(postCounter));
                startActivityForResult(intent, CREATE_POST_REQUEST);
            }
        });
    }

    /**
     * Ensures that only signed-in users can access the FeedActivity. If a user is not signed in,
     * they are redirected to the MainActivity.
     */
    private void protectFeedPage() {
        if (!userDetails.getSignIn()) {
            Intent intent = new Intent(FeedActivityMain.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }




    /**
     * Handles the activity result from CreatePostActivity, ShareActivity, or CommentActivity.
     * Depending on the requestCode, it updates the feed accordingly.
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_POST_REQUEST && resultCode == RESULT_OK && data != null) {
            String modifiedPostId = data.getStringExtra("modifiedPostId");
                PostDetails post = PostManager.getInstance().getPost(modifiedPostId);
                    postsViewModel.add(post);
        }

        if (requestCode == SHARE_PAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Handle successful share activity completion
            } else {
                // Handle unsuccessful share activity completion
            }
        }

        if (requestCode == EDIT_POST_REQUEST) {
            String modifiedPostId = data.getStringExtra("modifiedPostId");
            PostDetails post = PostManager.getInstance().getPost(modifiedPostId);
            postsViewModel.edit(post);
        }

//        if (requestCode == COMMENT_PAGE_REQUEST) {
//            int modifiedPostId = data.getIntExtra("commentPostId", 0);
//            if (postManager.getPost(modifiedPostId) != null) {
//                int commentCount = postManager.getPost(modifiedPostId).getCommentCount();
//                TextView commentValues = postViewMap.get(modifiedPostId).findViewById(R.id.comment_count);
//                // updates the comment count
//                commentValues.setText(commentCount + " Comments");
//                if (commentCount < 1) {
//                    // if there is no comment set invisible
//                    commentValues.setVisibility(View.INVISIBLE);
//                } else {
//                    commentValues.setVisibility(View.VISIBLE);
//                }
//            }
//        }
    }

//    private void reloadPosts() {
//        // Clear existing posts from the layout
//        layout.removeAllViews();
//
//        // Iterate through the entries in the postManager
//        for (PostDetails postDetails : postManager.getAllPosts()) {
//            // Add the post to the layout
//            addPost(postDetails);
//        }
//    }
    //If savedInstanceState is not null, it means that the activity is being recreated due to a configuration change (such as a theme change in your case)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current selected item of the bottom navigation view
        BottomNavigationView navigationView = findViewById(R.id.bootomnavigationid);
        outState.putInt("selectedItemId", navigationView.getSelectedItemId());
    }


    private void setPostsViewModel() {
        layout = findViewById(R.id.container);

        postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);

        postsListAdapter = new PostsListAdapter(this, this, new PostRemoveListener() {
            @Override
            public void onDeletePost(PostDetails postDetails) {
                postsViewModel.delete(postDetails);
            }
        }, new AddLikePostListener() {
            @Override
            public void onLikePost(PostDetails postDetails) {
                postsViewModel.addLike(postDetails);
            }
        }, new GoToProfileListener() {

            @Override
            public void goToProfile(String userID) {
                findViewById(R.id.profile_bar).setVisibility(View.GONE);
                findViewById(R.id.scroll).setVisibility(View.GONE);
                Fragment selectedFragment = new ProfileFragment(postsViewModel, userID);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayout, selectedFragment) // Use your actual container ID
                        .addToBackStack(null) // Optional: Add this transaction to the back stack
                        .commit();

            }
        });
        layout.setAdapter(postsListAdapter);
        layout.setLayoutManager(new LinearLayoutManager(this));


        postsViewModel.get().observe(this, new Observer<List<PostDetails>>() {
            @Override
            public void onChanged(List<PostDetails> postsList) {
                postsListAdapter.setPosts(postsList);
            }
        });

        SwipeRefreshLayout  postsRefresh = findViewById(R.id.scroll);
        postsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postsViewModel.reload(postsRefresh);
            }
        });
        postsViewModel.reload();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(FeedActivityMain.this, "Logout first", Toast.LENGTH_SHORT).show();
    }
}
