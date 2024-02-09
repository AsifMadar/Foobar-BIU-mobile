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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import il.ac.biu.project.foobar.AdvancedTextField.InputCallback;
import il.ac.biu.project.foobar.AdvancedTextField.ValidationFunction;
import java.io.IOException;

/**
 * Activity responsible for handling user sign-up.
 */
public class SignUpPageActivity extends AppCompatActivity {

    // User input variables
    private String username = "";
    private String password = "";
    private String rePassword = "";
    private String displayName = "";
    private Bitmap img;

    /**
     * Check if the input contains only English characters and numbers.
     *
     * @param input The input string to check.
     * @return True if the input contains only English characters and numbers, false otherwise.
     */
    private static boolean containsOnlyEnglishCharsAndNumbers(String input) {
        return input.matches("[a-zA-Z0-9]+");
    }

    /**
     * Check if the string length is within a specified range.
     *
     * @param input     The input string to check.
     * @param minLength The minimum allowed length.
     * @param maxLength The maximum allowed length.
     * @return True if the string length is within the specified range, false otherwise.
     */
    private static boolean isStringLengthInRange(String input, int minLength, int maxLength) {
        int length = input.length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Check if all input fields are valid.
     *
     * @return True if all input fields are valid, false otherwise.
     */
    private boolean isAllValid() {
        return (containsOnlyEnglishCharsAndNumbers(username)
                && isStringLengthInRange(username, 5, 16))
                && (containsOnlyEnglishCharsAndNumbers(password)
                && isStringLengthInRange(password, 8, 20))
                && (containsOnlyEnglishCharsAndNumbers(displayName)
                && isStringLengthInRange(displayName, 2, 20))
                && rePassword.equals(password)
                && img != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        UserDetails userDetails = UserDetails.getInstance();
        // if the user has signed in go to feed
        if (userDetails.getSignIn()) {
            Intent intent = new Intent(SignUpPageActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        }
        // Initialize AdvancedTextField instances for each input field


        EditText usernameEditText = findViewById(R.id.usernameEditText);
        //Username
        AdvancedTextField usernameField = new AdvancedTextField(usernameEditText, new InputCallback() {
            @Override
            public void onInputChanged(String input) {
                username = input;
            }
        }, new ValidationFunction() {
            @Override
            public boolean isValid(String input) {
                return containsOnlyEnglishCharsAndNumbers(input)
                        && isStringLengthInRange(input, 5, 16);
            }
        });
        usernameField.setErrorMessage("not 5-16 alphanumeric characters");

        //Password
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        AdvancedTextField passwordField = new AdvancedTextField(passwordEditText, new InputCallback() {
            @Override
            public void onInputChanged(String input) {
                password = input;
            }
        }, new ValidationFunction() {
            @Override
            public boolean isValid(String input) {
                return containsOnlyEnglishCharsAndNumbers(input) && isStringLengthInRange(input, 8, 20);
            }
        });

        passwordField.setErrorMessage("not 8-20 alphanumeric characters");
        //RePassword
        EditText rePasswordEditText = findViewById(R.id.rePasswordEditText);
        AdvancedTextField rePasswordField = new AdvancedTextField(rePasswordEditText, new InputCallback() {
            @Override
            public void onInputChanged(String input) {
                rePassword = input;
            }
        }, new ValidationFunction() {
            @Override
            public boolean isValid(String input) {
                return input.equals(password);
            }
        });
        rePasswordField.setErrorMessage("Passwords does not match");

        //DisplayName
        EditText displayNameEditText = findViewById(R.id.displayNameEditText);
        AdvancedTextField displayNameField = new AdvancedTextField(displayNameEditText, new InputCallback() {
            @Override
            public void onInputChanged(String input) {
                displayName = input;
            }
        }, new ValidationFunction() {
            @Override
            public boolean isValid(String input) {
                return containsOnlyEnglishCharsAndNumbers(input)
                        && isStringLengthInRange(input, 2, 16);
            }
        });
        displayNameField.setErrorMessage("not 2-16 alphanumeric characters");

        // adding a photo

        Button addImgButton = findViewById(R.id.imgButton);
        addImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if there is not permission ask for it
                if (!checkPermissions()) {
                    requestPermissions();
                }
                if (checkPermissions()) {
                    pickImage();
                }
            }
        });


        // Find the "Feed Activity" button in the layout
        Button feedActivityButton = findViewById(R.id.feedActivity);

        // Set a click listener for the button
        feedActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if all input fields are valid
                if (isAllValid()) {
                    // If valid, update UserDetails and start FeedActivity
                    userDetails.setSignIn(true);
                    userDetails.setUsername(username);
                    userDetails.setPassword(password);
                    userDetails.setDisplayName(displayName);
                    userDetails.setImg(img);
                    Intent intent = new Intent(SignUpPageActivity.this, FeedActivity.class);
                    startActivity(intent);
                } else {
                    // If not valid, display a toast with an error message
                    Toast.makeText(SignUpPageActivity.this,
                            "Invalid Fields or missing a photo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button signInActivityButton = findViewById(R.id.signInActivity);

        signInActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if all input fields are valid
                Intent intent = new Intent(SignUpPageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Launches an intent to pick an image from the gallery or capture from the camera.
     */
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
            ImageView showProfilePic = findViewById(R.id.profilePic);
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
            showProfilePic.setImageBitmap(img);

        }
    }

    /**
     * Requests the necessary permissions from the user.
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                100);
    }


    /**
     * Checks if the necessary permissions have been granted by the user.
     *
     * @return True if permissions are granted, false otherwise.
     */
    private boolean checkPermissions() {
        boolean cameraPer = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
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
