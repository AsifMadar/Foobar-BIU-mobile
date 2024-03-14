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

import il.ac.biu.project.foobar.api.users.EditUserDetailsAPI;
import il.ac.biu.project.foobar.entities.AdvancedTextField;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.utils.ImageTaker;
import il.ac.biu.project.foobar.utils.SignUpValidations;
import okhttp3.internal.Util;

public class EditProfileActivity extends AppCompatActivity {

    private EditText displayNameEditText;
    private EditUserDetailsAPI editUserDetailsApi;
    private ImageTaker imageTaker;
    private UserDetails userDetails;

    private  String newDisplayName; //The user input of new display name
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        displayNameEditText = findViewById(R.id.displayNameEditText);
        userDetails = UserDetails.getInstance();

        // Set current display name
        displayNameEditText.setText(userDetails.getDisplayName());

        setProfileImage();

        setSaveButton();
    }

    private void setProfileImage() {

        AdvancedTextField displayNameTextFIeld = new AdvancedTextField(displayNameEditText, new AdvancedTextField.InputCallback() {
            @Override
            public void onInputChanged(String input) {
                newDisplayName = input;
            }
        }, new AdvancedTextField.ValidationFunction() {
            @Override
            public boolean isValid(String input) {
                return SignUpValidations.containsOnlyEnglishCharsAndNumbersAndSpace(input);
            }
        });
        imageTaker = new ImageTaker(this);
        Button profileImageView= findViewById(R.id.changeImage);
        profileImageView.setOnClickListener(view -> imageTaker.pickImage());

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
        String newDisplayName = displayNameEditText.getText().toString().trim();
        Bitmap newProfileImage = imageTaker != null ? imageTaker.getImageBitmap() : userDetails.getImg() ;

        editUserDetailsApi.editUserDetails(userDetails.getUsername(), userDetails.getJwt(),
                newDisplayName, newProfileImage, new EditUserDetailsAPI.UserEditResponseCallback() {
                    @Override
                    public void onSuccess() {
                        userDetails.setDisplayName(newDisplayName);
                        if (newProfileImage != null) {
                            userDetails.setImg(newProfileImage);
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(EditProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imageTaker != null) {
            imageTaker.onActivityResult(requestCode, resultCode, data);
            ImageView profileImageView = findViewById(R.id.profileImageView);
            profileImageView.setImageBitmap(imageTaker.getImageBitmap());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (imageTaker != null) {
            imageTaker.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

