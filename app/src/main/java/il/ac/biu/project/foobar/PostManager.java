package il.ac.biu.project.foobar;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A singleton which manages the posts by ID.
 */
public class PostManager {
    private static PostManager instance;
    private HashMap<Integer, PostDetails> postMap;

    /**
     * Constructs a private PostManager instance. This constructor initializes the postMap, a HashMap used to store and manage PostDetails objects.
     */
    private PostManager() {
        postMap = new HashMap<>();
    }

    /**
     * Provides access to the singleton instance of the PostManager. If the instance does not exist, it is created. This method ensures that only one instance of the PostManager exists within the application, providing a global point of access.
     *
     * @return The single instance of the PostManager.
     */
    public static synchronized PostManager getInstance() {
        if (instance == null) {
            instance = new PostManager();
        }
        return instance;
    }

    /**
     * Adds or updates a post in the postMap. If a post with the same id already exists, it is replaced.
     *
     * @param id The unique identifier for the post.
     * @param postDetails The PostDetails object containing the post's data.
     */
    public void putPost(int id, PostDetails postDetails) {
        postMap.put(id, postDetails);
    }

    /**
     * Retrieves a post from the postMap by its id.
     *
     * @param id The unique identifier of the post to retrieve.
     * @return The PostDetails object associated with the specified id, or null if no such post exists.
     */
    public PostDetails getPost(int id) {
        return postMap.get(id);
    }

    /**
     * Removes a post from the postMap by its id.
     *
     * @param id The unique identifier of the post to be removed.
     */
    public void removePost(int id) {
        postMap.remove(id);
    }

    /**
     * Retrieves a list of all posts currently stored in the postMap, sorted by publishing time (oldest first).
     *
     * @return A list containing all the PostDetails objects stored in the postMap.
     */
    public List<PostDetails> getAllPosts() {
        LinkedList<PostDetails> postsList = new LinkedList<PostDetails>(postMap.values());
        postsList.sort(Comparator.comparingLong(PostDetails::getTime));
        return postsList;
    }

    /**
     * Checks if a post with the specified ID exists in the postMap.
     *
     * @param id The unique identifier of the post to check.
     * @return true if the post exists, false otherwise.
     */
    public boolean containsPost(int id) {
        return postMap.containsKey(id);
    }

}
