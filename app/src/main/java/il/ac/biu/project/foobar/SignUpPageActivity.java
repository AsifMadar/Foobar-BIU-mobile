package il.ac.biu.project.foobar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
                && rePassword.equals(password);
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
        usernameField.setErrorMessage("not 5-16 english chars or numbers");

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

        passwordField.setErrorMessage("not 8-20 english chars or numbers");
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
        displayNameField.setErrorMessage("not 2-16 english chars or numbers");

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
                    // Assuming you want to pass the captured photo to FeedActivity
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
}
