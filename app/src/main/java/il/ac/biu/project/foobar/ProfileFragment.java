package il.ac.biu.project.foobar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import il.ac.biu.project.foobar.adapters.FriendsAdapter;
import il.ac.biu.project.foobar.adapters.PostsListAdapter;
import il.ac.biu.project.foobar.api.friends.SendFriendRequestAPI;
import il.ac.biu.project.foobar.entities.AddLikePostListener;
import il.ac.biu.project.foobar.entities.GoToProfileListener;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.PostRemoveListener;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.entities.responses.UserDetailsResponse;
import il.ac.biu.project.foobar.utils.images;
import il.ac.biu.project.foobar.viewmodels.FriendsViewModel;
import il.ac.biu.project.foobar.viewmodels.PostsViewModel;
import il.ac.biu.project.foobar.viewmodels.UserViewModel;

public class ProfileFragment extends Fragment {
    private SendFriendRequestAPI sendFriendRequestAPI;

    private RecyclerView friendsRecyclerView;
    private FriendsAdapter friendsAdapter;
    private FriendsViewModel friendsViewModel;
    private UserViewModel userViewModel;
    private PostsListAdapter postsListAdapter;
    PostsViewModel postsViewModel;
    private RecyclerView layout;

    UserDetailsResponse profileUser = null;
    String userID;
    int friendsNum=0;

    public ProfileFragment(PostsViewModel postsViewModel, String userID) {
        this.postsViewModel = postsViewModel;
        this.userID = userID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setUserAndPostsViewModel(userID, view);

        return view;
    }

    private void setFriendsAndSendFriendViewModel(View view) {
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
        friendsViewModel.loadFriends(userID, jwtToken);

        TextView addFriendTextView = view.findViewById(R.id.addFriend);
        addFriendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle adding friend action
                // Send friend request
                sendFriendRequest(jwtToken);
                addFriendTextView.setText(R.string.friend_request_sent);
            }
        });
    }

    private void setUserAndPostsViewModel(String userID, View view) {
        layout = view.findViewById(R.id.profilePostsRecyclerView);
        postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);
        postsListAdapter = new PostsListAdapter(getActivity(), getContext(), new PostRemoveListener() {
            @Override
            public void onDeletePost(PostDetails postDetails) {
                postsViewModel.delete(postDetails);
            }
        }, new AddLikePostListener() {
            @Override
            public void onLikePost(PostDetails postDetails) {
                postsViewModel.addLike(postDetails);
            }
        }, new GoToProfileListener() {
            @Override
            public void goToProfile(String userID) {
            }
        });
        layout.setAdapter(postsListAdapter);
        layout.setLayoutManager(new LinearLayoutManager(getContext()));
        userViewModel =new ViewModelProvider(this).get(UserViewModel.class);

        postsViewModel.getProfilePosts().observe(getViewLifecycleOwner(), new Observer<List<PostDetails>>() {
            @Override
            public void onChanged(List<PostDetails> postsList) {
                postsListAdapter.setPosts(postsList);
            }
        });

        userViewModel.fetchUserDetails(userID, UserDetails.getInstance().getJwt());
        userViewModel.getUserDetailsFetchSuccess().observe(getViewLifecycleOwner(), new Observer<UserDetailsResponse>() {
            @Override
            public void onChanged(UserDetailsResponse userDetailsResponse) {
                if (userDetailsResponse != null) {
                    profileUser = userDetailsResponse;
                    setProfileUserUI(view);
                    setFriendsAndSendFriendViewModel(view);        // Initialize RecyclerView for friends

                } else {
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
         postsViewModel.reloadUserPosts(userID);
    }

    private void setProfileUserUI(View view) {
        TextView textView = view.findViewById(R.id.tv_username);
        textView.setText(profileUser.getDisplayName());
        ImageView imageView = view.findViewById(R.id.image_profile);
        imageView.setImageBitmap(images.base64ToBitmap(profileUser.getProfileImage()));
        UserDetails userDetails = UserDetails.getInstance();
        TextView friendsText = view.findViewById(R.id.friendsTitle);
        TextView postsText = view.findViewById(R.id.postsTitle);
        TextView addFriend = view.findViewById(R.id.addFriend);

        if (profileUser.getFriends().contains(userDetails.getUsername())
                || profileUser.getUsername().equals((userDetails.getUsername()))) {
            view.findViewById(R.id.addFriend).setVisibility(View.GONE);
            friendsText.setVisibility(View.VISIBLE);
            postsText.setVisibility(View.VISIBLE);
            addFriend.setText(R.string.friend_request_sent);

        } else {
            view.findViewById(R.id.addFriend).setVisibility(View.VISIBLE);
            friendsText.setVisibility(View.GONE);
            postsText.setVisibility(View.GONE);
            addFriend.setText(R.string.add_friend);
        }


    }

    private void sendFriendRequest(String jwtToken) {
        String friendId = profileUser.getUsername();

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
        friendsNum= friends.size();
    }
    private void updateFriendsCount(int count) {
        TextView friendsTitle = getView().findViewById(R.id.friendsTitle);
    }
}
