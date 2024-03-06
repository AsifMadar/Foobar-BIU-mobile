package il.ac.biu.project.foobar.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.LinkedList;
import java.util.List;
import il.ac.biu.project.foobar.entities.PostDetails;

public class PostsRepository {
   // private PostDao dao;
    private PostListData postListData;

    public PostsRepository() {
       // LocalDatabase db = LocalDatabase.getInstance();
       // dao = db.postDao();
        postListData = new PostListData();
    }

    class PostListData extends MutableLiveData<List<PostDetails>> {
        public PostListData() {
            super();
            setValue(new LinkedList<PostDetails>());
        }

        @Override
        protected void onActive() {
            super.onActive();
      //      new Thread(() -> postListData.postValue(dao.get())).start();
        }
    }

    public LiveData<List<PostDetails>> getAll() {
        return postListData;
    }
}

