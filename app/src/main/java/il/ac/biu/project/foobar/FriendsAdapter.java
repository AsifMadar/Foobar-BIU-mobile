package il.ac.biu.project.foobar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import il.ac.biu.project.foobar.api.friends.RejectFriendRequestAPI;
import il.ac.biu.project.foobar.entities.UserDetails;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private ArrayList<String> friendsList;
    private BottomSheetDialog dialog; // Added member variable


    public FriendsAdapter(ArrayList<String> friendsList) {
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String friend = friendsList.get(position);
        holder.friendTextView.setText(friend);

        // Handle click event to show bottom sheet dialog
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show bottom sheet dialog with menu options
                showBottomSheetDialog(v.getContext(), friend); // Pass the friend name to the method
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView friendTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendTextView = itemView.findViewById(R.id.friendTextView);
        }
    }

    private void showBottomSheetDialog(Context context, String friendName) {
        // Inflate the layout for menu options
        View view = LayoutInflater.from(context).inflate(R.layout.menu_options, null);

        // Create a BottomSheetDialog
        dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);
        dialog.show();

        // Get references to menu options
        TextView watchProfileText = view.findViewById(R.id.watchProfileText);
        TextView removeFriendText = view.findViewById(R.id.removeFriendText);

        // Set click listeners for menu options
        watchProfileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle watch profile action
                // For example: open profile activity
            }
        });

        removeFriendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle remove friend action
                deleteFriend(friendName); // Call method to delete friend
            }
        });
    }

    private void deleteFriend(String friendName) {
        // Initialize DeleteFriendAPI
        RejectFriendRequestAPI deleteFriendAPI = new RejectFriendRequestAPI();

        // Get JWT token from UserDetails
        String jwtToken = UserDetails.getInstance().getJwt();

        // Call the API to delete the friend
        deleteFriendAPI.rejectFriendRequestOrDeleteFriend(UserDetails.getInstance().getUsername(), friendName, jwtToken, new RejectFriendRequestAPI.RejectFriendRequestCallback() {
            @Override
            public void onSuccess() {
                // Handle successful deletion
                removeFriend(friendName);// Remove friend from the list
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure to delete friend
                Log.e("FriendsAdapter", "Failed to delete friend: " + errorMessage);
            }
        });
    }

    private void removeFriend(String friendName) {
        // Remove friend from the list
        friendsList.remove(friendName);
        notifyDataSetChanged(); // Notify adapter of data change
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        // You might want to notify the server about this change if necessary
        // For example: send a request to the server to update the friend list
    }
}
