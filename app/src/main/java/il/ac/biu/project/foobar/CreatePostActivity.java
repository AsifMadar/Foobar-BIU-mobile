package il.ac.biu.project.foobar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Activity class for creating and editing posts.
 * Allows users to input text, add photos, and publish or update posts.
 */
public class CreatePostActivity extends AppCompatActivity {
    private PostDetails postDetails;
    private Bitmap img;
    ImageTaker imageTaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        int postId = getIntent().getIntExtra("postID", 0);
        PostManager postManager = PostManager.getInstance();
        postDetails = postManager.getPost(postId);
        UserDetails user = UserDetails.getInstance();

        // Determine whether editing an existing post or creating a new one
        if (postDetails.getAuthorDisplayName() != null) {
            // Edit existing post
            setTemplateEditPost(postDetails);
        } else {
            // Create new post
            setTemplateNewPost(user);
        }

        publishPostButton(user);
        addPhotoButton();
    }

    /**
     * Initializes and handles the Publish Post button's functionality.
     * @param user The current user details.
     */
    private void publishPostButton(UserDetails user) {
        TextView postPublish = findViewById(R.id.post_publish);
        postPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText postContent = findViewById(R.id.user_input_create_post);
                if (postDetails.getPicture() == null && postContent.getText().toString().trim().equals("")) {
                    Toast.makeText(CreatePostActivity.this,
                            "Cannot post empty post", Toast.LENGTH_SHORT).show();
                } else {
                    publishPost(user);
                }
            }
        });
    }

    /**
     * Initializes and handles the Add Photo button functionality.
     */
    private void addPhotoButton() {
        ImageView addPhotoIcon = findViewById(R.id.add_photo_icon);
        imageTaker = new ImageTaker(this);
        addPhotoIcon.setOnClickListener(view -> imageTaker.pickImage());
    }

    /**
     * Gathers post details from the user input and publishes or updates the post.
     * @param user The current user details.
     */
    private void publishPost(UserDetails user) {
        EditText postContent = findViewById(R.id.user_input_create_post);
        postDetails.setUserInput(postContent.getText().toString());
        postDetails.setAuthorDisplayName(user.getDisplayName());
        postDetails.setAuthorProfilePicture(user.getImg());
        postDetails.setTime(getTimeAndDate());
        if (img != null) {
            postDetails.setPicture(img);
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("modifiedPostId", postDetails.getId());
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    /**
     * Returns the current time and date formatted as "DayOfWeek HH:mm".
     * @return A string representing the formatted current time and date.
     */
    private String getTimeAndDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE HH:mm");
        return now.format(formatter);
    }

    /**
     * Sets up the UI template for creating a new post with the current user's details.
     * @param user The current user details.
     */
    private void setTemplateNewPost(UserDetails user) {
        ImageView profileImage = findViewById(R.id.profile_picture_create_post);
        profileImage.setImageBitmap(user.getImg());

        TextView nameView = findViewById(R.id.user_name_create_post);
        nameView.setText(user.getDisplayName());
    }

    /**
     * Sets up the UI template for editing an existing post with the post's details.
     * @param postDetails The details of the post being edited.
     */
    private void setTemplateEditPost(PostDetails postDetails) {
        ImageView profileImage = findViewById(R.id.profile_picture_create_post);
        profileImage.setImageBitmap(postDetails.getAuthorProfilePicture());

        TextView nameView = findViewById(R.id.user_name_create_post);
        nameView.setText(postDetails.getAuthorDisplayName());

        EditText postText = findViewById(R.id.user_input_create_post);
        postText.setText(postDetails.getUserInput());

        if (postDetails.getPicture() != null) {
            ImageView postImage = findViewById(R.id.post_picture);
            postImage.setImageBitmap(postDetails.getPicture());
            postImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageTaker.onActivityResult(requestCode, resultCode, data);

        if (imageTaker.getImageBitmap() != null) {
            ImageView showPostImage = findViewById(R.id.post_picture);
            showPostImage.setImageBitmap(imageTaker.getImageBitmap());
            img = imageTaker.getImageBitmap();
            showPostImage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Handles the result of permission requests necessary for image picking.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imageTaker.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
