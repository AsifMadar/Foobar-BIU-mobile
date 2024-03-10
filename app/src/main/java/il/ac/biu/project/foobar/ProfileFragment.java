package il.ac.biu.project.foobar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private RecyclerView friendsRecyclerView;
    private ArrayList<String> friendsList;
    private FriendsAdapter friendsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize RecyclerView for friends
        friendsRecyclerView = view.findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Get the list of friends from UserDetails
        friendsList = UserDetails.getInstance().getFriends();
        if (friendsList == null) {
            friendsList = new ArrayList<>(); // Initialize if null
        }

        // Initialize and set up the adapter for friends RecyclerView
        friendsAdapter = new FriendsAdapter(friendsList);
        friendsRecyclerView.setAdapter(friendsAdapter);

        // Display the number of friends
        TextView friendsTitle = view.findViewById(R.id.friendsTitle);
        friendsTitle.setText("Friends (" + friendsList.size() + ")");

        TextView addFriendTextView = view.findViewById(R.id.addFriend);
        addFriendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle adding friend action
                // Here, you can add the friend request to the user
                String friendName = "Dummy Friend"; // You can replace this with actual friend data
                UserDetails.getInstance().addFriendRequest(friendName);

                // Optionally, you can display a message or update UI indicating the friend request is sent
                // For example, Toast.makeText(getContext(), "Friend request sent to " + friendName, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
