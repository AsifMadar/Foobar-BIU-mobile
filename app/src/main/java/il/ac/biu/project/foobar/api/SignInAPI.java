package il.ac.biu.project.foobar.api;


import retrofit2.Callback;
import il.ac.biu.project.foobar.MyApplication;
import il.ac.biu.project.foobar.R;
import il.ac.biu.project.foobar.entities.SignInRequest;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInAPI {
    private Retrofit retrofit;
    private SignInWebServiceAPI signInWebServiceAPI;

    private String responseBody = null;


    public SignInAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        signInWebServiceAPI = retrofit.create(SignInWebServiceAPI.class);
    }



    /**
     * Sign the user in, using the server
     *
     * @param signInRequest The unique identifiers for the sign in.
     */
    public void signInToServer(SignInRequest signInRequest, SignInResponseCallback callback) {
        Call<String> call = signInWebServiceAPI.signIn(signInRequest);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Notify callback about success
                    callback.onSuccess(response.body());
                } else {
                    // Handle error response, e.g., unauthorized or bad request
                    callback.onFailure("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Notify callback about failure
                callback.onFailure("Failure: " + t.getMessage());
            }
        });
    }

    public interface SignInResponseCallback {
        void onSuccess(String jwtToken);
        void onFailure(String errorMessage);
    }

}

