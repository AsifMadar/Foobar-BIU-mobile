package il.ac.biu.project.foobar;

import static il.ac.biu.project.foobar.utils.SignUpValidations.containsOnlyEnglishCharsAndNumbers;
import static il.ac.biu.project.foobar.utils.SignUpValidations.containsOnlyEnglishCharsAndNumbersAndSpace;
import static il.ac.biu.project.foobar.utils.SignUpValidations.isAllValid;
import static il.ac.biu.project.foobar.utils.SignUpValidations.isStringLengthInRange;

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

import java.util.ArrayList;

import androidx.lifecycle.ViewModelProvider;

import java.util.LinkedList;

import il.ac.biu.project.foobar.entities.AdvancedTextField;
import il.ac.biu.project.foobar.entities.AdvancedTextField.InputCallback;
import il.ac.biu.project.foobar.entities.AdvancedTextField.ValidationFunction;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.viewmodels.SignInViewModel;
import il.ac.biu.project.foobar.viewmodels.SignUpViewModel;


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
    private ImageTaker imageTaker;
    private ArrayList<String> friends=new ArrayList<>();
    private ArrayList<String> friendRequests=friends=new ArrayList<>();;
    private final UserDetails userDetails = UserDetails.getInstance();

    private SignUpViewModel signUpViewModel;
    private SignInViewModel signInViewModel;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

        protectSignUpPage();
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        setSignUpPage();
        //sample for tests
        setFriendRequests();
    }
    private void setFriendRequests() {
        // Add sample friend requests to the list
        friendRequests = new ArrayList<>();
        friendRequests.add("Friend 1");
        friendRequests.add("Friend 2");
        friendRequests.add("Friend 3");
        // You can add more sample friend requests as needed
    }

    private void setSignUpPage() {
        // Initialize AdvancedTextField instances for each input field

        setUserNameEditField();

        setPasswordEditText();

        setRePasswordEditText();

        setDisplayNameEditText();

        setAddPhotoButton();

        setSignUpButton();

        setSignInPageButton();
    }

    //sets username EditField
    private void setUserNameEditField() {
        EditText usernameEditText = findViewById(R.id.usernameEditText);
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
    }

    //sets password EditField
    private void setPasswordEditText() {
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
    }

    //sets retype password EditField
    private void setRePasswordEditText() {
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
    }

    //sets display name EditField
    private void setDisplayNameEditText() {
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
                return containsOnlyEnglishCharsAndNumbersAndSpace(input)
                        && isStringLengthInRange(input, 2, 16);
            }
        });
        displayNameField.setErrorMessage("not 2-16 alphanumeric characters");
    }

    // sets add photo button
    private void setAddPhotoButton() {
        imageTaker = new ImageTaker(this);
        Button addImgButton = findViewById(R.id.imgButton);
        addImgButton.setOnClickListener(view -> imageTaker.pickImage());
    }

    // sets sign up button which goes to feed if given valid user details
    private void setSignUpButton() {
        signUpViewModel.getSignUpSuccess().observe(this, success -> {
            if(success) {
                // If valid, update UserDetails and start FeedActivity
                userDetails.setUsername(username);
                userDetails.setPassword(password);
                userDetails.setDisplayName(displayName);
                userDetails.setImg(img);
                userDetails.setFriendsList(new LinkedList<String>());
                signInViewModel.signIn(username, password);
            } else {
                Toast.makeText(SignUpPageActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
            }
        });
        signInViewModel.getSignInSuccess().observe(this, success -> {
            if(success) {
                userDetails.setSignIn(true);
                proceedToFeed();
            } else {
                Toast.makeText(SignUpPageActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
        // Find the "Feed Activity" button in the layout
        Button feedActivityButton = findViewById(R.id.feedActivity);

        // Set a click listener for the button
        feedActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if all input fields are valid
                if (isAllValid(username, password, displayName, rePassword, img)) {
                    signUpViewModel.signUp(username, password, displayName, img);
                } else {
                    // If not valid, display a toast with an error message
                    Toast.makeText(SignUpPageActivity.this,
                            "Invalid Fields or missing a photo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void proceedToFeed() {
        Intent intent = new Intent(SignUpPageActivity.this, FeedActivityMain.class);
        startActivity(intent);
        finish();
    }

    //sets a button to navigate to sign in page
    private void setSignInPageButton() {
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

    // if the user has signed in go to feed
    private void protectSignUpPage() {
        if (userDetails.getSignIn()) {
            Intent intent = new Intent(SignUpPageActivity.this, FeedActivityMain.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageTaker.onActivityResult(requestCode, resultCode, data);

        if (imageTaker.getImageBitmap() != null) {
            ImageView showProfilePic = findViewById(R.id.profilePic);
            showProfilePic.setImageBitmap(imageTaker.getImageBitmap());
            img = imageTaker.getImageBitmap();
            showProfilePic.setVisibility(View.VISIBLE);
        }
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
        imageTaker.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
