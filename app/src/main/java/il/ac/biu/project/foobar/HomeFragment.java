package il.ac.biu.project.foobar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    TextView textViewadd;
    AlertDialog dialog;
    LinearLayout layout;
    ImageView profileImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the TextView within the inflated layout
        textViewadd = view.findViewById(R.id.whats_on_your_mind_textview);
        layout = view.findViewById(R.id.container);
        profileImage = view.findViewById(R.id.profile_image);



        // Set user profile picture
        Bitmap userProfilePicture = UserDetails.getInstance().getImg();
        if (userProfilePicture != null) {
            profileImage.setImageBitmap(userProfilePicture);
        }
        return view;
    }

    // Other methods remain unchanged
}