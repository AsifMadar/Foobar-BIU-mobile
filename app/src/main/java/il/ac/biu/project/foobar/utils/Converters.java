package il.ac.biu.project.foobar.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import il.ac.biu.project.foobar.entities.Comment;

public class Converters {
    private static Gson gson = new Gson();


    @TypeConverter
    public static List<String> fromString(String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<String> list) {
            if(list == null)
                return null;
        return gson.toJson(list);
    }

    @TypeConverter
    public static byte[] fromBitmap(Bitmap bitmap) {
        if (bitmap == null)
            return  null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    @TypeConverter
    public static Bitmap toBitmap(byte[] byteArray) {
            if (byteArray == null)
                return null;
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
    @TypeConverter
    public static List<Comment> stringToCommentList(String data) {
        if (data == null) {
            return null;
        }
        Type listType = new TypeToken<List<Comment>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String commentListToString(List<Comment> someObjects) {
            if(someObjects == null)
                return  null;
        return gson.toJson(someObjects);
    }
}
