package com.example.journeyjournal.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.journeyjournal.Adapters.ReminderAdapter;
import com.example.journeyjournal.ParseConnectorFiles.Post;
import com.example.journeyjournal.ParseConnectorFiles.Reminder;
import com.example.journeyjournal.ParseConnectorFiles.User;
import com.example.journeyjournal.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class RemindersActivity extends AppCompatActivity {
    private static final String TAG = "RemindersActivity";
    RecyclerView rvReminders;
    protected ReminderAdapter adapter;
    protected List<Reminder> allReminders;

    ImageView ivReminder;

    public User user = (User) ParseUser.getCurrentUser();

    @Override
    public void onResume() {
        super.onResume();
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifi.isConnected()){
            // query posts from the database
            Log.i(TAG, "onResume");
            adapter.clear();
            queryReminders();
        } else {
            querySavedReminders();}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        ivReminder = findViewById(R.id.ivReminder);
        ivReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifi.isConnected()) {
                    goComposeReminder();
                } else{
                    Toast.makeText(RemindersActivity.this, "Please connect to the internet", Toast.LENGTH_LONG).show();
                }
            }
        });


        rvReminders = findViewById(R.id.rvReminders);

        allReminders = new ArrayList<>();
        adapter = new ReminderAdapter(this, allReminders);

        // set the adapter on the RV
        rvReminders.setAdapter(adapter);
        // set the layout manager on RV
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvReminders.setLayoutManager(linearLayoutManager);
        //query comments from Parse
        if(wifi.isConnected()){
            // query posts from the database
            Log.i(TAG, "either");
            adapter.clear();
            queryReminders();
        } else {
            querySavedReminders();}    }

    private void goComposeReminder() {
            Intent intent = new Intent(this, ComposeReminder.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }

    private void queryReminders() {
        // specify type of data to query - Reminder.class
        ParseQuery<Reminder> query = ParseQuery.getQuery(Reminder.class);
        // include data referred by user key
        query.include(Reminder.KEY_USER);
        // limit query to latest 20 reminders
        query.setLimit(20);
        query.whereEqualTo(Reminder.KEY_USER, user);

        // order reminders by create (newest first)
        query.addDescendingOrder("createdAt");
        // start asynchronous call for reminders
        query.findInBackground(new FindCallback<Reminder>() {
            @Override
            public void done(List<Reminder> reminders, ParseException e) {
                // check for failure
                if (e != null) {
                    Log.e(TAG, "Failure to load reminders", e);
                    return;
                }
                // prints every reminder description for debugging purposes
                for (Reminder reminder : reminders){
                    Log.i(TAG, "Reminder: " + reminder.getReminder() + ", username: " + reminder.getUser().getUsername());
                }

                // save received comments to list and notify adapter of change
                allReminders.clear();
                allReminders.addAll(reminders);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void querySavedReminders(){
        ParseQuery<Reminder> query = ParseQuery.getQuery(Reminder.class);
        query.include(Reminder.KEY_USER);
        query.whereEqualTo(Reminder.KEY_USER, user);
        query.fromLocalDatastore();
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Reminder>() {
            @Override
            public void done(List<Reminder> reminders, ParseException e) {
                // check for failure
                if (e != null) {
                    Log.e(TAG, "Failure to load saved reminders", e);
                    return;
                }
                // prints every reminder description for debugging purposes
                for (Reminder reminder : reminders){
                    Log.i(TAG, "Reminder: " + reminder.getReminder() + ", username: " + reminder.getUser().getUsername());
                }
                // save received reminders to list and notify adapter of change
                allReminders.clear();
                allReminders.addAll(reminders);
                adapter.notifyDataSetChanged();
            }});
    }
}