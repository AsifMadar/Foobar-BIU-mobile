package il.ac.biu.project.foobar.repositories.PostsTasks;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import il.ac.biu.project.foobar.api.post.DeletePostAPI;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.PostManager;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.repositories.PostDao;

public class DeletePostTask extends AsyncTask<PostDetails, Void, PostDetails> {
    private PostDao dao;
    private DeletePostAPI apiService = new DeletePostAPI(); // Your API service for making network requests
    private MutableLiveData<List<PostDetails>> postListData;

    private PostDetails postDetails;

    public DeletePostTask(MutableLiveData<List<PostDetails>> postListData, PostDao dao, PostDetails postDetails) {
        this.postListData = postListData;
        this.dao = dao;
        this.postDetails = postDetails;
    }

    @Override
    protected PostDetails doInBackground(PostDetails... posts) {
        apiService.deletePost(postDetails, UserDetails.getInstance().getJwt(),
                new DeletePostAPI.DeletePostResponseCallback() {
            @Override
            public void onSuccess(PostDetails removedPost) {
                onPostExecute(removedPost);
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
            PostManager.getInstance().removePost(post.getId());
            dao.delete(post);
            postListData.postValue(dao.getPosts());
        }
    }
}
