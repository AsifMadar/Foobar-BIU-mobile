package il.ac.biu.project.foobar.api.post;

import java.util.List;

import il.ac.biu.project.foobar.entities.PostJsonDetails;
import il.ac.biu.project.foobar.entities.requests.AddPostRequest;
import il.ac.biu.project.foobar.entities.responses.PostResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import il.ac.biu.project.foobar.entities.PostDetails;

public interface PostWebServiceAPI {
    @GET("posts")
    Call<List<PostDetails>> getPosts();

    @POST("{userId}/posts")
    Call<PostJsonDetails> createPost(@Path("userId") String userId,
                                     @Header("Authorization") String jwtToken, @Body AddPostRequest postRequest);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);
}
