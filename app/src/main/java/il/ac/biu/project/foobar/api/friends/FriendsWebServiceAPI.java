package il.ac.biu.project.foobar.api.friends;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface FriendsWebServiceAPI {

    @GET("/api/users/{id}/friends")
    Call<ArrayList<String>> getFriends(@Path("id") String userId);

    @POST("users/{id}/friends")
    Call<Void> addFriendRequest(@Path("id") String userId, @Body String friendId);

    @PATCH("users/{userId}/friends/{friendId}")
    Call<Void> approveFriendRequest(@Path("userId") String userId, @Path("friendId") String friendId);

    @DELETE("users/{userId}/friends/{friendId}")
    Call<Void> removeFriend(@Path("userId") String userId, @Path("friendId") String friendId);
}
