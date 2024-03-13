package il.ac.biu.project.foobar.viewmodels;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import il.ac.biu.project.foobar.api.signup.SignUpAPI;
import il.ac.biu.project.foobar.entities.requests.SignUpRequest;


public class SignUpViewModel extends ViewModel{
    private final MutableLiveData<Boolean> signUpSuccess = new MutableLiveData<>();
    private final SignUpAPI signUpAPI = new SignUpAPI();



    public LiveData<Boolean> getSignUpSuccess() {
        return signUpSuccess;
    }

    public void signUp(String username, String password, String displayName, Bitmap img) {
        SignUpRequest signUpRequest = new SignUpRequest(username, password, displayName, img);
        signUpAPI.signUpToServer(signUpRequest, new SignUpAPI.SignUpResponseCallback() {
            @Override

            public void onSuccess() {
                signUpSuccess.setValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                signUpSuccess.setValue(false);
            }
        });
    }
}
