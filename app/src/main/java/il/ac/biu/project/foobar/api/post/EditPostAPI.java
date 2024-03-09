package il.ac.biu.project.foobar.api.post;

import androidx.annotation.NonNull;

import il.ac.biu.project.foobar.MyApplication;
import il.ac.biu.project.foobar.R;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.PostJsonDetails;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.entities.requests.AddPostRequest; // You might need to create or use an appropriate request class for editing
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditPostAPI {

    private Retrofit retrofit;
    private PostWebServiceAPI postWebServiceAPI;

    public EditPostAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        postWebServiceAPI = retrofit.create(PostWebServiceAPI.class);
    }

    /**
     * Edit an existing post, using the server
     *
     * @param postId The ID of the post to edit.
     * @param postDetails The new post details.
     * @param jwtToken The JWT token for authorization.
     * @param callback The callback to handle the response.
     */
    public void editPost(String postId, PostDetails postDetails, String jwtToken, EditPostResponseCallback callback) {
        // Assuming you have a request body similar to AddPostRequest for editing
        AddPostRequest editPostRequest = new AddPostRequest(postDetails.getUserInput(), postDetails.getPicture());
        // Adjust the method call according to your Retrofit interface definition
        Call<PostJsonDetails> call = postWebServiceAPI.editPost(UserDetails.getInstance().getUsername(), postId,
                "Bearer " + jwtToken, editPostRequest);

        call.enqueue(new Callback<PostJsonDetails>() {
            @Override
            public void onResponse(@NonNull Call<PostJsonDetails> call, @NonNull Response<PostJsonDetails> response) {
                new Thread(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        // Assuming response contains updated post details
                        callback.onSuccess(postDetails);
                    } else {
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

    public interface EditPostResponseCallback {
        void onSuccess(PostDetails updatedPost);
        void onFailure(String errorMessage);
    }

}
