package il.ac.biu.project.foobar;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class PostDetails implements Parcelable {
    private int id;
    private String authorDisplayName;
    private Bitmap authorProfilePicture;
    private String userInput;
    private Bitmap picture;
    private String time;

    // Constructor used for parcel
    protected PostDetails(Parcel in) {
        id = in.readInt();
        authorDisplayName = in.readString();
        authorProfilePicture = in.readParcelable(Bitmap.class.getClassLoader());
        userInput = in.readString();
        picture = in.readParcelable(Bitmap.class.getClassLoader());
        time = in.readString(); // Read the time string from the parcel
    }

    public PostDetails() {}

    // Standard constructor
    public PostDetails(int id, String authorDisplayName, Bitmap authorProfilePicture, String userInput, Bitmap picture, String time) {
        this.id = id;
        this.authorDisplayName = authorDisplayName;
        this.authorProfilePicture = authorProfilePicture;
        this.userInput = userInput;
        this.picture = picture;
        this.time = time;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id); // Write the id integer to the parcel
        dest.writeString(authorDisplayName);
        dest.writeParcelable(authorProfilePicture, flags);
        dest.writeString(userInput);
        dest.writeParcelable(picture, flags);
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostDetails> CREATOR = new Creator<PostDetails>() {
        @Override
        public PostDetails createFromParcel(Parcel in) {
            return new PostDetails(in);
        }

        @Override
        public PostDetails[] newArray(int size) {
            return new PostDetails[size];
        }
    };

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

    // New getter and setter for the time field
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
