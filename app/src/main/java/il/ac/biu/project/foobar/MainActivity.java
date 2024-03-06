package il.ac.biu.project.foobar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import il.ac.biu.project.foobar.api.SignInAPI;
import il.ac.biu.project.foobar.entities.AdvancedTextField;
import il.ac.biu.project.foobar.entities.SignInRequest;
import il.ac.biu.project.foobar.entities.UserDetails;

/**
 * The main activity for the Foobar-BIU mobile app. This class handles user authentication,
 * including login and navigation to the sign-up or feed activity depending on the user's
 * authentication status or actions.
 */
public class MainActivity extends AppCompatActivity {
    // Default values for username and password, used prior to user input.
    private String givenUsername = "#";
    private String givenPassword = "#";
    private SignInAPI signInAPI = new SignInAPI();

    private final UserDetails userDetails = UserDetails.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        protectSignInPage();

        initializeSignInPage();

    }

    // Automatically navigate to FeedActivityMain if user is already signed in.
    private void protectSignInPage() {
        if (userDetails.getSignIn()) {
            Intent intent = new Intent(MainActivity.this, FeedActivityMain.class);
            startActivity(intent);
            finish();
        }
    }

    private void initializeSignInPage() {
        // Initialize text fields for username and password input.
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        // AdvancedTextField wrappers to add input changed listeners to EditTexts.
        AdvancedTextField usernameField = new AdvancedTextField(usernameEditText, new AdvancedTextField.InputCallback() {
            @Override
            public void onInputChanged(String input) {
                givenUsername = input;
            }
        });

        AdvancedTextField passwordField = new AdvancedTextField(passwordEditText, new AdvancedTextField.InputCallback() {
            @Override
            public void onInputChanged(String input) {
                givenPassword = input;
            }
        });

        setSignInButton();
        setSignUpButton();

    }

    // Setup sign in button with a click listener to handle user authentication.
    private void setSignInButton() {
        Button signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            signIn();
            }
        });
    }

    // Setup button to navigate to the sign-up page.
    private void setSignUpButton() {
        Button signUpPageButton = findViewById(R.id.signUpPageActivity);
        signUpPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to SignUpPageActivity for new user registration.
                Intent intent = new Intent(MainActivity.this, SignUpPageActivity.class);
                startActivity(intent);
                finish(); // Close current activity.
            }
        });
    }

    //check if the username and password are correct, sign in if true
    private void signIn() {
        SignInRequest signInRequest = new SignInRequest(givenUsername, givenPassword);
        signInAPI.signInToServer(signInRequest, new SignInAPI.SignInResponseCallback() {
            @Override
            public void onSuccess(String jwtToken) {
                userDetails.setJwt(jwtToken);
                runOnUiThread(() -> {
                    userDetails.setSignIn(true); // Update sign-in status.
                    proceedToNextActivity(); // Method to continue to the feed
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                if (errorMessage.equals("Error: 404")) {
                    Toast.makeText(MainActivity.this,
                            "Invalid username or password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "FAILED - " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void proceedToNextActivity() {
        Intent intent = new Intent(MainActivity.this, FeedActivityMain.class);
        startActivity(intent);
        finish();
    }
}
