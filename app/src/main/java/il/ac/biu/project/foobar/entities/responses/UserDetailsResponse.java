package il.ac.biu.project.foobar.entities.responses;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsResponse {
    private String username;
    private String displayName;
    private String profileImage;
    private ArrayList<String> friends;

    public UserDetailsResponse(String username, String displayName, String profileImage, ArrayList<String> friends) {
        this.username = username;
        this.displayName = displayName;
        this.profileImage = profileImage;
        this.friends = friends;
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
}

