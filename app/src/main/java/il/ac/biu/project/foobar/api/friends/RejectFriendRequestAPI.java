package il.ac.biu.project.foobar.api.friends;

import il.ac.biu.project.foobar.MyApplication;
import il.ac.biu.project.foobar.R;
import il.ac.biu.project.foobar.api.friends.FriendsWebServiceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RejectFriendRequestAPI {

    private Retrofit retrofit;
    private FriendsWebServiceAPI friendsWebServiceAPI;

    public RejectFriendRequestAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        friendsWebServiceAPI = retrofit.create(FriendsWebServiceAPI.class);
    }

    public void rejectFriendRequestOrDeleteFriend(String userId, String friendId, String jwtToken, RejectFriendRequestCallback callback) {
        Call<Void> call = friendsWebServiceAPI.rejectFriendshipOrDelete(userId, friendId, jwtToken);

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

    public interface RejectFriendRequestCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
