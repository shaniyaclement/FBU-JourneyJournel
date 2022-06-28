package com.example.journeyjournal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.journeyjournal.R;
import com.example.journeyjournal.fragments.ComposeFragment;
import com.example.journeyjournal.fragments.FeedFragment;
import com.example.journeyjournal.fragments.HomeFragment;
import com.example.journeyjournal.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragmentToShow = null;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragmentToShow = new HomeFragment();
                        break;
                    case R.id.action_compose:
                        fragmentToShow = new ComposeFragment();
                        break;
                    case R.id.action_feed:
                        fragmentToShow = new FeedFragment();
                        break;
                    case R.id.action_profile:
                        fragmentToShow = new ProfileFragment();
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
}

//        btnJournalPage = findViewById(R.id.btnJournalPage);
//        ParseObject firstObject = new  ParseObject("TestClass");
//        firstObject.put("message","Hey! Parse is now connected");
//        firstObject.saveInBackground(e -> {
//            if (e != null){
//                Log.e(TAG, e.getLocalizedMessage());
//            } else{
//                Log.d(TAG,"Object saved.");
//            }
//        });