package il.ac.biu.project.foobar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import il.ac.biu.project.foobar.api.friends.ApproveFriendRequestAPI;
import il.ac.biu.project.foobar.api.friends.RejectFriendRequestAPI;
import il.ac.biu.project.foobar.entities.UserDetails;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.ViewHolder> {

    private ArrayList<String> friendRequests;
    private static final int REQUEST_CODE_FRIEND_REQUESTS = 1;
    private Context context;

    public FriendRequestsAdapter(ArrayList<String> friendRequests, Context context) {
        this.friendRequests = friendRequests != null ? friendRequests : new ArrayList<>();
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String friendRequest = friendRequests.get(position);
        holder.friendRequestTextView.setText(friendRequest);

        // Add Friend button click listener
        holder.addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendName = friendRequests.get(holder.getAdapterPosition());

                // Call the API to approve the friend request and add the friend
                ApproveFriendRequestAPI approveFriendRequestAPI = new ApproveFriendRequestAPI();
                approveFriendRequestAPI.approveFriendRequest(UserDetails.getInstance().getUsername(), friendName, UserDetails.getInstance().getJwt(), new ApproveFriendRequestAPI.ApproveFriendRequestCallback() {
                    @Override
                    public void onSuccess() {
                        // Handle successful approval
                        // Remove friend request from list
                        friendRequests.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();

                        // Add friend to friend list
                        UserDetails.getInstance().addFriend(friendName);

                        // Print the name of the friend added
                        Log.d("FriendRequestsAdapter", "Friend added: " + friendName);

                        // Pass back the updated friend list to the FriendsFragment
                        Intent resultIntent = new Intent();
                        resultIntent.putStringArrayListExtra("updatedFriendsList", UserDetails.getInstance().getFriends());
                        ((FriendRequests) context).setResult(REQUEST_CODE_FRIEND_REQUESTS, resultIntent);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Handle failure to approve
                        Log.e("FriendRequestsAdapter", "Failed to approve friend request: " + errorMessage);
                    }
                });
            }
        });

        // Reject Friend button click listener
        holder.rejectFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendName = friendRequests.get(holder.getAdapterPosition());

                // Call the API to reject the friend request or delete the friend
                RejectFriendRequestAPI rejectFriendRequestAPI = new RejectFriendRequestAPI();
                rejectFriendRequestAPI.rejectFriendRequestOrDeleteFriend(UserDetails.getInstance().getUsername(), friendName, UserDetails.getInstance().getJwt(), new RejectFriendRequestAPI.RejectFriendRequestCallback() {
                    @Override
                    public void onSuccess() {
                        // Handle successful rejection or deletion
                        // Remove friend request from list
                        friendRequests.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Handle failure to reject or delete
                        Log.e("FriendRequestsAdapter", "Failed to reject friend request or delete friend: " + errorMessage);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView friendRequestTextView;
        Button addFriendButton;
        Button rejectFriendButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendRequestTextView = itemView.findViewById(R.id.friendRequestTextView);
            addFriendButton = itemView.findViewById(R.id.addFriendButton);
            rejectFriendButton = itemView.findViewById(R.id.rejectFriendButton);
        }
    }
}
