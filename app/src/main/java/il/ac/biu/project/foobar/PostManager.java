package il.ac.biu.project.foobar;

import java.util.HashMap;

public class PostManager {
    private static PostManager instance;
    private HashMap<Integer, PostDetails> postMap;

    private PostManager() {
        postMap = new HashMap<>();
    }

    public static synchronized PostManager getInstance() {
        if (instance == null) {
            instance = new PostManager();
        }
        return instance;
    }

    public void putPost(int id, PostDetails postDetails) {
        postMap.put(id, postDetails);
    }

    public PostDetails getPost(int id) {
        return postMap.get(id);
    }

    public void removePost(int id) {
        postMap.remove(id);
    }

    public HashMap<Integer, PostDetails> getAllPosts() {
        return new HashMap<>(postMap); // Return a copy to prevent modification outside
    }
}
