package il.ac.biu.project.foobar.viewmodels;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import il.ac.biu.project.foobar.api.users.DeleteUserAPI;
import il.ac.biu.project.foobar.api.users.EditUserDetailsAPI;
import il.ac.biu.project.foobar.api.users.UsersAPI;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.entities.responses.UserDetailsResponse;


public class UserViewModel extends ViewModel {
    private final MutableLiveData<UserDetailsResponse> userDetailsFetchSuccess = new MutableLiveData<>();

    private final MutableLiveData<Boolean> userDetailEditSuccess = new MutableLiveData<>();

    private final UsersAPI usersAPI = new UsersAPI();
     private final DeleteUserAPI deleteUserApi = new DeleteUserAPI();

    private final EditUserDetailsAPI editUserDetailsApi = new EditUserDetailsAPI();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<Boolean> userDeletionSuccess = new MutableLiveData<>();


    public LiveData<UserDetailsResponse> getUserDetailsFetchSuccess() {
        return userDetailsFetchSuccess;
    }

    public LiveData<Boolean> getUserDetailEditSuccess() {
        return userDetailEditSuccess;
    }
    public LiveData<Boolean> getUserDeleteSuccess() {
        return userDeletionSuccess;
    }



    // Method to fetch user details
    public void fetchUserDetails(String userId, String jwtToken) {
        executor.execute(() -> {
            usersAPI.getUserDetails(userId, jwtToken, new UsersAPI.UserDetailsResponseCallback() {
                @Override
                public void onSuccess(UserDetailsResponse userDetailsResponse) {
                    // Indicate success
                    userDetailsFetchSuccess.postValue(userDetailsResponse);
                }

                @Override
                public void onFailure(String errorMessage) {
                    // Indicate failure
                    userDetailsFetchSuccess.postValue(null);
                }
            });
        });
    }

    public void deleteUser(String userId, String jwtToken) {
        executor.execute(() -> {
            deleteUserApi.deleteUser(userId, jwtToken, new DeleteUserAPI.UserDeleteResponseCallback() {

                @Override
                public void onSuccess() {
                    userDeletionSuccess.postValue(true);
                }

                @Override
                public void onFailure(String errorMessage) {
                    userDeletionSuccess.postValue(false);
                }
            });
        });
    }

    public void editUserDetails(String userId, String jwtToken, String displayName, Bitmap profilePic) {
        executor.execute(() -> {
            editUserDetailsApi.editUserDetails(userId, jwtToken, displayName, profilePic, new EditUserDetailsAPI.UserEditResponseCallback() {
                @Override
                public void onSuccess() {
                    userDetailEditSuccess.postValue(true);
                }

                @Override
                public void onFailure(String errorMessage) {
                    userDetailEditSuccess.postValue(false);
                }
            });
        });
    }


}
