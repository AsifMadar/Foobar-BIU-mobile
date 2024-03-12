package il.ac.biu.project.foobar.api.users;

import il.ac.biu.project.foobar.api.users.UsersWebServiceAPI;
import il.ac.biu.project.foobar.entities.UserDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import il.ac.biu.project.foobar.MyApplication;
import il.ac.biu.project.foobar.R;

public class DeleteUserAPI {
    private Retrofit retrofit;
    private UsersWebServiceAPI usersWebServiceAPI;

    public DeleteUserAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        usersWebServiceAPI = retrofit.create(UsersWebServiceAPI.class);
    }

    /**
     * Delete the user account from the server.
     *
     * @param userID The ID of the user whose account is to be deleted.
     * @param jwtToken The JWT token for authorization.
     * @param callback The callback to handle the response.
     */
    public void deleteUser(String userID, String jwtToken, UserDeleteResponseCallback callback) {
        Call<Void> call = usersWebServiceAPI.deleteUserDetail(userID, jwtToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
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

    public interface UserDeleteResponseCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
