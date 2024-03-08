package il.ac.biu.project.foobar.api.friends;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FriendsWebServiceAPI {
    @GET("users/{id}/friends")
    Call<ArrayList<String>> getFriends(@Path("id") String userId);

    @POST("users/{id}/friends/add")
    Call<Void> addFriend(@Path("id") String userId, @Body String friendId);

    @POST("users/{id}/friends/remove")
    Call<Void> removeFriend(@Path("id") String userId, @Body String friendId);
}
