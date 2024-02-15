package il.ac.biu.project.foobar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class FeedActivityMain extends AppCompatActivity {
    int postCounter = 0;
    ImageView profileImage;
    // Layout to contain posts
    LinearLayout layout;
    // Flag to indicate if editing a post
    boolean editingPost = false;
    // Singleton instance of PostManager
    PostManager postManager = PostManager.getInstance();
    // Map to store post views
    HashMap<Integer, View> postViewMap = new HashMap<>();
    // Singleton instance of UserDetails
    UserDetails userDetails = UserDetails.getInstance();

    // Request codes for activities
    static final int CREATE_POST_REQUEST = 1;
    static final int SHARE_PAGE_REQUEST = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_main);

        protectFeedPage();

        // Container layout for posts
        layout = findViewById(R.id.container);
        setAddPostButton();
        reloadPosts();
showHome();

        // Initialize BottomNavigationView

        BottomNavigationView navigationView = findViewById(R.id.bootomnavigationid);
        navigationView.setItemIconTintList(null);

        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        int itemId = item.getItemId();
                        if (itemId == R.id.MenuID) {
                            findViewById(R.id.profile_bar).setVisibility(View.GONE);
                            findViewById(R.id.scroll).setVisibility(View.GONE);
                            selectedFragment = new MenuFragment();
                        } else if (itemId == R.id.myhome) {
                            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);
                            if (currentFragment != null) {
                                // Remove the currently displayed fragment
                                getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                            }
                            showHome();
                            item.setChecked(true);
                        }else if (itemId == R.id.FriendsId) {
                            findViewById(R.id.profile_bar).setVisibility(View.GONE);
                            findViewById(R.id.scroll).setVisibility(View.GONE);
                            selectedFragment = new FriendsFragment();
                        }else if (itemId == R.id.VideoId) {
                            findViewById(R.id.profile_bar).setVisibility(View.GONE);
                            findViewById(R.id.scroll).setVisibility(View.GONE);
                            selectedFragment = new VideoFragment();
                        }else if (itemId == R.id.NotificationsId) {
                            findViewById(R.id.profile_bar).setVisibility(View.GONE);
                            findViewById(R.id.scroll).setVisibility(View.GONE);
                            selectedFragment = new NotificationFragment();
                        }

                        // Add additional 'else if' blocks for other menu items if needed

                        if (selectedFragment != null) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.framelayout, selectedFragment)
                                    .commit();
                            return true;
                        }
                        return false;
                    }

                });

        profileImage = findViewById(R.id.profile_image);



        // Set user profile picture
        Bitmap userProfilePicture = UserDetails.getInstance().getImg();
        if (userProfilePicture != null) {
            profileImage.setImageBitmap(userProfilePicture);
        }
    }

    public void showHome(){
        findViewById(R.id.profile_bar).setVisibility(View.VISIBLE);
        findViewById(R.id.scroll).setVisibility(View.VISIBLE);
        // Check the "Home" button in the bottom navigation view
        BottomNavigationView navigationView = findViewById(R.id.bootomnavigationid);
        navigationView.getMenu().findItem(R.id.myhome).setChecked(true);

    }
    private void setAddPostButton() {
        TextView addPostButton = findViewById(R.id.whats_on_your_mind_textview);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment post counter
                postCounter++;
                // Create post details
                PostDetails postDetails = new PostDetails(postCounter, "Author", null, "User input", null, "time");
                // Add post to PostManager
                postManager.putPost(postCounter, postDetails);

                // Start CreatePostActivity to create a new post
                Intent intent = new Intent(FeedActivityMain.this, CreatePostActivity.class);
                intent.putExtra("postID", postCounter);
                startActivityForResult(intent, CREATE_POST_REQUEST);
            }
        });
    }

    // Automatically navigate to FeedActivity if user is already signed in.
    private void protectFeedPage() {
        if (!userDetails.getSignIn()) {
            Intent intent = new Intent(FeedActivityMain.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // Method to add post to the layout
    private void addPost(PostDetails postDetails) {
        // Inflate post layout
        View view = getLayoutInflater().inflate(R.layout.post, null);
        // Initialize post view
        postInitializer(postDetails, view);
        // Add post view to layout
        layout.addView(view,0);
    }

    // Method to initialize post view
    private void postInitializer(PostDetails postDetails, View view) {
        // Store post view
        postViewMap.put(postDetails.getId(), view);

        // Set author name
        TextView nameView = view.findViewById(R.id.user_name);
        nameView.setText(postDetails.getAuthorDisplayName());

        // Set post content
        TextView postContent = view.findViewById(R.id.post_text);
        postContent.setText(postDetails.getUserInput());

        // Set profile picture
        ImageView profileImage = view.findViewById(R.id.profile_picture);
        profileImage.setImageBitmap(postDetails.getAuthorProfilePicture());

        // Set time
        TextView time = view.findViewById(R.id.upload_time);
        time.setText(postDetails.getTime());

        // Set post image if available
        ImageView postImage = view.findViewById(R.id.post_image);
        if (postDetails.getPicture() != null) {
            postImage.setImageBitmap(postDetails.getPicture());
            postImage.setVisibility(View.VISIBLE);
        } else {
            postImage.setVisibility(View.GONE);
        }

        // Set click listener for options button
        ImageView postOptionsButton = view.findViewById(R.id.ellipsis_icon);
        postOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display options dialog for the post
                editOrDeleteButton(postViewMap.get(postCounter), postDetails);
            }
        });

        // Set click listener for like section
        LinearLayout likeCount = view.findViewById(R.id.like_layout);
        likeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle like button click
                handleLikeButtonClick(postDetails);
            }
        });

        // Set click listener for share section
        LinearLayout shareLayout = view.findViewById(R.id.share_layout);
        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start ShareActivity for sharing the post
                Intent intentShareActivity = new Intent(FeedActivityMain.this, ShareActivity.class);
                startActivityForResult(intentShareActivity, SHARE_PAGE_REQUEST);
            }
        });
    }

    // Method to handle result from activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_POST_REQUEST && resultCode == RESULT_OK && data != null) {
            int modifiedPostId = data.getIntExtra("modifiedPostId", 0);
            if (postManager.getPost(modifiedPostId) != null) {
                if (editingPost) {
                    // Update post view if editing post
                    postInitializer(postManager.getPost(modifiedPostId), postViewMap.get(modifiedPostId));
                    editingPost = false;
                } else {
                    // Add new post view
                    addPost(postManager.getPost(modifiedPostId));
                }
            }
        }
        if (requestCode == SHARE_PAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Handle successful share activity completion
            } else {
                // Handle unsuccessful share activity completion
            }
        }
    }

    // Method to display edit or delete dialog for a post
    private void editOrDeleteButton(View view, PostDetails postDetails) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");

        // Edit post option
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editingPost = true;
                Intent intent = new Intent(FeedActivityMain.this, CreatePostActivity.class);
                intent.putExtra("postID", postDetails.getId());
                startActivityForResult(intent, CREATE_POST_REQUEST);
            }
        });

        // Delete post option
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Remove post view and data from layout and PostManager
                layout.removeView(postViewMap.get(postDetails.getId()));
                postViewMap.remove(postDetails.getId());
                postManager.removePost(postDetails.getId());
            }
        });

        // Show dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Method to handle like button click
    private void handleLikeButtonClick(PostDetails postDetails) {
        View postView = postViewMap.get(postDetails.getId());
        String userDisplayName = userDetails.getDisplayName();
        int numOfLikes = postDetails.getNumberOfLikes();
        ImageView likeIcon = postView.findViewById(R.id.like_icon);
        TextView numOfLikeView = postView.findViewById(R.id.like_count);
        TextView likeText = postView.findViewById(R.id.like_text);
        if (postDetails.isLiked(userDisplayName)) {
            postDetails.removeLike(userDisplayName);
            numOfLikes--;
            likeIcon.setImageResource(R.drawable.like_icon_not_pressed);
            likeText.setTextColor(Color.BLACK);
            if (numOfLikes == 0) {
                numOfLikeView.setVisibility(View.INVISIBLE);
            }
        } else {
            postDetails.addLike(userDisplayName);
            numOfLikes++;
            if (numOfLikes == 1) {
                numOfLikeView.setVisibility(View.VISIBLE);
            }
            likeIcon.setImageResource(R.drawable.like_icon_pressed);
            likeText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_like));
        }
        numOfLikeView.setText(numOfLikes + " likes");
    }
    private void reloadPosts() {
        // Clear existing posts from the layout
        layout.removeAllViews();

        // Iterate through the entries in the postManager HashMap
        for (HashMap.Entry<Integer, PostDetails> entry : postManager.getAllPosts().entrySet()) {
            // Extract the post details
            PostDetails postDetails = entry.getValue();
            // Add the post to the layout
            addPost(postDetails);
        }
    }
    //If savedInstanceState is not null, it means that the activity is being recreated due to a configuration change (such as a theme change in your case)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current selected item of the bottom navigation view
        BottomNavigationView navigationView = findViewById(R.id.bootomnavigationid);
        outState.putInt("selectedItemId", navigationView.getSelectedItemId());
    }

}
