package il.ac.biu.project.foobar;

import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.List;

public class Comment {
    private String text;
    private String authorName;
    private String time;
    private Bitmap authorPicture;
    private List<String> likeList = new ArrayList<>();
    private List<Comment> commentList = new ArrayList<>();

    /**
     * Gets the text of the comment.
     * @return The text of the comment.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the comment.
     * @param text The text to set for the comment.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the name of the author of the comment.
     * @return The author's name.
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Sets the name of the author of the comment.
     * @param authorName The name of the author.
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * Gets the time the comment was posted.
     * @return The post time of the comment.
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time the comment was posted.
     * @param time The post time to set.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Gets the picture of the author of the comment.
     * @return The Bitmap of the author's picture.
     */
    public Bitmap getAuthorPicture() {
        return authorPicture;
    }

    /**
     * Sets the picture of the author of the comment.
     * @param authorPicture The Bitmap of the author's picture to set.
     */
    public void setAuthorPicture(Bitmap authorPicture) {
        this.authorPicture = authorPicture;
    }

    /**
     * Adds a like to the comment.
     * @param userId The ID of the user liking the comment.
     */
    public void addLike(String userId) {
        likeList.add(userId);
    }

    /**
     * Removes a like from the comment.
     * @param userId The ID of the user whose like is to be removed.
     * @return true if the user's like was found and removed, false otherwise.
     */
    public boolean removeLike(String userId) {
        return likeList.remove(userId);
    }

    /**
     * Gets the number of likes on the comment.
     * @return The number of likes.
     */
    public int getLikesCount() {
        return likeList.size();
    }

    /**
     * Checks if a user has liked the post.
     * @param user Identifier of the user to check.
     * @return True if the user has liked the post, false otherwise.
     */
    public boolean isLiked(String user) {
        return likeList.contains(user);
    }
}
