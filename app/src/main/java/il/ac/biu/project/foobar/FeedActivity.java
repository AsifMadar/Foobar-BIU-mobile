package il.ac.biu.project.foobar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class FeedActivity extends AppCompatActivity {
    Button addPostButton;
    AlertDialog dialog;
    LinearLayout layout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserDetails userDetails = UserDetails.getInstance();
        if (!userDetails.getSignIn()) {
            Intent intent = new Intent(FeedActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_feed);
        addPostButton = findViewById(R.id.addPost);
        layout = findViewById(R.id.container);
        buildAddPost();
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        Button logoutButton = findViewById(R.id.logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDetails.setSignIn(false);
                // Go to Sign in page
                Intent intent = new Intent(FeedActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void buildAddPost() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        EditText name = view.findViewById(R.id.nameEdit);

        builder.setView(view);
        builder.setTitle("What are you thinking about?")
                .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addPost(name.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog = builder.create();
    }

    private void addPost(String string) {
        View view = getLayoutInflater().inflate(R.layout.post, null);
        UserDetails user = UserDetails.getInstance();
        TextView nameView = view.findViewById(R.id.user_name);
        TextView postContent = view.findViewById(R.id.post_text);
        postContent.setText(string);
        Button delete = view.findViewById(R.id.delete);
        nameView.setText(user.getDisplayName());
        ImageView profileImage = view.findViewById(R.id.profile_picture);
        profileImage.setImageBitmap(user.getImg());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
            }
        });

        layout.addView(view);
    }
}
