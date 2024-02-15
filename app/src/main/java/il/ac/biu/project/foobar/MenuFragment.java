package il.ac.biu.project.foobar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView profileImage;
    private TextView profileName;
    private GridView buttonsGrid;
    private RecyclerView recyclerView;
    private GridButtonAdapter adapter;
    SwitchCompat switchMode;
    boolean nightmode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Load the saved night mode setting from SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightmode = sharedPreferences.getBoolean("nightmode", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        UserDetails userDetails = UserDetails.getInstance();

        // Find the logout button
        Button logoutButton = rootView.findViewById(R.id.logout);

        // Set click listener for the logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear user session
                userDetails.setSignIn(false);

                // Go back to MainActivity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish(); // Finish the current activity (MenuFragment)
            }
        });
        profileImage = rootView.findViewById(R.id.menu_profile_picture);
        profileName = rootView.findViewById(R.id.menu_profile_name);

        // Set user profile picture
        Bitmap userProfilePicture = userDetails.getImg();
        if (userProfilePicture != null) {
            profileImage.setImageBitmap(userProfilePicture);
        }

        String userName = userDetails.getDisplayName();
        if (userName != null) {
            profileName.setText(userName);
        }

        // Set up dark mode switch
        switchMode = rootView.findViewById(R.id.switch_dark_mode);

        switchMode.setChecked(nightmode); // Set the switch state based on the night mode setting

        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle night mode
                nightmode = !nightmode;
                switchMode.setChecked(nightmode); // Update the switch state

                if (nightmode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                // Save the night mode setting
                editor = sharedPreferences.edit();
                editor.putBoolean("nightmode", nightmode);
                editor.apply();

                // Close the MenuFragment and show the home fragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(MenuFragment.this).commit();
                ((FeedActivityMain) requireActivity()).showHome();
            }
        });

        return rootView;
    }
}