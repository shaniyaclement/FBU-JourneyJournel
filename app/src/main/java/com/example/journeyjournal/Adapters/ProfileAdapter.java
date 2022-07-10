package com.example.journeyjournal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journeyjournal.Activities.PostDetails;
import com.example.journeyjournal.R;
import com.example.journeyjournal.ParseConnectorFiles.Post;
import com.parse.ParseFile;

import java.util.List;

// used to add posts to the rv in the profile page
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private final Context context;
    private final List<Post> posts;

    public ProfileAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }


    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.posts_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView ivUserPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserPost = itemView.findViewById(R.id.ivUserPost);

            ivUserPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostDetails.class);
                    context.startActivity(intent);
                }
            });
        }

        public void bind(Post post) {

            ParseFile image = post.getImage();

            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivUserPost);
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);

                Intent intent = new Intent(context, PostDetails.class);

                intent.putExtra("post", post);

                context.startActivity(intent);
            }
        }
    }
}