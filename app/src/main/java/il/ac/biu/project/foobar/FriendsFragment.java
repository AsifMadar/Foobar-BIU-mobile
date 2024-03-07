package il.ac.biu.project.foobar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import il.ac.biu.project.foobar.FriendsAdapter;
import il.ac.biu.project.foobar.UserDetails;

public class FriendsFragment extends Fragment {

    private RecyclerView friendsRecyclerView;
    private ArrayList<String> friendsList;
    private FriendsAdapter friendsAdapter;

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
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check if there are new friends added and update the UI
        ArrayList<String> updatedFriendsList = UserDetails.getInstance().getFriends();
        if (updatedFriendsList != null) {
            friendsList.clear(); // Clear the current list
            friendsList.addAll(updatedFriendsList); // Update with the new list
            friendsAdapter.notifyDataSetChanged(); // Notify adapter of data change
        }
    }
}
