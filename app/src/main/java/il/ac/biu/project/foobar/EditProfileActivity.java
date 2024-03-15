package il.ac.biu.project.foobar;

// EditProfileActivity.java

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import il.ac.biu.project.foobar.api.users.EditUserDetailsAPI;
import il.ac.biu.project.foobar.entities.AdvancedTextField;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.utils.ImageTaker;
import il.ac.biu.project.foobar.utils.SignUpValidations;
import il.ac.biu.project.foobar.viewmodels.PostsViewModel;
import il.ac.biu.project.foobar.viewmodels.UserViewModel;
import okhttp3.internal.Util;

public class EditProfileActivity extends AppCompatActivity {

    private EditText displayNameEditText;
    private EditUserDetailsAPI editUserDetailsApi;
    private Bitmap newProfileImage; // The new profile image selected by the user
    private UserDetails userDetails;
    PostsViewModel postViewModel;
    private ImageTaker imageTaker; // Declare ImageTaker as a class variable
    public static final int CHANGE_PROFILE_PICTURE_OR_DISPLAY_NAME_REQUEST = 5;


    private  String newDisplayName; //The user input of new display name
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        displayNameEditText = findViewById(R.id.displayNameEditText);
        userDetails = UserDetails.getInstance();

        // Set current display name
        displayNameEditText.setText(userDetails.getDisplayName());
        newProfileImage=userDetails.getImg();
        setProfileImage();
        setProfileDisplayname();
        setSaveButton();
    }
    private void setProfileImage() {
        imageTaker = new ImageTaker(EditProfileActivity.this);
        Button changeImageButton = findViewById(R.id.changeImage);
        changeImageButton.setOnClickListener(view -> imageTaker.pickImage());
    }

    private void setProfileDisplayname() {
        // Create an instance of AdvancedTextField
        AdvancedTextField displayNameTextField = new AdvancedTextField(displayNameEditText, new AdvancedTextField.InputCallback() {
            @Override
            public void onInputChanged(String input) {
                // Update the newDisplayName variable when input changes
                newDisplayName = input;
            }
        }, new AdvancedTextField.ValidationFunction() {
            @Override
            public boolean isValid(String input) {
                return SignUpValidations.containsOnlyEnglishCharsAndNumbersAndSpace(input);
            }
        });

        // Initialize newDisplayName with the current text of displayNameEditText
        newDisplayName = displayNameEditText.getText().toString().trim();
    }

    private void setSaveButton() {

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save changes
                saveProfileChanges();
            }
        });
    }
    private void saveProfileChanges() {
        // Check if all input fields are valid
        if (isDisplayNameValid(newDisplayName)) {
            // Update the display name in the database and other parts of the app
            editUserDetailsApi =new EditUserDetailsAPI();
            // Update the user details through API call
            editUserDetailsApi.editUserDetails(userDetails.getUsername(), userDetails.getJwt(),
                    newDisplayName, newProfileImage, new EditUserDetailsAPI.UserEditResponseCallback() {
                        @Override
                        public void onSuccess() {
                            // If the edit is successful, update the display name in UserDetails
                            userDetails.setDisplayName(newDisplayName);
                            userDetails.setImg(newProfileImage);
                            displayNameEditText.setText(userDetails.getDisplayName());
                            postViewModel = new ViewModelProvider(EditProfileActivity.this).get(PostsViewModel.class);
                            postViewModel.reload();
                            setResult(CHANGE_PROFILE_PICTURE_OR_DISPLAY_NAME_REQUEST);
                            finish();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(EditProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // If not valid, display a toast with an error message
            Toast.makeText(EditProfileActivity.this,
                    "Invalid Display Name", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageTaker.onActivityResult(requestCode, resultCode, data);

        if (imageTaker.getImageBitmap() != null) {
            ImageView showProfilePic = findViewById(R.id.profile_image);
            newProfileImage = imageTaker.getImageBitmap();
            showProfilePic.setImageBitmap(newProfileImage);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    private boolean isDisplayNameValid(String displayName) {
        return SignUpValidations.containsOnlyEnglishCharsAndNumbersAndSpace(displayName)
                && SignUpValidations.isStringLengthInRange(displayName, 2, 16); // Adjust the min and max length as needed
    }
}

