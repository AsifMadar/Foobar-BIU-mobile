package il.ac.biu.project.foobar.api.friends;

import java.util.ArrayList;

import il.ac.biu.project.foobar.MyApplication;
import il.ac.biu.project.foobar.R;
import il.ac.biu.project.foobar.entities.requests.AddFriendRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetFriendsAPI {
    private Retrofit retrofit;
    private FriendsWebServiceAPI friendsWebServiceAPI;

    public GetFriendsAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        friendsWebServiceAPI = retrofit.create(FriendsWebServiceAPI.class);
    }

    // Method to get friends for the friend with the provided ID
    public void getFriends(String friendId, String jwtToken, FriendsResponseCallback callback) {
        Call<ArrayList<String>> call = friendsWebServiceAPI.getFriends(friendId, "Bearer " + jwtToken);

        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                callback.onFailure("Failure: " + t.getMessage());
            }
        });
    }

    public interface FriendsResponseCallback {
        void onSuccess(ArrayList<String> friends);
        void onFailure(String errorMessage);
    }
}
