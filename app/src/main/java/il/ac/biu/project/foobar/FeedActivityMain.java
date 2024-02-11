package il.ac.biu.project.foobar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FeedActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedmain);
        Fragment selectedFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, selectedFragment)
                .commit();

        // Initialize BottomNavigationView

        BottomNavigationView navigationView = findViewById(R.id.bootomnavigationid);
        navigationView.setItemIconTintList(null);
        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        int itemId = item.getItemId();
                        if (itemId == R.id.myhome) {
                            selectedFragment = new HomeFragment();
                        } else if (itemId == R.id.MenuID) {
                            selectedFragment = new MenuFragment();
                        }else if (itemId == R.id.FriendsId) {
                            selectedFragment = new FriendsFragment();
                        }else if (itemId == R.id.VideoId) {
                            selectedFragment = new VideoFragment();
                        }else if (itemId == R.id.NotificationsId) {
                            selectedFragment = new NotificationFragment();
                        }

                        // Add additional 'else if' blocks for other menu items if needed

                        if (selectedFragment != null) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.framelayout, selectedFragment)
                                    .commit();
                            return true;
                        }
                        return false;
                    }

                });
    }
}
