package il.ac.biu.project.foobar.repositories.PostsTasks;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import il.ac.biu.project.foobar.api.post.AddLikeAPI;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.repositories.PostDao;
import retrofit2.Response;

public class AddLikeTask extends AsyncTask<String, Void, Boolean> {
    private AddLikeAPI apiService = new AddLikeAPI(); // Your API service for making network requests
    private MutableLiveData<List<PostDetails>> postListData;
    private UserDetails userDetails = UserDetails.getInstance();
    private PostDao dao;


    private PostDetails post;


    public AddLikeTask(MutableLiveData<List<PostDetails>> postListData, PostDao dao, PostDetails postDetails) {
        this.postListData = postListData;
        this.dao = dao;
        post = postDetails;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        apiService.addLike(post.getId(), userDetails.getJwt(), new AddLikeAPI.AddLikeResponseCallback() {
            @Override
            public void onSuccess() {
                onPostExecute(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                onPostExecute(false);
            }
        });
        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            dao.update(post);
            postListData.postValue(dao.getPosts());
        }
    }
}
