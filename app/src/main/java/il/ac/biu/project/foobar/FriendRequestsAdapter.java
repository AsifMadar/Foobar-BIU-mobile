package il.ac.biu.project.foobar;

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

import il.ac.biu.project.foobar.entities.UserDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.ViewHolder> {

    private ArrayList<String> friendRequests;
    private static final int REQUEST_CODE_FRIEND_REQUESTS = 1;

    public FriendRequestsAdapter(ArrayList<String> friendRequests) {
        this.friendRequests = friendRequests;
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

                    // Handle adding friend action
                String friendName = friendRequests.remove(holder.getAdapterPosition());
                notifyDataSetChanged(); // Notify adapter of data change

                // Add friend to friend list
                UserDetails.getInstance().addFriend(friendName);

                // Print the name of the friend added
                Log.d("FriendRequestsAdapter", "Friend added: " + friendName);

                // Pass back the updated friend list to the FriendsFragment
                Intent resultIntent = new Intent();
                resultIntent.putStringArrayListExtra("updatedFriendsList", UserDetails.getInstance().getFriends());
                ((FriendRequests) holder.itemView.getContext()).setResult(REQUEST_CODE_FRIEND_REQUESTS, resultIntent);
            }
        });

        // Reject Friend button click listener
        holder.rejectFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle rejecting friend action
                friendRequests.remove(holder.getAdapterPosition());
                notifyDataSetChanged(); // Notify adapter of data change
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
