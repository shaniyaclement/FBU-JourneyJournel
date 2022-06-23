package com.example.journeyjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.journeyjournal.fragments.ComposeFragment;
import com.example.journeyjournal.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    //Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //toolbar = findViewById(R.id.postToolbar);
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
//                    case R.id.action_profile:
//                        fragmentToShow = new ProfileFragment();
//                        break;
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