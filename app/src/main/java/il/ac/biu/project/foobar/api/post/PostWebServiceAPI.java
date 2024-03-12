package il.ac.biu.project.foobar.api.post;

import java.util.List;

import il.ac.biu.project.foobar.entities.PostJsonDetails;
import il.ac.biu.project.foobar.entities.requests.AddPostRequest;
import il.ac.biu.project.foobar.entities.requests.LikeRequest;
import il.ac.biu.project.foobar.entities.responses.PostResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import il.ac.biu.project.foobar.entities.PostDetails;

public interface PostWebServiceAPI {
    @GET("/api/posts")
    Call<List<PostJsonDetails>> getPosts(@Header("Authorization") String jwtToken);
    @POST("/api/users/{userId}/posts")
    Call<PostJsonDetails> createPost(@Path("userId") String userId,
                                     @Header("Authorization") String jwtToken, @Body AddPostRequest postRequest);

    @DELETE("/api/users/{id}/posts/{pid}")
    Call<Void> deletePost(@Path("id") String userId,
                          @Path("pid") String postId,
                          @Header("Authorization") String jwtToken);

    @PATCH("/api/users/{id}/posts/{pid}")
    Call<PostJsonDetails> editPost(@Path("id") String userId,
                                   @Path("pid") String postId,
                                   @Header("Authorization") String jwtToken,
                                   @Body AddPostRequest postRequest);

    @POST("/api/likes")
    Call<Void> addLike(@Body LikeRequest body,
                       @Header("Authorization") String jwtToken);

    @GET("/api/users/:id/posts")
            Call<List<PostJsonDetails>> getUserPosts(@Path("id") String userId,
                                                     @Header("Authorization") String jwtToken);

}


