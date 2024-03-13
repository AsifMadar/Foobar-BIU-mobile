package il.ac.biu.project.foobar.viewmodels;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

import il.ac.biu.project.foobar.api.friends.GetFriendsAPI;
import il.ac.biu.project.foobar.api.friends.SendFriendRequestAPI;
import il.ac.biu.project.foobar.entities.UserDetails;

public class FriendsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<String>> friendsLiveData;
    private GetFriendsAPI getFriendsAPI;
    private SendFriendRequestAPI sendFriendRequestAPI;


    public FriendsViewModel() {
        friendsLiveData = new MutableLiveData<>();
        getFriendsAPI = new GetFriendsAPI();

    }

    public LiveData<ArrayList<String>> getFriendsLiveData() {
        return friendsLiveData;
    }

    public void loadFriends(String userId, String jwtToken) {
        getFriendsAPI.getFriends(userId, jwtToken, new GetFriendsAPI.FriendsResponseCallback() {

            // Load friends from UserDetails (assuming it's already populated)
            @Override
            public void onSuccess(ArrayList<String> friends) {
                // Ensure UI updates are done on the main thread
                friendsLiveData.setValue(friends); // Update LiveData with the list of friends

            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure, if needed
            }
        });
    }
}
