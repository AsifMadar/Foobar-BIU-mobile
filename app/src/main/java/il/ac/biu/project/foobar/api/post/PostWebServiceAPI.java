package il.ac.biu.project.foobar.api.post;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import il.ac.biu.project.foobar.entities.PostDetails;

public interface PostWebServiceAPI {
    @GET("posts")
    Call<List<PostDetails>> getPosts();

    @POST("posts")
    Call<Void> createPost(@Body PostDetails post);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);
}
