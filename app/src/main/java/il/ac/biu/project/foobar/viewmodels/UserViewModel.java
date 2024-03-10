package il.ac.biu.project.foobar.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import il.ac.biu.project.foobar.api.users.UsersAPI;
import il.ac.biu.project.foobar.entities.UserDetails;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<Boolean> userDetailsFetchSuccess = new MutableLiveData<>();
    private final UsersAPI usersAPI = new UsersAPI();

    // Assuming UserDetails is a singleton
    private final UserDetails userDetails = UserDetails.getInstance();

    public LiveData<Boolean> getUserDetailsFetchSuccess() {
        return userDetailsFetchSuccess;
    }


    // Method to fetch user details
    public void fetchUserDetails(String userId, String jwtToken) {
        usersAPI.getUserDetails(userId, jwtToken, new UsersAPI.UserDetailsResponseCallback() {
            @Override
            public void onSuccess(UserDetails userDetailsResponse) {
                // Indicate success
                userDetailsFetchSuccess.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Indicate failure
                userDetailsFetchSuccess.postValue(false);
            }
        });
    }
}
