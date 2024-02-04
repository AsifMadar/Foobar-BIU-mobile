package il.ac.biu.project.foobar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/**
 * The main activity for the Foobar-BIU mobile app.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserDetails userDetails = UserDetails.getInstance();

        Button signUpPageButton = findViewById(R.id.signUpPageActivity);

        signUpPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the second activity when the button is clicked
                Intent intent = new Intent(MainActivity.this, SignUpPageActivity.class);
                startActivity(intent);
            }
        });
    }
}

