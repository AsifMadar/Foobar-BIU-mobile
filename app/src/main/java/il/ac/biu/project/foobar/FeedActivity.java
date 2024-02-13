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

import java.util.ArrayList;
import java.util.HashMap;

public class FeedActivity extends AppCompatActivity {
    int postCounter = 0;
    Button addPostButton;
    LinearLayout layout;
    boolean editingPost = false;
    PostManager postManager = PostManager.getInstance();
    HashMap<Integer, View> postViewMap = new HashMap<>();

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
                postCounter++;
                PostDetails postDetails = new PostDetails(postCounter, "Author", null, "User input", null, "time");
                postManager.putPost(postCounter, postDetails);

                Intent intent = new Intent(FeedActivity.this, CreatePostActivity.class);
                intent.putExtra("postID", postCounter);
                startActivityForResult(intent, CREATE_POST_REQUEST);
            }
        });
    }


    private void addPost(PostDetails postDetails) {
        View view = getLayoutInflater().inflate(R.layout.post, null);
        postInitializer(postDetails, view);
        layout.addView(view);
    }

    private void postInitializer(PostDetails postDetails, View view) {
        postViewMap.put(postDetails.getId(), view);
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
        ImageView postImage = view.findViewById(R.id.post_image);
        if (postDetails.getPicture() != null) {
            postImage.setImageBitmap(postDetails.getPicture());
            postImage.setVisibility(View.VISIBLE);
        } else {
            postImage.setVisibility(View.GONE);
        }
        ImageView postOptionsButton = view.findViewById(R.id.ellipsis_icon);
        postOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editOrDeleteButton(postViewMap.get(postCounter), postDetails);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_POST_REQUEST && resultCode == RESULT_OK && data != null) {
            int modifiedPostId = data.getIntExtra("modifiedPostId", 0);
            if (postManager.getPost(modifiedPostId) != null) {
                if (editingPost) {
                    postInitializer(postManager.getPost(modifiedPostId), postViewMap.get(modifiedPostId));
                    editingPost = false;
                } else {
                    addPost(postManager.getPost(modifiedPostId));
                }
            }
        }
    }

    private void editOrDeleteButton(View view, PostDetails postDetails) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");

        // Edit post
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editingPost = true;
                Intent intent = new Intent(FeedActivity.this, CreatePostActivity.class);
                intent.putExtra("postID", postDetails.getId());
                startActivityForResult(intent, CREATE_POST_REQUEST);
            }
        });
        //remove post
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                layout.removeView(postViewMap.get(postDetails.getId()));
                postViewMap.remove(postDetails.getId());
                postManager.removePost(postDetails.getId());
            }
        });

        // Creating and showing the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
