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
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import il.ac.biu.project.foobar.entities.Comment;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.PostManager;
import il.ac.biu.project.foobar.entities.UserDetails;


/**
 * CommentActivity manages the UI and functionality related to displaying and interacting with comments on a post.
 * It allows users to add new comments, edit or delete existing ones, and view all comments associated with a specific post.
 */
public class CommentActivity extends AppCompatActivity {
    private PostDetails postDetails;
    LinearLayout layout;
    UserDetails userDetails = UserDetails.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        // Fetch the post ID passed through the intent and retrieve the corresponding post details.
        int postId = getIntent().getIntExtra("postID", 0);
        PostManager postManager = PostManager.getInstance();
        postDetails = postManager.getPost(postId);
        //sets the container of the comments
        layout = findViewById(R.id.comments_container);
        //render the comments contained in the post
        renderComments();
        //render the abillity to add new comments
        renderAddComment();
    }

    /**
     * Sets up the UI for adding a new comment, including an EditText for input and a send button.
     */
    private void renderAddComment() {
        EditText commentText = findViewById(R.id.comment_text);
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

    /**
     * Publishes a new comment by adding it to the post's comment list and rendering it in the UI.
     * @param commentText The text of the comment to be published.
     * @param view The view to be used for displaying the comment.
     */
    private void publishComment(String commentText, View view) {
        Comment comment = new Comment();
        comment.setTime(System.currentTimeMillis());
        comment.setAuthorDisplayName(userDetails.getDisplayName());
        comment.setAuthorProfilePicture(userDetails.getImg());
        comment.setText(commentText);
        postDetails.addComment(comment);
        renderComment(postDetails.getCommentCount() - 1, view);
    }

    /**
     * Renders all comments associated with the post.
     */
    private void renderComments() {
        for (int i = 0; i < postDetails.getCommentCount(); i++) {
            View view = getLayoutInflater().inflate(R.layout.comment, null);
            layout.addView(view);
            renderComment(i, view);
        }
    }

    /**
     * Renders a single comment in the UI.
     * @param i The index of the comment in the post's comment list.
     * @param commentView The view to be used for displaying the comment.
     */
    private void renderComment(int i, View commentView) {
        Comment comment = postDetails.getComment(i);
        //sets profile picture
        if (comment.getAuthorProfilePicture() != null) {
            ImageView commentProfilePic = commentView.findViewById(R.id.profile_picture_comment);
            commentProfilePic.setImageBitmap(comment.getAuthorProfilePicture());
        }

        //sets comment author
        TextView authorName = commentView.findViewById(R.id.user_name_comment);
        authorName.setText(comment.getAuthorDisplayName());

        //sets comment text
        EditText commentText = commentView.findViewById(R.id.comment_text_body);
        commentText.setText(comment.getText());

        //sets like button color
        TextView likeButton = commentView.findViewById(R.id.like_button_comment);
        String username = userDetails.getUsername();

        if (comment.isLiked(username)) {
            likeButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_like));
        } else {
            likeButton.setTextColor(Color.GRAY);
        }
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display options dialog for the post
                if (!comment.isLiked(username)) {
                    likeButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_like));
                    comment.addLike(username);
                } else {
                    likeButton.setTextColor(Color.GRAY);
                    comment.removeLike(username);
                }
                setLikesCount(comment, commentView);
            }
        });

        setLikesCount(comment, commentView);
        //sets time
        TextView uploadTimeComment = commentView.findViewById(R.id.upload_time_comment);
        uploadTimeComment.setText(comment.getTimeStr());
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


    /**
     * Displays a dialog allowing the user to edit or delete a comment.
     * @param view The view associated with the comment to be edited or deleted.
     * @param comment The comment object to be edited or deleted.
     */    private void editOrDeleteButton(View view, Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");

        // Edit post option
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText commentText = view.findViewById(R.id.comment_text_body);
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
                    } else { //makes the comment read only
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
        resultData.putExtra("commentPostId", postDetails.getId());
        setResult(RESULT_OK, resultData);
        //destroy the activity after the user exits
        finish();
    }

}
