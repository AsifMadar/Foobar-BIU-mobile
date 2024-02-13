package il.ac.biu.project.foobar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.HashMap;

public class FeedActivity extends AppCompatActivity {
    int postCounter = 0;
    LinearLayout layout;
    boolean editingPost = false;
    PostManager postManager = PostManager.getInstance();
    HashMap<Integer, View> postViewMap = new HashMap<>();
    UserDetails userDetails = UserDetails.getInstance();


    static final int CREATE_POST_REQUEST = 1;
    static final int SHARE_PAGE_REQUEST = 2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!userDetails.getSignIn()) {
            Intent intent = new Intent(FeedActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_feed);
        Button addPostButton = findViewById(R.id.addPost);
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
        //Edit or delete section
        ImageView postOptionsButton = view.findViewById(R.id.ellipsis_icon);
        postOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editOrDeleteButton(postViewMap.get(postCounter), postDetails);
            }
        });
        // Like section
        LinearLayout likeCount = view.findViewById(R.id.like_layout);
        likeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View postView = postViewMap.get(postDetails.getId());
                String userDisplayName = userDetails.getDisplayName();
                int numOfLikes = postDetails.getNumberOfLikes();
                ImageView likeIcon = postView.findViewById(R.id.like_icon);
                TextView numOfLikeView = postView.findViewById(R.id.like_count);
                TextView likeText = postView.findViewById(R.id.like_text);
                if (postDetails.isLiked(userDisplayName)) {
                    postDetails.removeLike(userDisplayName);
                    numOfLikes--;
                    likeIcon.setImageResource(R.drawable.like_icon_not_pressed);
                    likeText.setTextColor(Color.BLACK);
                    if (numOfLikes == 0) {
                        numOfLikeView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    postDetails.addLike(userDisplayName);
                    numOfLikes++;
                    if (numOfLikes == 1) {
                        numOfLikeView.setVisibility(View.VISIBLE);
                    }
                    likeIcon.setImageResource(R.drawable.like_icon_pressed);
                    likeText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_like));
                }
                numOfLikeView.setText(numOfLikes + " likes");

            }
        });

        // Share section
        LinearLayout shareLayout = view.findViewById(R.id.share_layout);
        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentShareActivity = new Intent(FeedActivity.this, ShareActivity.class);
                startActivityForResult(intentShareActivity, SHARE_PAGE_REQUEST);
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
        if (requestCode == SHARE_PAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                // What to do when share screen is over (for next assignment maybe)
            } else {
                // What to do when share screen is over (for next assignment maybe)
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
