package com.example.journeyjournal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.journeyjournal.ParseConnectorFiles.User;
import com.example.journeyjournal.R;
import com.example.journeyjournal.fragments.ComposeFragment;
import com.example.journeyjournal.fragments.FeedFragment;
import com.example.journeyjournal.fragments.HomeFragment;
import com.example.journeyjournal.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

// activity that holds bottom navigation bar and allows it ot be accessible
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    HomeFragment homeFragment = new HomeFragment();
    FeedFragment feedFragment = new FeedFragment();
    ComposeFragment composeFragment = new ComposeFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    // declaring items in layout
    public BottomNavigationView bottomNavigationView;

    // setting up fragments
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bottom navigation bar functionality
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragmentToShow = null;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragmentToShow = homeFragment;
                        break;
                    case R.id.action_compose:
                        fragmentToShow = composeFragment;
                        break;
                    case R.id.action_feed:
                        fragmentToShow = feedFragment;
                        break;
                    case R.id.action_profile:
                        fragmentToShow = profileFragment;
                        break;
                    default: break;
                }
                if (fragmentToShow != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragmentToShow).commit();
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    public void goToProfileFragment(ParseUser user) {
        // makes sure the profile navigation leads to the user whose image was selected
        bottomNavigationView.setSelectedItemId(R.id.action_profile);
        profileFragment.user = (User) user;
    }

}
