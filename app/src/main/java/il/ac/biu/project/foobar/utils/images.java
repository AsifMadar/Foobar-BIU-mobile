package il.ac.biu.project.foobar.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class images {

    public static Bitmap base64ToBitmap(String imgBase64) {
            // Decode the Base64 string into a byte array
            byte[] decodedBytes = Base64.decode(imgBase64, Base64.DEFAULT);

            // Convert the byte array into a Bitmap
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            // Set the decoded bitmap as the user's profile image
            return decodedBitmap;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static String byteArrayToBase64(byte[] byteArray) {
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
