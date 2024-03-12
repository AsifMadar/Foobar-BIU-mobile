package il.ac.biu.project.foobar.viewmodels;

import static il.ac.biu.project.foobar.MyApplication.context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.repositories.PostsRepository;

public class PostsViewModel extends ViewModel {
    private PostsRepository postsRepository;
    private LiveData<List<PostDetails>> posts;


    public PostsViewModel() {
        postsRepository = new PostsRepository(context);
        posts = postsRepository.getAll();
    }
    public LiveData<List<PostDetails>> get() {
        return posts;
    }

    public void add(PostDetails post) {
        postsRepository.add(post);
    }

    public void edit(PostDetails post) {
        postsRepository.editPost(post);
    }
    public void delete(PostDetails post) {
        postsRepository.deletePost(post);
    }

    public void addLike(PostDetails post) {
        postsRepository.addLike(post);
    }

    public PostDetails getPostFromData(String postID) {
        return postsRepository.getPostFromData(postID);
    }
    public void reload(SwipeRefreshLayout postsRefresh) {
        postsRepository.reload(postsRefresh);
    }

    //used for the first initialization
    public void reload() {
        postsRepository.reload();
    }
    public void reloadUserPosts(String userID) {
        postsRepository.reloadUserPosts(userID);
    }

    public void clearPostsFromDB() {
        postsRepository.clearPostsFromDB();
    }

}
