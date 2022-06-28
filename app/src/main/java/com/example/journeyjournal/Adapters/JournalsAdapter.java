package com.example.journeyjournal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journeyjournal.ParseConnectorFiles.Journals;
import com.example.journeyjournal.Activities.JournalDetails;
import com.example.journeyjournal.R;


import org.parceler.Parcels;

import java.util.List;

public class JournalsAdapter extends RecyclerView.Adapter<JournalsAdapter.ViewHolder> {

    private Context context;
    private List<Journals> journal;

    public JournalsAdapter(Context context, List<Journals> journal) {
        this.context = context;
        this.journal = journal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_journal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Journals journals = journal.get(position);
        holder.bind(journals);
    }

    @Override
    public int getItemCount() {
        return journal.size();
    }
//
//    // Clean all elements of the recycler
    public void clear() {
        journal.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Journals> posts) {
        journal.addAll(journal);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvEntry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvEntry = itemView.findViewById(R.id.tvEntry);
        }

        public void bind(Journals journals) {
            // Bind the post data to the view elements
            tvTitle.setText(journals.getTitle());
            tvEntry.setText(journals.getEntry());

            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, JournalDetails.class);
                    i.putExtra("journals", Parcels.wrap(journals));
                    context.startActivity(i);
                }
            });
        }
    }
}
