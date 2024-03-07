package il.ac.biu.project.foobar.repositories.PostsTasks;

import android.os.AsyncTask;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.repositories.PostDao;

// Assuming PostDao is an interface for database operations
public class GetPostsTask extends AsyncTask<Void, Void, List<PostDetails>> {
    private MutableLiveData<List<PostDetails>> postListData;
    private PostDao dao;

    public GetPostsTask(MutableLiveData<List<PostDetails>> postListData, PostDao dao) {
        this.postListData = postListData;
        this.dao = dao;
    }

    @Override
    protected List<PostDetails> doInBackground(Void... voids) {
        // Simulating web service connection and data retrieval
        // This is where you'd normally use an HTTP client to fetch data
        // and parse the JSON response into a list of Post objects

        // For demonstration, let's assume dao.getPosts() fetches the posts
        List<PostDetails> posts = dao.getPosts();
        return posts;
    }

    @Override
    protected void onPostExecute(List<PostDetails> posts) {
        // Once the background task is done, update LiveData with the list of posts
        postListData.postValue(posts);
    }
}
