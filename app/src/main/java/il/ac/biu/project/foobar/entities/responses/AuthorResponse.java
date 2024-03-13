package il.ac.biu.project.foobar.entities.responses;

public class AuthorResponse {
    private String displayName;
    private String profileImage;
    private String username;

    // Constructors
    public AuthorResponse(String displayName, String profileImage, String username) {
        this.displayName = displayName;
        this.profileImage = profileImage;
        this.username = username;
    }

    // Getters and Setters
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

