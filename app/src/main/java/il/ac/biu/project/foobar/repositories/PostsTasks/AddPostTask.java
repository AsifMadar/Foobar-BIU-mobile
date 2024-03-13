package il.ac.biu.project.foobar.repositories.PostsTasks;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import il.ac.biu.project.foobar.api.post.AddPostAPI;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.PostManager;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.repositories.PostDao;
import retrofit2.Response;

public class AddPostTask extends AsyncTask<PostDetails, Void, PostDetails> {
    private PostDao dao;
    private AddPostAPI apiService = new AddPostAPI(); // Your API service for making network requests
    private MutableLiveData<List<PostDetails>> postListData;
    private UserDetails userDetails = UserDetails.getInstance();

    private PostDetails postDetails;


    public AddPostTask(MutableLiveData<List<PostDetails>> postListData, PostDao dao, PostDetails postDetails) {
        this.postListData = postListData;
        this.dao = dao;
        this.postDetails = postDetails;
    }

    @Override
    protected PostDetails doInBackground(PostDetails... posts) {
                apiService.addPost(postDetails, userDetails.getJwt(), new AddPostAPI.AddPostResponseCallback() {
                    @Override
                    public void onSuccess(PostDetails addedPost) {
                        onPostExecute(addedPost);
                    }


                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
        return null;
    }

    @Override
    protected void onPostExecute(PostDetails post) {
        if (post != null) {
            PostManager.getInstance().putPost(postDetails.getId(), postDetails);
            dao.insert(post);
            postListData.postValue(dao.getPosts());
        }
    }
}
