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
    static final int CREATE_POST_REQUEST = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserDetails userDetails = UserDetails.getInstance();
        if (!userDetails.getSignIn()) {
            Intent intent = new Intent(FeedActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_feed);
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
        addPostButton = findViewById(R.id.addPost);
        layout = findViewById(R.id.container);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDetails postDetails = new PostDetails("Author", null, "User input", null); // Assuming a constructor like this exists
                // Use YourActivityName.this instead of this
                Intent intent = new Intent(FeedActivity.this, CreatePostActivity.class);
                intent.putExtra("postDetails", postDetails);
                startActivityForResult(intent, CREATE_POST_REQUEST);
            }
        });
;

    }


    private void addPost(PostDetails postDetails) {
        View view = getLayoutInflater().inflate(R.layout.post, null);
        TextView nameView = view.findViewById(R.id.user_name);
        TextView postContent = view.findViewById(R.id.post_text);
        postContent.setText(postDetails.getUserInput());
        nameView.setText(postDetails.getAuthorDisplayName());
        ImageView profileImage = view.findViewById(R.id.profile_picture);
        profileImage.setImageBitmap(postDetails.getAuthorProfilePicture());
        if (postDetails.getPicture() != null) {
            ImageView postImage = view.findViewById(R.id.post_image);
            postImage.setImageBitmap(postDetails.getPicture());
            postImage.setVisibility(View.VISIBLE);
        }
        Button delete = view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
            }
        });

        layout.addView(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_POST_REQUEST && resultCode == RESULT_OK && data != null) {
            PostDetails modifiedPostDetails = data.getParcelableExtra("modifiedPostDetails");
            if (modifiedPostDetails != null) {
                addPost(modifiedPostDetails);
            }
        }
    }

}
