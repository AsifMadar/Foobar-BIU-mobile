package il.ac.biu.project.foobar.api.friends;

import il.ac.biu.project.foobar.MyApplication;
import il.ac.biu.project.foobar.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApproveFriendRequestAPI {

    private Retrofit retrofit;
    private FriendsWebServiceAPI friendsWebServiceAPI;

    public ApproveFriendRequestAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        friendsWebServiceAPI = retrofit.create(FriendsWebServiceAPI.class);
    }

    public void approveFriendRequest(String userId, String friendId, String jwtToken, ApproveFriendRequestCallback callback) {
        Call<Void> call = friendsWebServiceAPI.approveFriendRequest(userId, friendId, jwtToken);

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

    public interface ApproveFriendRequestCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
