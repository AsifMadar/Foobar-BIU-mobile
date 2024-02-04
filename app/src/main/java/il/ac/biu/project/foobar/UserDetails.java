package il.ac.biu.project.foobar;

import android.graphics.Bitmap;

/**
 * Singleton class for managing user details.
 */
public class UserDetails {

    private static UserDetails instance;

    private boolean signIn = false;
    private String username;
    private String password;
    private String img;
    private String displayName;

    /**
     * Private constructor to prevent instantiation.
     */
    private UserDetails() {}

    /**
     * Get the singleton instance of UserDetails.
     *
     * @return The UserDetails instance.
     */
    public static synchronized UserDetails getInstance() {
        if (instance == null) {
            instance = new UserDetails();
        }
        return instance;
    }

    /**
     * Set the sign-in status.
     *
     * @param flag True if the user is signed in, false otherwise.
     */
    public void setSignIn(boolean flag) {
        signIn = flag;
    }

    /**
     * Set the username.
     *
     * @param username1 The username to set.
     */
    public void setUsername(String username1) {
        username = username1;
    }

    /**
     * Set the password.
     *
     * @param pass The password to set.
     */
    public void setPassword(String pass) {
        password = pass;
    }

    /**
     * Set the image URL.
     *
     * @param img1 The image URL to set.
     */
    public void setImg(String img1) {
        img = img1;
    }

    /**
     * Set the display name.
     *
     * @param displayname1 The display name to set.
     */
    public void setDisplayName(String displayname1) {
        displayName = displayname1;
    }

    /**
     * Get the sign-in status.
     *
     * @return True if the user is signed in, false otherwise.
     */
    public boolean getSignIn() {
        return signIn;
    }

    /**
     * Get the username.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the password.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the image URL.
     *
     * @return The image URL.
     */
    public String getImg() {
        return img;
    }

    /**
     * Get the display name.
     *
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    public void setProfilePhoto(Bitmap profilePhoto) {
    }
}
