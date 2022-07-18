package com.example.journeyjournal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journeyjournal.ParseConnectorFiles.Reminder;
import com.example.journeyjournal.ParseConnectorFiles.User;
import com.example.journeyjournal.R;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private static Context context;
    private final List<Reminder> reminders;

    public User user = (User) ParseUser.getCurrentUser();

    public ReminderAdapter(Context context, List<Reminder> reminders){
        this.context = context;
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating the reminder XML layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);
        holder.bind(reminder);
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public void clear() {
        reminders.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // add XML element
        public TextView tvRemind;
        public TextView tvNotes;
        public TextView tvDate;
        public TextView tvLocationItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initialize element
            tvRemind = (TextView) itemView.findViewById(R.id.tvRemind);
            tvNotes = (TextView) itemView.findViewById(R.id.tvNotes);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvLocationItem = (TextView) itemView.findViewById(R.id.tvLocationItem);
        }

        public void bind(Reminder reminder) {
            // Bind the reminder data to the view element
            tvRemind.setText(reminder.getReminder());
            tvNotes.setText(reminder.getNotes());
            Date remindDate = reminder.getRemindDate();
            ParseGeoPoint location = reminder.getLocation();
            if (remindDate != null){
                tvDate.setText(reminder.getDateString(reminder));
            } else{
                tvDate.setText("");
                Log.i("remind", "date null");

            }
            if (location != null){
                tvLocationItem.setText(reminder.getLocationName());
            } else{
                tvLocationItem.setText("");
                Log.i("remind", "location null");
            }

            tvLocationItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // intent for launching Google Maps
                    if(location != null){
                        String latitude = Double.toString(location.getLatitude());
                        String longitude = Double.toString(location.getLongitude());
                        String stringParse = "google.navigation:q=" + latitude + "," + longitude;
                        Uri gmmIntentUri = Uri.parse(stringParse);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                            v.getContext().startActivity(mapIntent);
                        }
                    }
                }
            });
        }
    }
}
