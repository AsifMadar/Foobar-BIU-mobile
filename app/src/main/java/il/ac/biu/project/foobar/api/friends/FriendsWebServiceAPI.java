package il.ac.biu.project.foobar.api.friends;

import java.util.ArrayList;

import il.ac.biu.project.foobar.entities.requests.AddFriendRequest;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface FriendsWebServiceAPI {

    @GET("/api/users/{id}/friends")
    Call<ArrayList<String>> getFriends(@Path("id") String userId, @Header("Authorization") String jwtToken);

    @POST("/api/users/{id}/friends")
    Call<Void> sendFriendRequest(@Path("id") String friendId, @Header("Authorization") String jwtToken);

    @PATCH("/api/users/{id}/friends/{fid}")
    Call<Void> approveFriendRequest(@Path("id") String userId, @Path("fid") String friendId, @Header("Authorization") String jwtToken);

    @DELETE("/api/users/{id}/friends/{fid}")
    Call<Void> rejectFriendshipOrDelete(@Path("id") String userId, @Path("fid") String friendId, @Header("Authorization") String jwtToken);
}
