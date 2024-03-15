package il.ac.biu.project.foobar.entities.responses;

import android.graphics.Bitmap;

public interface ProfileUpdateListener {
    void onProfileUpdated(String displayName, Bitmap profileImage);
}
