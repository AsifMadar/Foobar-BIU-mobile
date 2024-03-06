package il.ac.biu.project.foobar.api;

import androidx.lifecycle.MutableLiveData;
import java.util.List;

import il.ac.biu.project.foobar.MyApplication;
import il.ac.biu.project.foobar.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import il.ac.biu.project.foobar.entities.PostDetails;


 public class PostAPI {
  /*  private MutableLiveData<List<PostDetails>> postListData;
    private PostDao dao;
    private Retrofit retrofit;
    private PostWebServiceAPI postWebServiceAPI;

    public PostAPI(MutableLiveData<List<PostDetails>> postListData, PostDao dao) {
        this.postListData = postListData;
        this.dao = dao;

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        postWebServiceAPI = retrofit.create(postWebServiceAPI.class);
    }

    public void get() {
        Call<List<PostDetails>> call = postWebServiceAPI.getPosts();
        call.enqueue(new Callback<List<PostDetails>>() {
            @Override
            public void onResponse(Call<List<PostDetails>> call, Response<List<PostDetails>> response) {
                new Thread(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        dao.clear();
                        dao.insertList(response.body());
                        postListData.postValue(dao.get());
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<List<PostDetails>> call, Throwable t) {
                // Handle failure
            }
        });
    } */
}
