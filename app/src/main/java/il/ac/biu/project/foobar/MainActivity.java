package il.ac.biu.project.foobar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The main activity for the Foobar-BIU mobile app.
 */
public class MainActivity extends AppCompatActivity {
    private String givenUsername = "#"; //default value
    private String givenPassword = "#"; //default value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserDetails userDetails = UserDetails.getInstance();
        if (userDetails.getSignIn()) {
            Intent intent = new Intent(MainActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        }
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);


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
        Button signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (givenUsername.equals(userDetails.getUsername())
                        && givenPassword.equals(userDetails.getPassword())) {
                    userDetails.setSignIn(true);
                    Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this,
                            "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button signUpPageButton = findViewById(R.id.signUpPageActivity);

        signUpPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Sign up page
                Intent intent = new Intent(MainActivity.this, SignUpPageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

