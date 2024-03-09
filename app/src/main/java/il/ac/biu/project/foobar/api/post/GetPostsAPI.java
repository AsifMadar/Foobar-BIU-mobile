package il.ac.biu.project.foobar.api.post;

import androidx.annotation.NonNull;
import il.ac.biu.project.foobar.MyApplication;
import il.ac.biu.project.foobar.R;
import il.ac.biu.project.foobar.entities.PostJsonDetails;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetPostsAPI {

    private Retrofit retrofit;
    private PostWebServiceAPI postWebServiceAPI;

    public GetPostsAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        postWebServiceAPI = retrofit.create(PostWebServiceAPI.class);
    }

    /**
     * Fetches posts from the server.
     *
     * @param jwtToken The JWT token for authorization.
     * @param callback The callback to handle the response.
     */
    public void getPosts(String jwtToken, GetPostsResponseCallback callback) {
        Call<List<PostJsonDetails>> call = postWebServiceAPI.getPosts("Bearer " + jwtToken);

        call.enqueue(new Callback<List<PostJsonDetails>>() {
            @Override
            public void onResponse(@NonNull Call<List<PostJsonDetails>> call, @NonNull Response<List<PostJsonDetails>> response) {
                new Thread(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        callback.onSuccess(response.body());
                    } else {
                        callback.onFailure("Error: " + response.code());
                    }
                }).start();
            }

            @Override
            public void onFailure(@NonNull Call<List<PostJsonDetails>> call, @NonNull Throwable t) {
                // Notify callback about failure to communicate with the server
                callback.onFailure("Failure: " + t.getMessage());
            }
        });
    }

    public interface GetPostsResponseCallback {
        void onSuccess(List<PostJsonDetails> posts);
        void onFailure(String errorMessage);
    }
}
