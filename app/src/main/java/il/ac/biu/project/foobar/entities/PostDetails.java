package il.ac.biu.project.foobar.entities;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.LinkedList;
import java.util.List;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * Represents the details of a post.
 */
@Entity
public class PostDetails {
    // Unique identifier for the post
    @PrimaryKey(autoGenerate = true)
    private int id;
    // Display name of the post author
    private String authorDisplayName;

    // Username of the post author
    private String username;

    // Profile picture of the post author
    private Bitmap authorProfilePicture;
    // User input or content of the post
    private String userInput;
    // Picture attached to the post
    private Bitmap picture;
    // Time when the post was created
    private long time;
    // List of users who liked the post
    private List<String> likeList = new LinkedList<>();
    private List<Comment> commentList = new LinkedList<>();

    /**
     * Constructor to initialize post details.
     * @param id Unique identifier for the post.
     * @param authorDisplayName Display name of the post author.
     * @param authorProfilePicture Profile picture of the post author.
     * @param userInput User input or content of the post.
     * @param picture Picture attached to the post.
     * @param time Time when the post was created.
     */
    public PostDetails(int id,String username, String authorDisplayName, Bitmap authorProfilePicture, String userInput, Bitmap picture, long time) {
        this.setUsername(username);
        this.id = id;
        this.authorDisplayName = authorDisplayName;
        this.authorProfilePicture = authorProfilePicture;
        this.userInput = userInput;
        this.picture = picture;
        this.time = time;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorDisplayName() {
        return authorDisplayName;
    }

    public void setAuthorDisplayName(String authorDisplayName) {
        this.authorDisplayName = authorDisplayName;
    }

    public Bitmap getAuthorProfilePicture() {
        return authorProfilePicture;
    }

    public void setAuthorProfilePicture(Bitmap authorProfilePicture) {
        this.authorProfilePicture = authorProfilePicture;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public long getTime() {
        return time;
    }

    public String getTimeStr() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant
                .ofEpochMilli(time), TimeZone.getDefault().toZoneId());

        return "" + dateTime.getDayOfMonth() + '/' + dateTime.getMonthValue() + '/' +
            dateTime.getYear() + ' ' + dateTime.getHour() + ':' +
            String.format("%02d", dateTime.getMinute());
    }

    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Adds a like to the post.
     * @param likeIdentifier Identifier of the user who liked the post.
     */
    public void addLike(String likeIdentifier) {
        if (!likeList.contains(likeIdentifier)) {
            likeList.add(likeIdentifier);
        }
    }

    /**
     * Gets the number of likes for the post.
     * @return Number of likes for the post.
     */
    public int getNumberOfLikes() {
        return likeList.size();
    }

    /**
     * Removes a like from the post.
     * @param likeIdentifier Identifier of the user whose like needs to be removed.
     * @return True if the like was successfully removed, false otherwise.
     */
    public boolean removeLike(String likeIdentifier) {
        return likeList.remove(likeIdentifier);
    }

    /**
     * Checks if a user has liked the post.
     * @param user Identifier of the user to check.
     * @return True if the user has liked the post, false otherwise.
     */
    public boolean isLiked(String user) {
        return likeList.contains(user);
    }


    /**
     * Adds a comment to the list.
     *
     * @param comment The {@link Comment} object to be added to the list.
     */
    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    /**
     * Removes a specified comment from the list by comparing the comment object itself.
     *
     * @param comment The {@link Comment} object to be removed.
     * @return true if the comment was found and successfully removed, false otherwise.
     */
    public boolean removeComment(Comment comment) {
        return commentList.remove(comment);
    }

    /**
     * Retrieves a comment from the list by its index.
     *
     * @param index The index of the comment in the list.
     * @return The {@link Comment} at the specified index in the list.
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size()).
     */
    public Comment getComment(int index) {
        return commentList.get(index);
    }

    /**
     * Returns the number of comments in the list.
     *
     * @return The size of the comment list.
     */
    public int getCommentCount() {
        return commentList.size();
    }
    /**
     * Retrieves the username of the user.
     *
     * @return A String representing the user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets or updates the username of the user.
     *
     * @param username The new username to be set for the user. This is a String value.
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
