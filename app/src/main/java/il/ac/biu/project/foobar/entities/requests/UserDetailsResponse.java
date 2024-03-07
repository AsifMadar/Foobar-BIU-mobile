package il.ac.biu.project.foobar.entities.requests;

import java.util.List;

public class UserDetailsResponse {
    private String username;
    private String displayName;
    private String profileImage;
    private List<String> friends;

    public UserDetailsResponse(String username, String displayName, String profileImage, List<String> friends) {
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

    public List<String> getFriends() {
        return friends;
    }
}

