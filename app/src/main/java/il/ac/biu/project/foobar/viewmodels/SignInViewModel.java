package il.ac.biu.project.foobar.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import il.ac.biu.project.foobar.api.signin.SignInAPI;
import il.ac.biu.project.foobar.entities.requests.SignInRequest;
import il.ac.biu.project.foobar.entities.UserDetails;

public class SignInViewModel extends  ViewModel{
    private final MutableLiveData<Boolean> signInSuccess = new MutableLiveData<>();
    private final SignInAPI signInAPI = new SignInAPI();
    private final UserDetails userDetails = UserDetails.getInstance();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();




    public LiveData<Boolean> getSignInSuccess() {
        return signInSuccess;
    }

    //check if the username and password are correct, sign in if true
    public void signIn(String username, String password) {
        executor.execute(() -> {
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
        });
    }
}
