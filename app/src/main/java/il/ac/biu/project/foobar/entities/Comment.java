package il.ac.biu.project.foobar.entities;

import android.graphics.Bitmap;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class Comment {
    private String text;
    private String authorDisplayName;
    private long time;
    private Bitmap authorProfilePicture;
    private List<String> likeList = new ArrayList<>();
    private List<Comment> commentList = new ArrayList<>();

    /**
     * Default constructor; Make sure to set all the fields manually if you use this constructor
     */
    public Comment() {}

    /**
     * Constructor to initialize comment details.
     * @param authorDisplayName Display name of the comment author.
     * @param authorProfilePicture Profile picture of the comment author.
     * @param text Comment's contents.
     * @param time Comment's creation time (in ms since UNIX epoch)
     */
    public Comment(String authorDisplayName, Bitmap authorProfilePicture, String text, long time) {
        this.authorDisplayName = authorDisplayName;
        this.authorProfilePicture = authorProfilePicture;
        this.text = text;
        this.time = time;
    }

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
    public String getAuthorDisplayName() {
        return authorDisplayName;
    }

    /**
     * Sets the name of the author of the comment.
     * @param authorDisplayName The name of the author.
     */
    public void setAuthorDisplayName(String authorDisplayName) {
        this.authorDisplayName = authorDisplayName;
    }

    /**
     * Gets the time the comment was posted.
     * @return The post time of the comment.
     */
    public long getTime() {
        return time;
    }

    /***
     * Gets a string representing the time the comment was posted.
     * @return The string representing the time the comment was posted.
     */
    public String getTimeStr() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant
                .ofEpochMilli(time), TimeZone.getDefault().toZoneId());

        return "" + dateTime.getDayOfMonth() + '/' + dateTime.getMonthValue() + ' ' +
                dateTime.getHour() + ':' + String.format("%02d", dateTime.getMinute());
    }

    /**
     * Sets the time the comment was posted.
     * @param time The post time to set.
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Gets the picture of the author of the comment.
     * @return The Bitmap of the author's picture.
     */
    public Bitmap getAuthorProfilePicture() {
        return authorProfilePicture;
    }

    /**
     * Sets the picture of the author of the comment.
     * @param authorProfilePicture The Bitmap of the author's picture to set.
     */
    public void setAuthorProfilePicture(Bitmap authorProfilePicture) {
        this.authorProfilePicture = authorProfilePicture;
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
