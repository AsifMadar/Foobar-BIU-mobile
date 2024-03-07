package il.ac.biu.project.foobar.api.signup;

import android.util.Log;

import il.ac.biu.project.foobar.entities.requests.SignUpRequest;
import retrofit2.Callback;
import il.ac.biu.project.foobar.MyApplication;
import il.ac.biu.project.foobar.R;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpAPI {

    private Retrofit retrofit;
    private SignUpWebServiceAPI signUpWebServiceAPI;


    public SignUpAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        signUpWebServiceAPI = retrofit.create(SignUpWebServiceAPI.class);
    }



    /**
     * Sign the user in, using the server
     *
     * @param signUpRequest The unique identifiers for the sign in.
     */
    public void signUpToServer(SignUpRequest signUpRequest, SignUpAPI.SignUpResponseCallback callback) {
        Call<Void> call = signUpWebServiceAPI.signUp(signUpRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("SignUpAPI", "Response code: " + response.code()); // This will print the response code
                if (response.isSuccessful()) {

                    // Notify callback about success
                    callback.onSuccess();
                } else {
                    // Handle error response, e.g., unauthorized or bad request
                    callback.onFailure("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("SignUpAPI", "Response code: " + t.getMessage()); // This will print the response code
                // Notify callback about failure
                callback.onFailure("Failure: " + t.getMessage());
            }
        });
    }

    public interface SignUpResponseCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
