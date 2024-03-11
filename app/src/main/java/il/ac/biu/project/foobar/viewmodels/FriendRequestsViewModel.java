package il.ac.biu.project.foobar.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import il.ac.biu.project.foobar.api.friends.ApproveFriendRequestAPI;

public class FriendRequestsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<String>> friendRequestsLiveData;
    private ApproveFriendRequestAPI approveFriendRequestAPI;

    public FriendRequestsViewModel() {
        friendRequestsLiveData = new MutableLiveData<>();
        approveFriendRequestAPI = new ApproveFriendRequestAPI();
    }

    // Method to approve a friend request
    public void approveFriendRequest(String userId, String friendId, String jwtToken) {
        approveFriendRequestAPI.approveFriendRequest(userId, friendId, jwtToken, new ApproveFriendRequestAPI.ApproveFriendRequestCallback() {
            @Override
            public void onSuccess() {
                // Handle successful approval
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure to approve
            }
        });
    }

    // Method to reject a friend request
    public void rejectFriendRequest(String userId, String friendId, String jwtToken) {
        // Implement rejectFriendRequest method if needed
    }

    // Getter method for friend requests LiveData
    public LiveData<ArrayList<String>> getFriendRequestsLiveData() {
        return friendRequestsLiveData;
    }
}
