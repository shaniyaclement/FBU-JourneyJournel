package com.example.journeyjournal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.parceler.Parcels;


import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Journals> journal;

    public PostsAdapter(Context context, List<Journals> journal) {
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

        private EditText etTitle;
        private TextView tvJEntry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etTitle = itemView.findViewById(R.id.etTitle);
            tvJEntry = itemView.findViewById(R.id.tvJEntry);
        }

        public void bind(Journals journals) {
            // Bind the post data to the view elements
            etTitle.setText(journals.getTitle());
            tvJEntry.setText(journals.getEntry());

            etTitle.setOnClickListener(new View.OnClickListener() {
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
