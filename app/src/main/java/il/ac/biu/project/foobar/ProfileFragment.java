package il.ac.biu.project.foobar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import il.ac.biu.project.foobar.FriendsAdapter;
import il.ac.biu.project.foobar.R;
import il.ac.biu.project.foobar.api.friends.SendFriendRequestAPI;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.viewmodels.FriendsViewModel;

public class ProfileFragment extends Fragment {
    private SendFriendRequestAPI sendFriendRequestAPI;

    private RecyclerView friendsRecyclerView;
    private FriendsAdapter friendsAdapter;
    private FriendsViewModel friendsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize RecyclerView for friends
        friendsRecyclerView = view.findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Initialize ViewModel
        friendsViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);

        // Initialize SendFriendRequestAPI
        sendFriendRequestAPI = new SendFriendRequestAPI(); // Initialize here

        // Observe friends LiveData for UI updates
        friendsViewModel.getFriendsLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> friends) {
                // Update UI with the new list of friends
                if (friends != null) {
                    updateFriendsList(friends);
                }
            }
        });

        // Load friends
        String jwtToken = UserDetails.getInstance().getJwt(); // Retrieve JWT token from the appropriate source
        friendsViewModel.loadFriends(UserDetails.getInstance().getUsername(), jwtToken);

        TextView addFriendTextView = view.findViewById(R.id.addFriend);
        addFriendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle adding friend action
                // Send friend request
                sendFriendRequest(jwtToken);
            }
        });

        return view;
    }

    private void sendFriendRequest(String jwtToken) {
        String friendId = "user1";

        sendFriendRequestAPI.sendFriendRequest(friendId, jwtToken, new SendFriendRequestAPI.SendFriendRequestCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Friend request sent successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure to send friend request
            }
        });
    }

    private void updateFriendsList(ArrayList<String> friends) {
        friendsAdapter = new FriendsAdapter(friends);
        friendsRecyclerView.setAdapter(friendsAdapter);
        TextView friendsTitle = getView().findViewById(R.id.friendsTitle);
        friendsTitle.setText("Friends (" + friends.size() + ")");
    }
}
