package il.ac.biu.project.foobar;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Objects;

public class HomeFragment extends Fragment {
    TextView textViewadd;
    boolean editingPost = false;
    AlertDialog dialog;
    LinearLayout layout;
    int postCounter = 0;
    HashMap<Integer, View> postViewMap = new HashMap<>();
    static final int CREATE_POST_REQUEST = 1;
    static final int SHARE_PAGE_REQUEST = 2;
    PostManager postManager = PostManager.getInstance();
    ImageView profileImage;
    UserDetails userDetails = UserDetails.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the TextView within the inflated layout
        textViewadd = view.findViewById(R.id.whats_on_your_mind_textview);
        layout = view.findViewById(R.id.container);
        profileImage = view.findViewById(R.id.profile_image);



        // Set user profile picture
        Bitmap userProfilePicture = UserDetails.getInstance().getImg();
        if (userProfilePicture != null) {
            profileImage.setImageBitmap(userProfilePicture);
        }

        layout = view.findViewById(R.id.container);
        setAddPostButton(view);




        return view;
    }

    private void setAddPostButton(View view) {
        TextView addPostButton = view.findViewById(R.id.whats_on_your_mind_textview);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment post counter
                postCounter++;
                // Create post details
                PostDetails postDetails = new PostDetails(postCounter, "Author", null, "User input", null, "time");
                // Add post to PostManager
                postManager.putPost(postCounter, postDetails);

                // Start CreatePostActivity to create a new post
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                intent.putExtra("postID", postCounter);
                startActivityForResult(intent, CREATE_POST_REQUEST);
            }
        });
    }
    private void addPost(PostDetails postDetails) {
        // Inflate post layout
        View view = getLayoutInflater().inflate(R.layout.post, null);
        // Initialize post view
        postInitializer(postDetails, view);
        // Add post view to layout
        layout.addView(view);
    }


    // Method to initialize post view
    private void postInitializer(PostDetails postDetails, View view) {
        // Store post view
        postViewMap.put(postDetails.getId(), view);

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
        time.setText(postDetails.getTime());

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
                editOrDeleteButton(postViewMap.get(postCounter), postDetails);
            }
        });

        // Set click listener for like section
        LinearLayout likeCount = view.findViewById(R.id.like_layout);
        likeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle like button click
                handleLikeButtonClick(postDetails);
            }
        });

        // Set click listener for share section
        LinearLayout shareLayout = view.findViewById(R.id.share_layout);
        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start ShareActivity for sharing the post
                Intent intentShareActivity = new Intent(getActivity(), ShareActivity.class);
                startActivityForResult(intentShareActivity, SHARE_PAGE_REQUEST);
            }
        });
    }
    // Method to handle result from activities

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_POST_REQUEST && resultCode == RESULT_OK && data != null) {
            int modifiedPostId = data.getIntExtra("modifiedPostId", 0);
            if (postManager.getPost(modifiedPostId) != null) {
                if (editingPost) {
                    // Update post view if editing post
                    postInitializer(postManager.getPost(modifiedPostId), postViewMap.get(modifiedPostId));
                    editingPost = false;
                } else {
                    // Add new post view
                    addPost(postManager.getPost(modifiedPostId));
                }
            }
        }
        if (requestCode == SHARE_PAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Handle successful share activity completion
            } else {
                // Handle unsuccessful share activity completion
            }
        }
    }
    // Method to display edit or delete dialog for a post
    private void editOrDeleteButton(View view, PostDetails postDetails) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity());
        builder.setTitle("Choose an option");

        // Edit post option
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editingPost = true;
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                intent.putExtra("postID", postDetails.getId());
                startActivityForResult(intent, CREATE_POST_REQUEST);
            }
        });

        // Delete post option
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Remove post view and data from layout and PostManager
                layout.removeView(postViewMap.get(postDetails.getId()));
                postViewMap.remove(postDetails.getId());
                postManager.removePost(postDetails.getId());
            }
        });

        // Show dialog
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void handleLikeButtonClick(PostDetails postDetails) {
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
            likeText.setTextColor(ContextCompat.getColor(requireActivity(), R.color.blue_like));
        }
        numOfLikeView.setText(numOfLikes + " likes");
    }
}