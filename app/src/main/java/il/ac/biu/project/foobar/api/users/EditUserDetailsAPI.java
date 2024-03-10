package il.ac.biu.project.foobar.api.users;


import android.graphics.Bitmap;

import il.ac.biu.project.foobar.MyApplication;
import il.ac.biu.project.foobar.R;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.entities.requests.UpdateUserRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditUserDetailsAPI {
    private Retrofit retrofit;
    private UsersWebServiceAPI usersWebServiceAPI;

    public EditUserDetailsAPI() {
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
    public void editUserDetails(String userID, String jwtToken, String displayName, Bitmap profilePic,
                               UserEditResponseCallback callback) {
        Call<Void> call = usersWebServiceAPI.editUserDetail(userID, jwtToken,
                new UpdateUserRequest(displayName, profilePic));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    UserDetails userDetails = UserDetails.getInstance();
                    if(profilePic != null)
                        userDetails.setImg(profilePic);

                    if (displayName != null)
                        userDetails.setDisplayName(displayName);

                    callback.onSuccess();
                } else {
                    callback.onFailure("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Failure: " + t.getMessage());
            }
        });
    }

    public interface UserEditResponseCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}

