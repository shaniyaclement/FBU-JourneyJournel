package com.example.journeyjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;


import org.parceler.Parcels;

public class JournalDetails extends AppCompatActivity {

    Journals journals;
    EditText etTitle;
    TextView etEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_details);

        journals = Parcels.unwrap(getIntent().getParcelableExtra("journals"));
        etEntry = findViewById(R.id.etEntry);
        etTitle = findViewById(R.id.etTitle);

        etTitle.setText(journals.getTitle());
        etEntry.setText(journals.getEntry());

    }
}