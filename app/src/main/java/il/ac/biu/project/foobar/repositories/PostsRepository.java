package il.ac.biu.project.foobar.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.PostManager;
import il.ac.biu.project.foobar.repositories.PostsTasks.AddLikeTask;
import il.ac.biu.project.foobar.repositories.PostsTasks.AddPostTask;
import il.ac.biu.project.foobar.repositories.PostsTasks.DeletePostTask;
import il.ac.biu.project.foobar.repositories.PostsTasks.EditPostTask;
import il.ac.biu.project.foobar.repositories.PostsTasks.GetPostsTask;
import il.ac.biu.project.foobar.repositories.PostsTasks.GetUserPostsTask;

public class PostsRepository {
    private PostDao dao;
    private AppDB db;

    private PostListData postListData;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();


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

    public void reload(SwipeRefreshLayout postsRefresh){
        new GetPostsTask(postListData, dao, postsRefresh).execute();
    }

    public void reload(){
        new GetPostsTask(postListData, dao).execute();
    }

    public void reloadUserPosts(String userID){
        new GetUserPostsTask(userID, postListData, dao).execute();
    }

    public void add(PostDetails postDetails){
        new AddPostTask(postListData, dao, postDetails).execute();
    }

    public void deletePost(PostDetails postDetails){
        new DeletePostTask(postListData, dao, postDetails).execute();
    }

    public void editPost(PostDetails postDetails){
        new EditPostTask(postListData, dao, postDetails).execute();
    }
    public PostDetails getPostFromData(String id){
        return dao.get(id);
    }
    public void addLike(PostDetails postDetails){
        new AddLikeTask(postListData, dao, postDetails).execute();
    }

    public void clearPostsFromDB() {
        executor.execute(() -> {
            dao.deleteAll();
            PostManager.getInstance().removeAllPosts();
        });
      }



}

