package il.ac.biu.project.foobar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CreatePostActivity extends AppCompatActivity {
    private PostDetails postDetails;
    private Bitmap img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post); // Correctly sets the layout
        postDetails = getIntent().getParcelableExtra("postDetails");
        UserDetails user = UserDetails.getInstance();

        // Directly set the template using the current layout
        setTemplate(user);

        TextView postPublish = findViewById(R.id.post_publish);
        postPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishPost(user);
            }
        });
        ImageView addPhotoIcon = findViewById(R.id.add_photo_icon);
        addPhotoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissions()) {
                    requestPermissions();
                }
                if (checkPermissions()) {
                    pickImage();
                }
            }
        });
    }

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
        returnIntent.putExtra("modifiedPostDetails", postDetails);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private String getTimeAndDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE HH:mm");
        return now.format(formatter);
    }

    private void setTemplate(UserDetails user) {
        // Using findViewById to get the current views from the layout
        ImageView profileImage = findViewById(R.id.profile_picture_create_post);
        profileImage.setImageBitmap(user.getImg()); // Assuming user.getImg() returns the correct bitmap

        TextView nameView = findViewById(R.id.user_name_create_post);
        nameView.setText(user.getDisplayName());
    }
    private void pickImage() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
        pickImageIntent.setType("image/*");

        // Create an Intent to capture an image using the camera
        Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create a chooser Intent that includes both gallery and camera options
        Intent chooserIntent = Intent.createChooser(pickImageIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureImageIntent});

        startActivityForResult(chooserIntent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            ImageView showPostImage = findViewById(R.id.post_picture);
            // Check if the image is from the gallery
            if (data != null && data.getData() != null) {
                // The user has successfully picked an image from the gallery
                Uri selectedImageUri = data.getData();
                try {
                    // Convert the Uri to a Bitmap
                    img = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // The image is from the camera
                // Use the thumbnail directly
                Bundle extras = data.getExtras();
                if (extras != null) {
                    // Use the thumbnail as a Bitmap
                    img = (Bitmap) extras.get("data");

                }
            }
            showPostImage.setImageBitmap(img);
            showPostImage.setVisibility(View.VISIBLE);

        }
    }

    /**
     * Requests the necessary permissions from the user.
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                100);
    }


    /**
     * Checks if the necessary permissions have been granted by the user.
     *
     * @return True if permissions are granted, false otherwise.
     */
    private boolean checkPermissions() {
        boolean cameraPer = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean writePer = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return cameraPer;
    }

    /**
     * Called when the permission request response is received. If permissions are granted,
     * an image pick is initiated.
     *
     * @param requestCode  The request code passed in requestPermissions().
     * @param permissions  The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (!checkPermissions()) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                pickImage();
            } else {
                Toast.makeText(this, "Permission denied. Cannot choose an image.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}

