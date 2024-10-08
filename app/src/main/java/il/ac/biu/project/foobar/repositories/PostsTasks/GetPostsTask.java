package il.ac.biu.project.foobar.repositories.PostsTasks;

import static il.ac.biu.project.foobar.utils.Converters.convertToPostDetailsList;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import androidx.lifecycle.MutableLiveData;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import il.ac.biu.project.foobar.api.post.GetPostsAPI;
import il.ac.biu.project.foobar.entities.Comment;
import il.ac.biu.project.foobar.entities.PostJsonDetails;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.PostManager;
import il.ac.biu.project.foobar.repositories.PostDao;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.utils.images;

public class GetPostsTask extends AsyncTask<Void, Void, Void> {
    private GetPostsAPI apiService = new GetPostsAPI();
    private MutableLiveData<List<PostDetails>> postListData;
    private PostDao dao;
    private UserDetails userDetails = UserDetails.getInstance();
    SwipeRefreshLayout postsRefresh;

    public GetPostsTask(MutableLiveData<List<PostDetails>> postListData, PostDao dao, SwipeRefreshLayout postsRefresh) {
        this.dao = dao;
        this.postListData = postListData;
        this.postsRefresh = postsRefresh;
    }

    public GetPostsTask(MutableLiveData<List<PostDetails>> postListData, PostDao dao) {
        this.dao = dao;
        this.postListData = postListData;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        apiService.getPosts(userDetails.getJwt(), new GetPostsAPI.GetPostsResponseCallback() {
            @Override
            public void onSuccess(List<PostJsonDetails> postJsonDetailsList) {
                // Convert PostJsonDetails to PostDetails
                List<PostDetails> postDetailsList = convertToPostDetailsList(postJsonDetailsList);
                dao.deleteAll();
                for (PostDetails postDetails : postDetailsList) {
                    dao.insert(postDetails);
                }
                postListData.postValue(dao.getPosts());
                if (postsRefresh != null)
                  postsRefresh.setRefreshing(false);
                PostManager.getInstance().removeAllPosts();
            }

            @Override
            public void onFailure(String errorMessage) {
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }


}
