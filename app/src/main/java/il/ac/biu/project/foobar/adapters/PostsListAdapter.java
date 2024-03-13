package il.ac.biu.project.foobar.adapters;


import static il.ac.biu.project.foobar.FeedActivityMain.COMMENT_PAGE_REQUEST;
import static il.ac.biu.project.foobar.FeedActivityMain.EDIT_POST_REQUEST;
import static il.ac.biu.project.foobar.FeedActivityMain.SHARE_PAGE_REQUEST;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import il.ac.biu.project.foobar.CommentActivity;
import il.ac.biu.project.foobar.CreatePostActivity;
import il.ac.biu.project.foobar.R;
import il.ac.biu.project.foobar.ShareActivity;
import il.ac.biu.project.foobar.entities.AddLikePostListener;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.PostManager;
import il.ac.biu.project.foobar.entities.PostRemoveListener;
import il.ac.biu.project.foobar.entities.UserDetails;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

    class PostViewHolder extends  RecyclerView.ViewHolder {


        private PostViewHolder(View itemView) {
            super(itemView);
        }
    }

    private final LayoutInflater postInflater;
    private List<PostDetails> posts;
    private UserDetails userDetails = UserDetails.getInstance();
    private final Activity feedActivity;

    private final Context feedContext;

    private PostRemoveListener postRemoveListener;
    private AddLikePostListener addLikePostListener;



    public PostsListAdapter(Activity feedActivity, Context context, PostRemoveListener removeListener,
                            AddLikePostListener addLikePostListener) {

        postInflater = LayoutInflater.from(context);
        this.feedActivity = feedActivity;
        feedContext = context;
        postRemoveListener = removeListener;
        this.addLikePostListener = addLikePostListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = postInflater.inflate(R.layout.post, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsListAdapter.PostViewHolder holder, int position) {
        if (posts != null) {
            final PostDetails currentPost = posts.get(position);
            postInitializer(currentPost, holder.itemView);
        }
    }

    @Override
    public int getItemCount() {
        if (posts != null)
            return posts.size();
        return 0;
    }

    public void setPosts(List<PostDetails> s) {
        Collections.sort(s, new Comparator<PostDetails>() {
            @Override
            public int compare(PostDetails p1, PostDetails p2) {
                // Sort in descending order of time (most recent first)
                return Long.compare(p2.getTime(), p1.getTime());
            }
        });
        posts = s;
        notifyDataSetChanged();
    }

    /**
     * Initializes a post view with the provided post details. This includes setting up
     * the author name, post content, profile picture, and time. It also sets up listeners
     * for post options, like, comment, and share interactions.
     * @param postDetails The details of the post to initialize the view with.
     * @param view The view to be initialized.
     */
    private void postInitializer(PostDetails postDetails, View view) {


        // Set author name
        TextView nameView = view.findViewById(R.id.user_name);
        nameView.setText(postDetails.getAuthorDisplayName());

        // Set post content
        TextView postContent = view.findViewById(R.id.post_text);
        postContent.setText(postDetails.getUserInput());

        // Set profile picture
        ImageView profileImage = view.findViewById(R.id.profile_picture);
        profileImage.setImageBitmap(postDetails.getAuthorProfilePicture());

        // Set time
        TextView time = view.findViewById(R.id.upload_time);
        time.setText(postDetails.getTimeStr());

        // Set post image if available
        ImageView postImage = view.findViewById(R.id.post_image);
        if (postDetails.getPicture() != null) {
            postImage.setImageBitmap(postDetails.getPicture());
            postImage.setVisibility(View.VISIBLE);
        } else {
            postImage.setVisibility(View.GONE);
        }

        // Set click listener for options button
        ImageView postOptionsButton = view.findViewById(R.id.ellipsis_icon);
        postOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display options dialog for the post
                editOrDeleteButton(view, postDetails);
            }
        });

        if(!postDetails.getUsername().equals(userDetails.getUsername())) {
            postOptionsButton.setVisibility(View.INVISIBLE);
        }

        // Initialize likes count
        LinearLayout likeLayout = view.findViewById(R.id.like_layout);
        int numOfLikes = postDetails.getNumberOfLikes();
        TextView likeCount = view.findViewById(R.id.like_count);
        if (numOfLikes > 0) {
            likeCount.setVisibility(View.VISIBLE);
            likeCount.setText(numOfLikes + " likes");
        } else {
            likeCount.setVisibility(View.INVISIBLE);
        }
        ImageView likeIcon = view.findViewById(R.id.like_icon);
        TextView likeText = view.findViewById(R.id.like_text);

        // Update like button image
        if (postDetails.isLiked(userDetails.getUsername())) {
            likeIcon.setImageResource(R.drawable.like_icon_pressed);
            likeText.setTextColor(ContextCompat.getColor(feedContext.getApplicationContext(), R.color.blue_like));

        } else {
            likeIcon.setImageResource(R.drawable.like_icon_not_pressed);
            likeText.setTextColor(Color.GRAY);
        }



        // Set click listener for like section
        likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle like button click
                handleLikeButtonClick(view, postDetails);
                addLikePostListener.onLikePost(postDetails);
            }
        });

        //comment section
        TextView commentCountLayout = view.findViewById(R.id.comment_count);
        if (postDetails.getCommentCount() > 0) {
            commentCountLayout.setVisibility(View.VISIBLE);
            commentCountLayout.setText(postDetails.getCommentCount() + " Comments");
        } else {
            commentCountLayout.setVisibility(View.INVISIBLE);
        }

        // Set click listener for comment section
        LinearLayout commentLayout = view.findViewById(R.id.comment_layout);
        commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCommentActivity = new Intent(feedActivity, CommentActivity.class);
                intentCommentActivity.putExtra("postID", postDetails.getId());
                feedActivity.startActivityForResult(intentCommentActivity, COMMENT_PAGE_REQUEST);
            }
        });

        // Set click listener for share section
        LinearLayout shareLayout = view.findViewById(R.id.share_layout);
        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start ShareActivity for sharing the post
                Intent intentShareActivity = new Intent(feedActivity, ShareActivity.class);
                feedActivity.startActivityForResult(intentShareActivity, SHARE_PAGE_REQUEST);
            }
        });
    }
    /**
     * Displays an AlertDialog with options to edit or delete a post. Based on the user's choice,
     * it either starts CreatePostActivity for editing the post or removes the post from the layout
     * and PostManager.
     * @param postView The view of the post to be edited or deleted.
     * @param postDetails The details of the post associated with the view.
     */
    private void editOrDeleteButton(View postView, PostDetails postDetails) {
        PostManager.getInstance().putPost(postDetails.getId(), postDetails);
        AlertDialog.Builder builder = new AlertDialog.Builder(feedContext);
        builder.setTitle("Choose an option");

        // Edit post option
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(feedContext, CreatePostActivity.class);
                intent.putExtra("postID", postDetails.getId());
                feedActivity.startActivityForResult(intent, EDIT_POST_REQUEST);
            }
        });

        // Delete post option
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                postRemoveListener.onDeletePost(postDetails);
            }
        });

        // Show dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Handles like button clicks. Updates the like count and icon based on whether the post is
     * already liked by the user.
     * @param postDetails The details of the post being liked or unliked.
     */
    private void handleLikeButtonClick(View view, PostDetails postDetails) {
        View postView = (View) view.getParent().getParent().getParent();

        String userName = userDetails.getUsername();
        int numOfLikes = postDetails.getNumberOfLikes();
        ImageView likeIcon = postView.findViewById(R.id.like_icon);
        TextView numOfLikeView = postView.findViewById(R.id.like_count);

        TextView likeText = postView.findViewById(R.id.like_text);

        if (postDetails.isLiked(userName)) {
            postDetails.removeLike(userName);
            numOfLikes--;
            likeIcon.setImageResource(R.drawable.like_icon_not_pressed);
            likeText.setTextColor(Color.GRAY);
            if (numOfLikes == 0) {
                numOfLikeView.setVisibility(View.INVISIBLE);
            }
        } else {
            postDetails.addLike(userName);
            numOfLikes++;

            numOfLikeView.setVisibility(View.VISIBLE);
            likeIcon.setImageResource(R.drawable.like_icon_pressed);
            likeText.setTextColor(ContextCompat.getColor(feedContext.getApplicationContext(), R.color.blue_like));
        }
        numOfLikeView.setText(numOfLikes + " likes");
    }



}
