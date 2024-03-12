package il.ac.biu.project.foobar.api.users;

import static il.ac.biu.project.foobar.utils.images.base64ToBitmap;

import android.util.Log;

import il.ac.biu.project.foobar.MyApplication;
import il.ac.biu.project.foobar.R;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.entities.responses.UserDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersAPI {
    private Retrofit retrofit;
    private UsersWebServiceAPI usersWebServiceAPI;

    public UsersAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        usersWebServiceAPI = retrofit.create(UsersWebServiceAPI.class);
    }

    /**
     * Fetch the user details from the server.
     *
     * @param userID The ID of the user whose details are to be fetched.
     * @param jwtToken The JWT token for authorization.
     * @param callback The callback to handle the response.
     */
    public void getUserDetails(String userID, String jwtToken, UserDetailsResponseCallback callback) {
        Call<UserDetailsResponse> call = usersWebServiceAPI.getUserDetails(userID, jwtToken);
        call.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                if (response.isSuccessful()) {
                    // Notify callback about success
                    UserDetailsResponse userDetailsResponse = response.body();
                    callback.onSuccess(userDetailsResponse);
                } else {
                    callback.onFailure("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                callback.onFailure("Failure: " + t.getMessage());
            }
        });
    }

    public interface UserDetailsResponseCallback {
        void onSuccess(UserDetailsResponse userDetailsResponse);
        void onFailure(String errorMessage);
    }
}
