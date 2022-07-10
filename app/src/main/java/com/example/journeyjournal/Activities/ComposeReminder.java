package com.example.journeyjournal.Activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.journeyjournal.Adapters.ReminderAdapter;
import com.example.journeyjournal.ParseConnectorFiles.Reminder;
import com.example.journeyjournal.ParseConnectorFiles.User;
import com.example.journeyjournal.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ComposeReminder extends AppCompatActivity {

    private static final String TAG = "ComposeReminder";

    protected ReminderAdapter adapter;
    protected List<Reminder> allReminders;

    // XML elements
    RecyclerView rvReminders;
    LinearLayout datePick;
    TextView tvDone;
    EditText etRemind;
    EditText etNotes;
    ImageView ivLocation;
    Button btnDate;
    TextView tvDateEntry;
    Date date;

    public User user = (User) ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_reminder);

        // attached variables to XML elements
        datePick = findViewById(R.id.datePick);
        ivLocation = findViewById(R.id.ivLocation);
        tvDone = findViewById(R.id.tvDone);
        etRemind = findViewById(R.id.etRemind);
        etNotes = findViewById(R.id.etNotes);
        rvReminders = findViewById(R.id.rvReminders);
        btnDate = findViewById(R.id.btnDate);
        tvDateEntry = findViewById(R.id.tvDateEntry);

        String apiKey = "";
        // Initialize the SDK
        Places.initialize(getApplicationContext(), apiKey);

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);


        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        // construct Material Date Picker
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Select Remind Date");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        // show date picker when Layout  is clicked
        datePick.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to interact with the fragments associated with the material design date picker tag is to get any error in logcat
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });
        // show date picker when Layout  is clicked
        tvDateEntry.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to interact with the fragments associated with the material design date picker tag is to get any error in logcat
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        // now handle the positive button click from the
        // material design date picker
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        // save selected date to date variable
                        Date selectedDate = (Date) new Date((Long) materialDatePicker.getSelection());
                        date = selectedDate;
                        // format the dates in simple format
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("MMM dd, yyyy");
                        // display it with setText
                        tvDateEntry.setText("Remind me on: " + simpleFormat.format(date));}
                });



        allReminders = new ArrayList<>();
        adapter = new ReminderAdapter(this, allReminders);

        // set the adapter on the RV
        rvReminders.setAdapter(adapter);
        // set the layout manager on RV
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvReminders.setLayoutManager(linearLayoutManager);
        //query comments from Parse
        queryReminders();

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remind = etRemind.getText().toString();
                String notes = etNotes.getText().toString();
//                ParseGeoPoint location = new ParseGeoPoint(latitude, longitude);

                addReminder(remind, notes, user, date);
            }
        });
    }

    private void addReminder(String remind, String notes, ParseUser user, Date date) {
        Reminder reminder = new Reminder();
        reminder.setReminder(remind);
        reminder.setNotes(notes);
        reminder.setUser(user);
        reminder.setRemindDate(date);
//        reminder.setLocation(location);

        reminder.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error adding reminder", e);
                    return;
                }
                etRemind.setText("");
                etNotes.setText("");
                tvDateEntry.setText("");
                queryReminders();
            }
        });
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
}