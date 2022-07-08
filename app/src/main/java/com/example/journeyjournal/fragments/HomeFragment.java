package com.example.journeyjournal.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.journeyjournal.Activities.RemindersActivity;
import com.example.journeyjournal.R;


public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    Button button;
    CalendarView calendar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button = view.findViewById(R.id.button);
        calendar = view.findViewById(R.id.calendar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goReminderActivity();
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

            }
        });
    }


    // takes to view current goals/ reminders page
    private void goReminderActivity() {
        Intent i = new Intent(getContext(), RemindersActivity.class);
        startActivity(i);
    }


}