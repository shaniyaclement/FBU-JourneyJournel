package com.example.journeyjournal.Activities;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.journeyjournal.Adapters.ReminderAdapter;
import com.example.journeyjournal.ParseConnectorFiles.Reminder;
import com.example.journeyjournal.ParseConnectorFiles.User;
import com.example.journeyjournal.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
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
    public static String locationName;
    private static String dateString;

    protected ReminderAdapter adapter;
    protected List<Reminder> allReminders;

    // XML elements
    RecyclerView rvReminders;
    TextView tvDone;
    TextView tvDateEntry;
    TextView tvLocation;
    EditText etRemind;
    EditText etNotes;

    // date picker variable
    Date date;

    // location variables
    ParseGeoPoint location;
    double longitude;
    double latitude;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;


    public User user = (User) ParseUser.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_reminder);

        // attached variables to XML elements
        tvDone = findViewById(R.id.tvDone);
        tvLocation = findViewById(R.id.tvLocation);
        etRemind = findViewById(R.id.etRemind);
        etNotes = findViewById(R.id.etNotes);
        rvReminders = findViewById(R.id.rvReminders);
        tvDateEntry = findViewById(R.id.tvDateEntry);

        // Initialize the SDK
        String apiKey = getString(R.string.MAPS_API_KEY);
        Places.initialize(getApplicationContext(), apiKey);


        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(ComposeReminder.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });


        // construct Material Date Picker
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Select Remind Date");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

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
                        date = new Date((Long) materialDatePicker.getSelection());
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
                location = new ParseGeoPoint(latitude, longitude);

                addReminder(remind, notes, user, date, location);
            }
        });
    }


    private void addReminder(String remind, String notes, ParseUser user, Date date, ParseGeoPoint location) {
        Reminder reminder = new Reminder();
        reminder.setReminder(remind);
        reminder.setNotes(notes);
        reminder.setUser(user);
        reminder.setLocationName(locationName);
        if(date!= null){reminder.setRemindDate(date);}
        if(location != null) {reminder.setLocation(location);}

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
                finish();
            }
        });

        Intent intent = new Intent(ComposeReminder.this, RemindersActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_out, R.anim.left_in);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = null;
                if (data != null) {
                    place = Autocomplete.getPlaceFromIntent(data);
                }
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());
                
                locationName = place.getName();

                final LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    longitude = latLng.longitude;
                    latitude = latLng.latitude;
                    tvLocation.setText(place.getName());
                } else{
                    Log.i(TAG, "latLng is null");
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = null;
                if (data != null) {
                    status = Autocomplete.getStatusFromIntent(data);
                }
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
