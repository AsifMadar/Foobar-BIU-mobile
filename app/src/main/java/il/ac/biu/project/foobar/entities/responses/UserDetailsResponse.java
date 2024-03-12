package il.ac.biu.project.foobar.entities.responses;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsResponse {
    private String username;
    private String displayName;
    private String profileImage;
    private ArrayList<String> friends;
    private ArrayList<String> friendRequests;

    public UserDetailsResponse(String username, String displayName, String profileImage, ArrayList<String> friends,ArrayList<String> friendRequests) {
        this.username = username;
        this.displayName = displayName;
        this.profileImage = profileImage;
        this.friends = friends;
        this.friendRequests =friendRequests;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }
    public ArrayList<String> getFriendRequests() {
        return friendRequests;
    }
}

