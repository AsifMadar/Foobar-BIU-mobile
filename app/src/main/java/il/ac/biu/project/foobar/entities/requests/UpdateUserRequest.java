package il.ac.biu.project.foobar.entities.requests;

import static il.ac.biu.project.foobar.utils.images.bitmapToByteArray;
import static il.ac.biu.project.foobar.utils.images.byteArrayToBase64;

import android.graphics.Bitmap;

public class UpdateUserRequest {
    String displayName;
    String profilePic;


    public UpdateUserRequest(String displayName, Bitmap profileImage) {
        this.displayName = displayName;
        byte[] profileImageBytes = bitmapToByteArray(profileImage);
        this.profilePic = byteArrayToBase64(profileImageBytes);
    }
}
