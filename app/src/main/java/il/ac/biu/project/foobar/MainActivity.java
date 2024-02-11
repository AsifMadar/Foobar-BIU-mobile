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
        Button btn = findViewById(R.id.loginbtn);
        btn.setOnClickListener(v -> {
            Intent i= new Intent(this, FeedActivity.class);
            startActivity(i);
        });

    }

}
