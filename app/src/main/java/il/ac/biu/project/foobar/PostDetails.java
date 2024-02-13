package il.ac.biu.project.foobar;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;
import java.util.List;

public class PostDetails {
    private int id;
    private String authorDisplayName;
    private Bitmap authorProfilePicture;
    private String userInput;
    private Bitmap picture;
    private String time;
    private List<String> likeList = new LinkedList<>();



    public PostDetails(int id, String authorDisplayName, Bitmap authorProfilePicture, String userInput, Bitmap picture, String time) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void addLike(String likeIdentifier) {
        if (!likeList.contains(likeIdentifier)) {
            likeList.add(likeIdentifier);
        }
    }

    public int getNumberOfLikes() {
        if (likeList.isEmpty()) {
            return 0;
        }
        return likeList.size();
    }

    public boolean removeLike(String likeIdentifier) {
        return likeList.remove(likeIdentifier);
    }

    public boolean isLiked(String user) {
        return likeList.contains(user);
    }
}
