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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import il.ac.biu.project.foobar.AdvancedTextField.InputCallback;
import il.ac.biu.project.foobar.AdvancedTextField.ValidationFunction;

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
        if (userDetails.getSignIn()) {
            Intent intent = new Intent(SignUpPageActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        }
        // Find EditText views in the layout
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText rePasswordEditText = findViewById(R.id.rePasswordEditText);
        EditText displayNameEditText = findViewById(R.id.displayNameEditText);

        // Initialize AdvancedTextField instances for each input field
        //Username
        AdvancedTextField usernameField = new AdvancedTextField(usernameEditText, new InputCallback() {
            @Override
            public void onInputChanged(String input) {
                username = input;
            }
        }, new ValidationFunction() {
            @Override
            public boolean isValid(String input) {
                return containsOnlyEnglishCharsAndNumbers(input) && isStringLengthInRange(input, 5, 16);
            }
        });
        usernameField.setErrorMessage("not 5-16 alphanumeric characters");

        //Password
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
        Button addImgButton = findViewById(R.id.imgButton);
        addImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkPermissions()) {
                    requestPermissions();
                }
                if(checkPermissions()) {
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
                            "Invalid Fields", Toast.LENGTH_SHORT).show();
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
            // Check if the image is from the camera
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
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                100);
    }



    private boolean checkPermissions() {
        boolean cameraPer = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean writePer = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return cameraPer;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                Toast.makeText(this, "Permission denied. Cannot choose an image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
