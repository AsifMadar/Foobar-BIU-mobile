package il.ac.biu.project.foobar.repositories.PostsTasks;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import il.ac.biu.project.foobar.api.post.EditPostAPI;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.PostManager;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.repositories.PostDao;

public class EditPostTask extends AsyncTask<PostDetails, Void, PostDetails> {
    private PostDao dao;
    private EditPostAPI apiService = new EditPostAPI(); // Your API service for making network requests
    private MutableLiveData<List<PostDetails>> postListData;
    private UserDetails userDetails = UserDetails.getInstance();

    private PostDetails postDetails;

    public EditPostTask(MutableLiveData<List<PostDetails>> postListData, PostDao dao, PostDetails postDetails) {
        this.postListData = postListData;
        this.dao = dao;
        this.postDetails = postDetails;
    }

    @Override
    protected PostDetails doInBackground(PostDetails... posts) {
        apiService.editPost(postDetails.getId(), postDetails, userDetails.getJwt(), new EditPostAPI.EditPostResponseCallback() {
            @Override
            public void onSuccess(PostDetails updatedPost) {
                onPostExecute(updatedPost);
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
            dao.update(post);
            postListData.postValue(dao.getPosts());
        }
    }
}
