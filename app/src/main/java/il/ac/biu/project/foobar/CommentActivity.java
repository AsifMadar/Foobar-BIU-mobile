package il.ac.biu.project.foobar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CommentActivity extends AppCompatActivity {
    private PostDetails postDetails;
    LinearLayout layout;
    UserDetails userDetails = UserDetails.getInstance();
    boolean editingComment = false;
    static final int EDIT_COMMENT_REQUEST = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        int postId = getIntent().getIntExtra("postID", 0);
        PostManager postManager = PostManager.getInstance();
        postDetails = postManager.getPost(postId);
        layout = findViewById(R.id.comments_container);
        renderComments();
        renderAddComment();
    }

    private void renderAddComment() {
        EditText commentText = findViewById(R.id.comment_text);
        String check = commentText.getText().toString();
        ImageView sendCommentButton = findViewById(R.id.send_comment_button);
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentText.getText().toString().trim().equals("")) {
                    Toast.makeText(CommentActivity.this,
                            "Cannot post empty comment", Toast.LENGTH_SHORT).show();
                } else {
                    View view = getLayoutInflater().inflate(R.layout.comment, null);
                    layout.addView(view);
                    publishComment(commentText.getText().toString(), view);
                    commentText.setText("");
                }
            }
        });
    }

    private void publishComment(String commentText, View view) {
        Comment comment = new Comment();
        comment.setTime(getTimeAndDate());
        comment.setAuthorName(userDetails.getDisplayName());
        comment.setAuthorPicture(userDetails.getImg());
        comment.setText(commentText);
        postDetails.addComment(comment);
        renderComment(postDetails.getCommentCount() - 1, view);

    }

    private String getTimeAndDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE HH:mm");
        return now.format(formatter);
    }

    private void renderComments() {
        for (int i = 0; i < postDetails.getCommentCount(); i++) {
            View view = getLayoutInflater().inflate(R.layout.comment, null);
            layout.addView(view);
            renderComment(i, view);
        }
    }

    private void renderComment(int i, View commentView) {
        Comment comment = postDetails.getComment(i);
        //sets profile picture
        if (comment.getAuthorPicture() != null) {
            ImageView commentProfilePic = commentView.findViewById(R.id.profile_picture_comment);
            commentProfilePic.setImageBitmap(comment.getAuthorPicture());
        }

        //sets comment author
        TextView authorName = commentView.findViewById(R.id.user_name_comment);
        authorName.setText(comment.getAuthorName());

        //sets comment text
        EditText commentText = commentView.findViewById(R.id.comment_text);
        commentText.setText(comment.getText());

        //sets like button color
        TextView likeButton = commentView.findViewById(R.id.like_button_comment);
        String username = userDetails.getUsername();

        if (comment.isLiked(username)) {
            likeButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_like));
        } else {
            likeButton.setTextColor(Color.BLACK);
        }
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display options dialog for the post
                if (!comment.isLiked(username)) {
                    likeButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_like));
                    comment.addLike(username);
                } else {
                    likeButton.setTextColor(Color.BLACK);
                    comment.removeLike(username);
                }
                setLikesCount(comment, commentView);
            }
        });

        setLikesCount(comment, commentView);
        //sets time
        TextView uploadTimeComment = commentView.findViewById(R.id.upload_time_comment);
        uploadTimeComment.setText(comment.getTime());
        //sets options for the comment
        ImageView commentOptionsButton = commentView.findViewById(R.id.ellipsis_icon_comment);
        commentOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display options dialog for the post
                editOrDeleteButton(commentView, comment);
            }
        });
    }

    private void setLikesCount(Comment comment, View view) {
        //sets likes count
        ImageView likeIconComment = view.findViewById(R.id.like_icon_comment);
        TextView likeCounterComment = view.findViewById(R.id.like_counter_comment);
        if (comment.getLikesCount() == 0) {
            likeIconComment.setVisibility(View.INVISIBLE);
            likeCounterComment.setVisibility(View.INVISIBLE);
        } else {
            likeCounterComment.setText(Integer.toString(comment.getLikesCount()));
            likeCounterComment.setVisibility(View.VISIBLE);
            likeIconComment.setVisibility(View.VISIBLE);
        }
    }


    // Method to display edit or delete dialog for a comment
    private void editOrDeleteButton(View view, Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");

        // Edit post option
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editingComment = true;
                EditText commentText = view.findViewById(R.id.comment_text);
                // Make EditText editable
                commentText.setFocusableInTouchMode(true);
                commentText.setFocusable(true);
                commentText.setClickable(true);
                commentText.requestFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(commentText, InputMethodManager.SHOW_IMPLICIT);

                ImageView saveEdit = view.findViewById(R.id.comment_accept_edit);
                saveEdit.setVisibility(View.VISIBLE);

                // Handle "Save" action
                saveEdit.setOnClickListener(v -> {
                    if (commentText.getText().toString().trim().equals("")) {
                        Toast.makeText(CommentActivity.this,
                                "Cannot post empty comment", Toast.LENGTH_SHORT).show();
                    } else {
                        comment.setText(commentText.getText().toString());
                        commentText.setFocusableInTouchMode(false);
                        commentText.setFocusable(false);
                        commentText.setClickable(false);
                        saveEdit.setVisibility(View.GONE);
                    }
                });

            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Remove post view and data from layout and PostManager
                layout.removeView(view);
                postDetails.removeComment(comment);
            }
        });
        // Show dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent resultData = new Intent();
        // Assuming postDetails.getId() correctly fetches the ID you want to return.
        resultData.putExtra("commentPostId", postDetails.getId());

        // RESULT_OK is a standard resultCode indicating successful operation
        setResult(RESULT_OK, resultData);

        // Call finish() to destroy this activity
        finish();

        // Do not call super.onBackPressed() after finish()
    }

}
