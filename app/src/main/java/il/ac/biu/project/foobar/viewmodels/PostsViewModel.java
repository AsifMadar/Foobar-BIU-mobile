package il.ac.biu.project.foobar.viewmodels;

import static il.ac.biu.project.foobar.MyApplication.context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

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

    public PostDetails getPostFromData(String postID) {
        return postsRepository.getPostFromData(postID);
    }
//
//    public void delete(PostDetails post) {
//        postsRepository.delete(post);
//    }

    public void reload() {
        postsRepository.reload();
    }

}
