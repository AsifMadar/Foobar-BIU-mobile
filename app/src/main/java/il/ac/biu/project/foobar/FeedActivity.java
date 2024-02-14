package il.ac.biu.project.foobar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FeedActivity extends AppCompatActivity {
    // Counter to keep track of posts
    int postCounter = 0;
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

        protectFeedPage();

        setContentView(R.layout.activity_feed);

        // Container layout for posts
        layout = findViewById(R.id.container);

        try (InputStream inputStream = getResources().openRawResource(R.raw.posts)) {
            this.loadPostsFromJson(inputStream);
        } catch (java.io.IOException error) {
            // In case of error loading the posts, simply don't load the posts.
        }

        setAddPostButton();

    }

    // Button to add new posts
    private void setAddPostButton() {
        Button addPostButton = findViewById(R.id.addPost);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment post counter
                postCounter++;
                // Create post details
                PostDetails postDetails = new PostDetails(postCounter, null, null, "User input", null, "time");
                // Add post to PostManager
                postManager.putPost(postCounter, postDetails);

                // Start CreatePostActivity to create a new post
                Intent intent = new Intent(FeedActivity.this, CreatePostActivity.class);
                intent.putExtra("postID", postCounter);
                startActivityForResult(intent, CREATE_POST_REQUEST);
            }
        });
    }

    // Automatically navigate to FeedActivity if user is already signed in.
    private void protectFeedPage() {
        if (!userDetails.getSignIn()) {
            Intent intent = new Intent(FeedActivity.this, MainActivity.class);
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
        layout.addView(view);
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

        // Set click listener for share section
        LinearLayout shareLayout = view.findViewById(R.id.share_layout);
        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start ShareActivity for sharing the post
                Intent intentShareActivity = new Intent(FeedActivity.this, ShareActivity.class);
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
                Intent intent = new Intent(FeedActivity.this, CreatePostActivity.class);
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
        String userName = userDetails.getUsername();
        int numOfLikes = postDetails.getNumberOfLikes();
        ImageView likeIcon = postView.findViewById(R.id.like_icon);
        TextView numOfLikeView = postView.findViewById(R.id.like_count);
        TextView likeText = postView.findViewById(R.id.like_text);
        if (postDetails.isLiked(userName)) {
            postDetails.removeLike(userName);
            numOfLikes--;
            likeIcon.setImageResource(R.drawable.like_icon_not_pressed);
            likeText.setTextColor(Color.BLACK);
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
            PostDetails parsedPost = new PostDetails(this.postCounter++,
                post.author.displayName, authorProfilePicture, post.contents, postImage,
                Long.toString(post.timestamp));

            // Create likes list
            for (PostJsonDetails.UserJsonDetails user : post.likes) {
                parsedPost.addLike(user.username);
            }

            // Save parsed post
            parsedPosts[i] = parsedPost;
        }

        // Add posts to feed
        for (PostDetails postDetails : parsedPosts) {
            this.addPost(postDetails);
        }
    }
}
