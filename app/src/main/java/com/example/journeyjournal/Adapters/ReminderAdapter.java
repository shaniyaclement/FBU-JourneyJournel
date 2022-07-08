package com.example.journeyjournal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journeyjournal.ParseConnectorFiles.Reminder;
import com.example.journeyjournal.ParseConnectorFiles.User;
import com.example.journeyjournal.R;
import com.parse.ParseUser;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private final Context context;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initialize element
            tvRemind = (TextView) itemView.findViewById(R.id.tvRemind);
        }

        public void bind(Reminder reminder) {
            // Bind the reminder data to the view element
            tvRemind.setText(reminder.getReminder());
        }
    }
}
