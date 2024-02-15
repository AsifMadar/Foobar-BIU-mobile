package il.ac.biu.project.foobar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;

public class FeedActivityMain extends AppCompatActivity {
    // Counter to keep track of posts
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
    static final int COMMENT_PAGE_REQUEST = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        protectFeedPage();

        setContentView(R.layout.activity_feed_main);

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

        // Load posts from JSON file
        if (postManager.getAllPosts().isEmpty()) {
            try (InputStream inputStream = getResources().openRawResource(R.raw.posts)) {
                this.loadPostsFromJson(inputStream);
            } catch (IOException error) {

            }
        }
    }

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
                // Create post details
                PostDetails postDetails = new PostDetails(postCounter, userDetails.getUsername(),
                    null, null, null, null, 0);
                // Add post to PostManager
                postManager.putPost(postCounter, postDetails);

                // Start CreatePostActivity to create a new post
                Intent intent = new Intent(FeedActivityMain.this, CreatePostActivity.class);
                intent.putExtra("postID", postCounter);
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
     * Adds a post to the feed layout. This method inflates a post layout and initializes it
     * with the post details.
     * @param postDetails The details of the post to be added to the feed.
     */
    private void addPost(PostDetails postDetails) {
        // Inflate post layout
        View view = getLayoutInflater().inflate(R.layout.post, null);
        // Initialize post view
        postInitializer(postDetails, view);
        // Add post view to layout
        layout.addView(view,0);
    }

    /**
     * Initializes a post view with the provided post details. This includes setting up
     * the author name, post content, profile picture, and time. It also sets up listeners
     * for post options, like, comment, and share interactions.
     * @param postDetails The details of the post to initialize the view with.
     * @param view The view to be initialized.
     */
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
        time.setText(postDetails.getTimeStr());

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

        if(!postDetails.getUsername().equals(userDetails.getUsername())) {
            postOptionsButton.setVisibility(View.INVISIBLE);
        }

        // Initialize likes count
        LinearLayout likeLayout = view.findViewById(R.id.like_layout);
        int numOfLikes = postDetails.getNumberOfLikes();
        if (numOfLikes > 0) {
            TextView likeCount = view.findViewById(R.id.like_count);
            likeCount.setVisibility(View.VISIBLE);
            likeCount.setText(numOfLikes + " likes");
        }

        // Update like button image
        if (postDetails.isLiked(userDetails.getUsername())) {
            ImageView likeIcon = view.findViewById(R.id.like_icon);
            likeIcon.setImageResource(R.drawable.like_icon_pressed);
        }

        // Set click listener for like section
        likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle like button click
                handleLikeButtonClick(postDetails);
            }
        });

        //comment section
        TextView commentCountLayout = view.findViewById(R.id.comment_count);
        if (postDetails.getCommentCount() > 0) {
            commentCountLayout.setVisibility(View.VISIBLE);
            commentCountLayout.setText(postDetails.getCommentCount() + " Comments");
        } else {
            commentCountLayout.setVisibility(View.INVISIBLE);
        }

        // Set click listener for comment section
        LinearLayout commentLayout = view.findViewById(R.id.comment_layout);
        commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCommentActivity = new Intent(FeedActivityMain.this, CommentActivity.class);
                intentCommentActivity.putExtra("postID", postDetails.getId());
                startActivityForResult(intentCommentActivity, COMMENT_PAGE_REQUEST);
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

        if (requestCode == COMMENT_PAGE_REQUEST) {
            int modifiedPostId = data.getIntExtra("commentPostId", 0);
            if (postManager.getPost(modifiedPostId) != null) {
                int commentCount = postManager.getPost(modifiedPostId).getCommentCount();
                TextView commentValues = postViewMap.get(modifiedPostId).findViewById(R.id.comment_count);
                // updates the comment count
                commentValues.setText(commentCount + " Comments");
                if (commentCount < 1) {
                    // if there is no comment set invisible
                    commentValues.setVisibility(View.INVISIBLE);
                } else {
                    commentValues.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * Displays an AlertDialog with options to edit or delete a post. Based on the user's choice,
     * it either starts CreatePostActivity for editing the post or removes the post from the layout
     * and PostManager.
     * @param view The view of the post to be edited or deleted.
     * @param postDetails The details of the post associated with the view.
     */
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

    /**
     * Handles like button clicks. Updates the like count and icon based on whether the post is
     * already liked by the user.
     * @param postDetails The details of the post being liked or unliked.
     */
    private void handleLikeButtonClick(PostDetails postDetails) {
        View postView = postViewMap.get(postDetails.getId());
        String userName = userDetails.getUsername();
        int numOfLikes = postDetails.getNumberOfLikes();
        ImageView likeIcon = postView.findViewById(R.id.like_icon);
        TextView numOfLikeView = postView.findViewById(R.id.like_count);
        TextView likeText = postView.findViewById(R.id.like_text);
        if (postDetails.isLiked(userName)) {
            postDetails.removeLike(userName);
            numOfLikes--;
            likeIcon.setImageResource(R.drawable.like_icon_not_pressed);
            likeText.setTextColor(Color.GRAY);
            if (numOfLikes == 0) {
                numOfLikeView.setVisibility(View.INVISIBLE);
            }
        } else {
            postDetails.addLike(userName);
            numOfLikes++;

            numOfLikeView.setVisibility(View.VISIBLE);
            likeIcon.setImageResource(R.drawable.like_icon_pressed);
            likeText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_like));
        }
        numOfLikeView.setText(numOfLikes + " likes");
    }
    private void reloadPosts() {
        // Clear existing posts from the layout
        layout.removeAllViews();

        // Iterate through the entries in the postManager
        for (PostDetails postDetails : postManager.getAllPosts()) {
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

    private void loadPostsFromJson(InputStream jsonStream) {
        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(jsonStream));
        PostJsonDetails[] posts = gson.fromJson(reader, PostJsonDetails[].class);
        PostDetails[] parsedPosts = new PostDetails[posts.length];

        for (int i = 0; i < posts.length; i++) {
            PostJsonDetails post = posts[i];

            // Get profile picture
            Bitmap authorProfilePicture = null;
            try (@SuppressLint("DiscouragedApi") InputStream profilePictureStream = getResources()
                .openRawResource(getResources().getIdentifier(post.author.profileImage,
                "raw", getPackageName()))) {
                authorProfilePicture = BitmapFactory.decodeStream(profilePictureStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Get post picture
            Bitmap postImage = null;
            if (post.images.length > 0) {
                try (@SuppressLint("DiscouragedApi") InputStream postPictureStream =
                     getResources().openRawResource(getResources().getIdentifier(post.images[0],
                     "raw", getPackageName()))) {
                    postImage = BitmapFactory.decodeStream(postPictureStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            // Create the `PostDetails` instance
            PostDetails parsedPost = new PostDetails(this.postCounter++, post.author.username,
                post.author.displayName, authorProfilePicture, post.contents, postImage,
                post.timestamp);

            // Create likes list
            for (PostJsonDetails.UserJsonDetails user : post.likes) {
                parsedPost.addLike(user.username);
            }

            // Get comments for the post
            for (PostJsonDetails.CommentJsonDetails comment : post.comments) {
                // Get profile picture
                Bitmap commentAuthorProfilePicture = null;
                try (@SuppressLint("DiscouragedApi") InputStream profilePictureStream =
                     getResources().openRawResource(getResources().getIdentifier(
                         comment.author.profileImage, "raw", getPackageName()))) {
                    commentAuthorProfilePicture = BitmapFactory.decodeStream(profilePictureStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Create the `Comment` instance
                Comment parsedComment = new Comment(comment.author.displayName,
                    commentAuthorProfilePicture, comment.contents, comment.timestamp);

                // Create likes list
                for (PostJsonDetails.UserJsonDetails user : comment.likes) {
                    parsedComment.addLike(user.username);
                }

                // Save parsed comment
                parsedPost.addComment(parsedComment);
            }

            // Save parsed post
            parsedPosts[i] = parsedPost;
        }

        // Add posts to feed
        for (PostDetails postDetails : parsedPosts) {
            postManager.putPost(postDetails.getId(), postDetails);
        }

        this.reloadPosts();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(FeedActivityMain.this, "Logout first", Toast.LENGTH_SHORT).show();
    }
}
