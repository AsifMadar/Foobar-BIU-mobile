package il.ac.biu.project.foobar.api.post;

import androidx.annotation.NonNull;

import il.ac.biu.project.foobar.MyApplication;
import il.ac.biu.project.foobar.R;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.UserDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeletePostAPI {

    private Retrofit retrofit;
    private PostWebServiceAPI postWebServiceAPI;

    public DeletePostAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        postWebServiceAPI = retrofit.create(PostWebServiceAPI.class);
    }

    /**
     * Delete an existing post, using the server
     *
     * @param postDetails The ID of the post to delete.
     * @param jwtToken The JWT token for authorization.
     * @param callback The callback to handle the response.
     */
    public void deletePost(PostDetails postDetails, String jwtToken, DeletePostResponseCallback callback) {
        Call<Void> call = postWebServiceAPI.deletePost(UserDetails.getInstance().getUsername(),
                postDetails.getId(), "Bearer " + jwtToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                new Thread(() -> {
                    if (response.isSuccessful()) {
                        callback.onSuccess(postDetails);
                    } else {
                        callback.onFailure("Error: " + response.code());
                    }
                }).start();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                // Notify callback about failure to communicate with the server
                callback.onFailure("Failure: " + t.getMessage());
            }
        });
    }

    public interface DeletePostResponseCallback {
        void onSuccess(PostDetails postDetails);
        void onFailure(String errorMessage);
    }
}
