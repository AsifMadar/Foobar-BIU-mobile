package il.ac.biu.project.foobar;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FeedActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        TextView helloTextView = findViewById(R.id.helloTextView);
        helloTextView.setText("Hello World");
    }
}
