package il.ac.biu.project.foobar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for sharing content.
 */
public class ShareActivity extends AppCompatActivity {

    /**
     * Initializes the activity.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        // Find the exit button in the layout
        Button exitButton = findViewById(R.id.exit_button);

        // Set click listener for the exit button
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the quit method when the button is clicked
                quit();
            }
        });
    }

    /**
     * Quits the activity and returns a result to the calling activity.
     */
    private void quit() {
        // Create a new intent to hold the result
        Intent resultIntent = new Intent();

        // Set the result code to indicate success
        setResult(RESULT_OK, resultIntent);

        // Finish the activity and return to the calling activity
        finish();
    }
}
