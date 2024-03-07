package il.ac.biu.project.foobar.entities.requests;

import static il.ac.biu.project.foobar.utils.images.bitmapToByteArray;
import static il.ac.biu.project.foobar.utils.images.byteArrayToBase64;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;


public class SignUpRequest {

    private String username;
    private String password;
    private String displayName;
    private String profileImage;


    public SignUpRequest(String username, String password, String displayName, Bitmap profileImage) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        byte[] profileImageBytes = bitmapToByteArray(profileImage);
        this.profileImage = byteArrayToBase64(profileImageBytes);
    }


}
