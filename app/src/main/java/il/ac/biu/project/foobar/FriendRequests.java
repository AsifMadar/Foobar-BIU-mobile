package il.ac.biu.project.foobar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import il.ac.biu.project.foobar.FriendRequestsAdapter;
import il.ac.biu.project.foobar.entities.UserDetails;

public class FriendRequests extends AppCompatActivity {

    private ArrayList<String> friendRequestsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendrequests);

        // Initialize friendRequestsList
        friendRequestsList = new ArrayList<>();

        // Get the friend requests list from UserDetails
        if (UserDetails.getInstance() != null) {
            friendRequestsList = UserDetails.getInstance().getFriendRequests();
        }

        // Find the RecyclerView
        RecyclerView friendRequestsRecyclerView = findViewById(R.id.friendRequestsRecyclerView);

        // Set up the RecyclerView adapter
        FriendRequestsAdapter adapter = new FriendRequestsAdapter(friendRequestsList);
        friendRequestsRecyclerView.setAdapter(adapter);
        friendRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Find the back button by ID
        ImageButton backButton = findViewById(R.id.backButton);

        // Set OnClickListener to navigate back to previous activity or fragment
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity
                finish();
            }
        });
    }
}
