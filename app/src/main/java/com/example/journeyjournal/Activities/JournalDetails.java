package com.example.journeyjournal.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;


import com.example.journeyjournal.ParseConnectorFiles.Journals;
import com.example.journeyjournal.R;

import org.parceler.Parcels;

public class JournalDetails extends AppCompatActivity {

    Journals journals;
    TextView tvTitle;
    TextView tvEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_details);

        journals = Parcels.unwrap(getIntent().getParcelableExtra("journals"));
        tvEntry = findViewById(R.id.tvEntry);
        tvTitle = findViewById(R.id.tvTitle);

        tvTitle.setText(journals.getTitle());
        tvEntry.setText(journals.getEntry());

    }
}