package il.ac.biu.project.foobar.viewmodels;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import il.ac.biu.project.foobar.MainActivity;
import il.ac.biu.project.foobar.api.SignInAPI;
import il.ac.biu.project.foobar.entities.SignInRequest;
import il.ac.biu.project.foobar.entities.UserDetails;

public class SignInViewModel extends  ViewModel{
    private final MutableLiveData<Boolean> signInSuccess = new MutableLiveData<>();
    private final SignInAPI signInAPI = new SignInAPI();
    private final UserDetails userDetails = UserDetails.getInstance();



    public LiveData<Boolean> getSignInSuccess() {
        return signInSuccess;
    }

    //check if the username and password are correct, sign in if true
    public void signIn(String username, String password) {
        SignInRequest signInRequest = new SignInRequest(username, password);
        signInAPI.signInToServer(signInRequest, new SignInAPI.SignInResponseCallback() {
            @Override
            public void onSuccess(String jwtToken) {
                userDetails.setJwt(jwtToken);
                    userDetails.setSignIn(true); // Update sign-in status.
                signInSuccess.setValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                signInSuccess.setValue(false);
            }
        });
    }
}
