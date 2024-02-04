package il.ac.biu.project.foobar;

import android.graphics.Bitmap;

/**
 * Singleton class for managing user details.
 */
public class UserDetails {

    private static UserDetails instance;

    private boolean signIn;
    private String username;
    private String password;
    private Bitmap img;
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
     * Set the display name.
     *
     * @param displayname1 The display name to set.
     */
    public void setDisplayName(String displayname1) {
        displayName = displayname1;
    }

    /**
     * Set the profile photo (img).
     *
     * @param profilePhoto The profile photo to set.
     */
    public void setImg(Bitmap img1) {
        img = img1;
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
     * Get the display name.
     *
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the profile photo (img).
     *
     * @return The profile photo (img).
     */
    public Bitmap getImg() {
        return img;
    }
}
