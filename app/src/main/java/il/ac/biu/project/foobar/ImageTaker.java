package il.ac.biu.project.foobar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.IOException;

/**
 * A utility class to facilitate image selection from the device's camera or gallery.
 * It abstracts the permission requests, intent handling, and bitmap extraction logic
 * to simplify image picking in activities.
 */
public class ImageTaker {

    private Activity activity;
    private Bitmap imageBitmap;
    private static final int IMAGE_PICK_CODE = 1;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    /**
     * Constructs an ImageTaker associated with a specific Activity.
     * @param activity The activity from which image picking is invoked.
     */
    public ImageTaker(Activity activity) {
        this.activity = activity;
    }

    /**
     * Initiates the process to pick an image, either from the device's camera or gallery,
     * after checking for the necessary permissions.
     */
    public void pickImage() {
        if (!checkPermissions()) {
            requestPermissions();
            return;
        }
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
        pickImageIntent.setType("image/*");

        Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooserIntent = Intent.createChooser(pickImageIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureImageIntent});

        activity.startActivityForResult(chooserIntent, IMAGE_PICK_CODE);
    }

    /**
     * Checks if the necessary permissions (camera and read external storage) are granted.
     * @return True if all necessary permissions are granted, false otherwise.
     */
    private boolean checkPermissions() {
        boolean cameraPer = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean readStoragePer = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return cameraPer && readStoragePer;
    }

    /**
     * Requests the necessary permissions from the user.
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSIONS_REQUEST_CODE);
    }

    /**
     * Handles the result of the permission request. If permissions are granted, proceeds to pick an image.
     * @param requestCode  The request code passed in requestPermissions().
     * @param permissions  The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                Toast.makeText(activity, "Permission denied. Cannot choose an image.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Processes the result of the image pick action. Extracts the image as a Bitmap.
     * @param requestCode The request code passed to startActivityForResult().
     * @param resultCode  The result code returned by the child activity.
     * @param data        The intent data containing the image.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    imageBitmap = (Bitmap) extras.get("data");
                }
            }
        }
    }

    /**
     * Returns the selected or captured image as a Bitmap.
     * @return The image bitmap, or null if no image was selected or captured.
     */
    public Bitmap getImageBitmap() {
        return imageBitmap;
    }
}
