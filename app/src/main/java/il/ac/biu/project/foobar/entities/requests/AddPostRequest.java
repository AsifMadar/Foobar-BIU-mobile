package il.ac.biu.project.foobar.entities.requests;

import static il.ac.biu.project.foobar.utils.images.bitmapToByteArray;
import static il.ac.biu.project.foobar.utils.images.byteArrayToBase64;

import android.graphics.Bitmap;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class AddPostRequest {
    private final String contents;
    private List<String> images = new LinkedList<>(); // Base64-encoded image

    // Constructor that accepts a Bitmap and content, then converts the Bitmap to a Base64 string
    public AddPostRequest(String contents, Bitmap imageBitmap) {
        this.contents = contents;
        if (imageBitmap != null) {
            this.images.add(byteArrayToBase64(bitmapToByteArray(imageBitmap)));
        }
    }
}
