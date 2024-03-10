package il.ac.biu.project.foobar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FriendsFragment extends Fragment {

    private RecyclerView friendsRecyclerView;
    private ArrayList<String> friendsList;
    private FriendsAdapter friendsAdapter;
    private static final int REQUEST_CODE_FRIEND_REQUESTS = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        // Initialize RecyclerView
        friendsRecyclerView = view.findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Get the friend list from UserDetails
        friendsList = UserDetails.getInstance().getFriends();
        if (friendsList == null) {
            friendsList = new ArrayList<>(); // Initialize if null
        }

        // Initialize adapter
        friendsAdapter = new FriendsAdapter(friendsList);
        friendsRecyclerView.setAdapter(friendsAdapter);

        // Find the button by ID
        Button friendRequestsButton = view.findViewById(R.id.friendRequestsButton);

        // Set OnClickListener to navigate to FriendRequestsActivity
        friendRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to FriendRequestsActivity
                Intent intent = new Intent(getActivity(), FriendRequests.class);
                startActivityForResult(intent, REQUEST_CODE_FRIEND_REQUESTS);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FRIEND_REQUESTS && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra("updatedFriendsList")) {
                ArrayList<String> updatedFriendsList = data.getStringArrayListExtra("updatedFriendsList");
                if (updatedFriendsList != null) {
                    friendsList.clear(); // Clear the current list
                    friendsList.addAll(updatedFriendsList); // Update with the new list
                    friendsAdapter.notifyDataSetChanged(); // Notify adapter of data change
                }
            }
        }
    }
}
