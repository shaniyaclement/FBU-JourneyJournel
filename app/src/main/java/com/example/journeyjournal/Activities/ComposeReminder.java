package com.example.journeyjournal.Activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.journeyjournal.Adapters.ReminderAdapter;
import com.example.journeyjournal.ParseConnectorFiles.Reminder;
import com.example.journeyjournal.ParseConnectorFiles.User;
import com.example.journeyjournal.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.parse.FindCallback;
import com.parse.ParseException;
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
    TextView tvDone;
    TextView tvDateEntry;
    TextView tvLocation;
    EditText etRemind;
    EditText etNotes;
    EditText etLocation;

    // date picker variable
    Date date;

    private static int AUTOCOMPLETE_REQUEST_CODE = 1;


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
        etLocation = findViewById(R.id.etLocation);
        rvReminders = findViewById(R.id.rvReminders);
        tvDateEntry = findViewById(R.id.tvDateEntry);

        // Initialize the SDK
        String apiKey = getString(R.string.MAPS_API_KEY);
        Places.initialize(getApplicationContext(), apiKey);

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);


        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

//        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
//        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
//                new LatLng(-33.880490, 151.18363),
//                new LatLng(-33.858754, 151.229596)));
        autocompleteFragment.setCountries("USA");

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                etLocation.setText(place.getAddress());
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
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
