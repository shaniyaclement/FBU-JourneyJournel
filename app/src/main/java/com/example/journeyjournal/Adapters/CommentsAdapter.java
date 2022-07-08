package com.example.journeyjournal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journeyjournal.Activities.MainActivity;
import com.example.journeyjournal.ParseConnectorFiles.User;
import com.example.journeyjournal.R;
import com.example.journeyjournal.ParseConnectorFiles.Comment;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

// add comments to the RV
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private Context context;
    private List<Comment> comments;

    public User user = (User) ParseUser.getCurrentUser();

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating the comment XML layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // add xml elements
        public TextView tvUsername;
        public TextView tvComment;
        public ImageView ivProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializes elements
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvComment = (TextView) itemView.findViewById(R.id.tvPost);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
        }

        public void bind(Comment comment) {
            // Bind the comment data to the view elements
            tvComment.setText(comment.getComment());
            tvUsername.setText(comment.getCommenter().getUsername());

            // display profile picture on comment in RecyclerView
            ParseFile profileImage = user.getProfileImage();
            if (profileImage != null) {
                Glide.with(context).load(profileImage.getUrl()).circleCrop().into(ivProfileImage);}

            // clicking on profile pic --> profile page
            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity) context;
                    activity.goToProfileFragment(comment.getCommenter());
                }
            });
        }
    }

}
