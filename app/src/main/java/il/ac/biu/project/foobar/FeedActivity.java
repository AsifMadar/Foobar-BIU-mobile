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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class FeedActivity extends AppCompatActivity {
    Button addPostButton;
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
        addPostButton = findViewById(R.id.addPost);
        layout = findViewById(R.id.container);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDetails postDetails = new PostDetails("Author", null, "User input", null, "time"); // Assuming a constructor like this exists
                // Use YourActivityName.this instead of this
                Intent intent = new Intent(FeedActivity.this, CreatePostActivity.class);
                intent.putExtra("postDetails", postDetails);
                startActivityForResult(intent, CREATE_POST_REQUEST);
            }
        });
    }


    private void addPost(PostDetails postDetails) {
        View view = getLayoutInflater().inflate(R.layout.post, null);
        TextView nameView = view.findViewById(R.id.user_name);
        //sets author of the post
        nameView.setText(postDetails.getAuthorDisplayName());
        //sets post text
        TextView postContent = view.findViewById(R.id.post_text);
        postContent.setText(postDetails.getUserInput());
        //sets profile picture
        ImageView profileImage = view.findViewById(R.id.profile_picture);
        profileImage.setImageBitmap(postDetails.getAuthorProfilePicture());
        //sets time
        TextView time = view.findViewById(R.id.upload_time);
        time.setText(postDetails.getTime());
        if (postDetails.getPicture() != null) {
            ImageView postImage = view.findViewById(R.id.post_image);
            postImage.setImageBitmap(postDetails.getPicture());
            postImage.setVisibility(View.VISIBLE);
        }
        ImageView postOptionsButton = view.findViewById(R.id.ellipsis_icon);
        postOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editOrDeleteButton(view);
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

    private void editOrDeleteButton(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");

        // Setting up the options
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Option 1 Click
                Toast.makeText(getApplicationContext(), "TODO edit", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Option 2 Click
                View parentView = (View) view.getParent().getParent(); // Assuming the structure is: your layout -> post layout -> ellipsis icon
                layout.removeView(parentView);
            }
        });

        // Creating and showing the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
