package il.ac.biu.project.foobar.api.post;

import androidx.annotation.NonNull;

import il.ac.biu.project.foobar.MyApplication;

import il.ac.biu.project.foobar.R;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.PostJsonDetails;
import il.ac.biu.project.foobar.entities.PostManager;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.entities.requests.AddPostRequest;
import il.ac.biu.project.foobar.entities.responses.PostResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddPostAPI {

    private Retrofit retrofit;
    private PostWebServiceAPI postWebServiceAPI;

    public AddPostAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl) + "users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        postWebServiceAPI = retrofit.create(PostWebServiceAPI.class);
    }

    /**
     * Create a new post, using the server
     *
     * @param postDetails The post details.
     * @param jwtToken The JWT token for authorization.
     * @param callback The callback to handle the response.
     */
    public void addPost(PostDetails postDetails, String jwtToken, AddPostResponseCallback callback) {
        AddPostRequest addPostRequest = new AddPostRequest(postDetails.getUserInput(), postDetails.getPicture());
        // Ensure the PostWebServiceAPI interface's createPost method accepts an AddPostRequest and includes a way to set the Authorization header
        Call<PostJsonDetails> call = postWebServiceAPI.createPost(UserDetails.getInstance().getUsername(),
                "Bearer " + jwtToken, addPostRequest);

        call.enqueue(new Callback<PostJsonDetails>() {
            @Override
            public void onResponse(@NonNull Call<PostJsonDetails> call, @NonNull Response<PostJsonDetails> response) {
                new Thread(() ->{
                    if (response.isSuccessful() && response.body() != null) {

                        // update post manager and postID
                    postDetails.setId(response.body().id);
                    callback.onSuccess(postDetails);
                } else {
                    // Handle error response, e.g., unauthorized or bad request
                    callback.onFailure("Error: " + response.code());
                }
            }).start();
        }


            @Override
            public void onFailure(@NonNull Call<PostJsonDetails> call, @NonNull Throwable t) {
                // Notify callback about failure to communicate with the server
                callback.onFailure("Failure: " + t.getMessage());
            }
        });
    }

    public interface AddPostResponseCallback {
        void onSuccess(PostDetails addedPost);
        void onFailure(String errorMessage);
    }

}
