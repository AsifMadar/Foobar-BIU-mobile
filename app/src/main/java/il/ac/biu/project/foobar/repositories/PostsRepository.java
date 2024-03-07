package il.ac.biu.project.foobar.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import java.util.LinkedList;
import java.util.List;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.repositories.PostsTasks.AddPostTask;
import il.ac.biu.project.foobar.repositories.PostsTasks.GetPostsTask;

public class PostsRepository {
    private PostDao dao;
    private AppDB db;

    private PostListData postListData;

    public PostsRepository(Context context) {
          db = Room.databaseBuilder(context.getApplicationContext(),
                AppDB.class, "PostsDB").build();
        dao = db.postDao();
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
            new Thread(() -> postListData.postValue(dao.getPosts())).start();
        }
    }

    public LiveData<List<PostDetails>> getAll() {
        return postListData;
    }

    public void reload(){
        new GetPostsTask(postListData, dao).execute();
    }

    public void add(PostDetails postDetails){
        new AddPostTask(postListData, dao, postDetails).execute();
    }
    public PostDetails getPostFromData(String id){
        return dao.get(id);
    }



}

