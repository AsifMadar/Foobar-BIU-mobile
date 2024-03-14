package il.ac.biu.project.foobar;

import static il.ac.biu.project.foobar.utils.images.base64ToBitmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import il.ac.biu.project.foobar.entities.AdvancedTextField;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.entities.responses.UserDetailsResponse;
import il.ac.biu.project.foobar.viewmodels.SignInViewModel;
import il.ac.biu.project.foobar.viewmodels.UserViewModel;

/**
 * The main activity for the Foobar-BIU mobile app. This class handles user authentication,
 * including login and navigation to the sign-up or feed activity depending on the user's
 * authentication status or actions.
 */
public class MainActivity extends AppCompatActivity {
    // Default values for username and password, used prior to user input.
    private String givenUsername = "#";
    private String givenPassword = "#";

    private SignInViewModel signInViewModel;
    private final UserDetails userDetails = UserDetails.getInstance();

    UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        protectSignInPage();

        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        initializeSignInPage();
    }

    private void initializeSignInPage() {
        signInViewModel.getSignInSuccess().observe(this, success -> {
            if(success) {
                userViewModel.fetchUserDetails(givenUsername, userDetails.getJwt());
            } else {
                Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

//        postsViewModel.get().observe(this, new Observer<List<PostDetails>>() {
//            @Override
//            public void onChanged(List<PostDetails> postsList) {
//                postsListAdapter.setPosts(postsList);
//            }
//        });
        userViewModel.getUserDetailsFetchSuccess().observe(this, new Observer<UserDetailsResponse>() {
            @Override
            public void onChanged(UserDetailsResponse userDetailsResponse) {
                if (userDetailsResponse != null) {
                    UserDetails user = UserDetails.getInstance();
                    user.setUsername(userDetailsResponse.getUsername());
                    user.setDisplayName(userDetailsResponse.getDisplayName());
                    user.setImg(base64ToBitmap(userDetailsResponse.getProfileImage()));
                    user.setFriends(userDetailsResponse.getFriends());
                    user.setFriendRequests(userDetailsResponse.getFriendRequests());
                    proceedToFeed();
                } else {
                    // Failure, show an error message
                    Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
            signInViewModel.signIn(givenUsername, givenPassword);
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


    private void proceedToFeed() {
        Intent intent = new Intent(MainActivity.this, FeedActivityMain.class);
        startActivity(intent);
        finish();
    }
    // Automatically navigate to FeedActivityMain if user is already signed in.
    private void protectSignInPage() {
        if (userDetails.getSignIn()) {
            Intent intent = new Intent(MainActivity.this, FeedActivityMain.class);
            startActivity(intent);
            finish();
        }
    }
}

