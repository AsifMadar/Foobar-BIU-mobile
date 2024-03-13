package il.ac.biu.project.foobar.repositories.PostsTasks;

import static il.ac.biu.project.foobar.utils.Converters.convertToPostDetailsList;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import androidx.lifecycle.MutableLiveData;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import il.ac.biu.project.foobar.api.post.GetUserPostsAPI; // Assuming you have a similar GetUserPostsAPI
import il.ac.biu.project.foobar.entities.Comment;
import il.ac.biu.project.foobar.entities.PostJsonDetails;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.repositories.PostDao;
import il.ac.biu.project.foobar.entities.UserDetails;

public class GetUserPostsTask extends AsyncTask<Void, Void, Void> {
    private GetUserPostsAPI apiService = new GetUserPostsAPI();
    private MutableLiveData<List<PostDetails>> postListData;
    private PostDao dao;
    private UserDetails userDetails = UserDetails.getInstance();
    private String userId; // User ID whose posts are to be fetched


    public GetUserPostsTask(String userId, MutableLiveData<List<PostDetails>> postListData, PostDao dao) {
        this.userId = userId;
        this.dao = dao;
        this.postListData = postListData;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        apiService.getUserPosts(userId, userDetails.getJwt(), new GetUserPostsAPI.GetUserPostsResponseCallback() {
            @Override
            public void onSuccess(List<PostJsonDetails> postJsonDetailsList) {
                List<PostDetails> postDetailsList = convertToPostDetailsList(postJsonDetailsList);
//                dao.deleteAll();
//                for (PostDetails postDetails : postDetailsList) {
//                    dao.insert(postDetails);
//                }
                postListData.postValue(postDetailsList);
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
