package il.ac.biu.project.foobar.api.post;

import androidx.annotation.NonNull;
import il.ac.biu.project.foobar.MyApplication;
import il.ac.biu.project.foobar.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddLikeAPI {

    private Retrofit retrofit;
    private PostWebServiceAPI postWebServiceAPI;

    public AddLikeAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        postWebServiceAPI = retrofit.create(PostWebServiceAPI.class);
    }

    /**
     * Adds a like to a post.
     *
     * @param postID The ID of the post to be liked.
     * @param jwtToken The JWT token for authorization.
     * @param callback The callback to handle the response.
     */
    public void addLike(String postID, String jwtToken, AddLikeResponseCallback callback) {
        Call<Void> call = postWebServiceAPI.addLike(postID, "Bearer " + jwtToken);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                new Thread(() ->{
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Error: " + response.code());
                }
                }).start();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onFailure("Failure: " + t.getMessage());
            }
        });
    }

    public interface AddLikeResponseCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

}
