package com.example.journeyjournal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journeyjournal.Activities.CommentActivity;
import com.example.journeyjournal.Activities.MainActivity;
import com.example.journeyjournal.ParseConnectorFiles.Post;
import com.example.journeyjournal.Activities.PostDetails;
import com.example.journeyjournal.ParseConnectorFiles.User;
import com.parse.ParseFile;
import com.example.journeyjournal.R;
import com.parse.ParseUser;


import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

//    public User user = (User) ParseUser.getCurrentUser();
    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> posts) {
        posts.addAll(posts);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvUsernameBottom;
        private TextView tvCreatedAt;
        private ImageView ivProfileImage;
        private ImageButton ibComment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initialize elements
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvUsernameBottom = itemView.findViewById(R.id.tvUsernameBottom);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            ibComment = itemView.findViewById(R.id.ibComment);

        }

        public void bind(Post post) {
            // Bind the post data to the view elements
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvUsernameBottom.setText(post.getUser().getUsername());

            Date createdAt = post.getCreatedAt();
            String duration = Post.timeAgo(createdAt);
            tvCreatedAt.setText(duration);

            ParseFile image = post.getImage();

            // loads image into ImageView for post
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
                // click image and it navigates to post details
//                ivImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent i = new Intent(context, PostDetails.class);
//                        i.putExtra("post", Parcels.wrap(post));
//                        context.startActivity(i);
//                    }
                //});
            }

            // display profile picture on post in RecyclerView
            User user = (User) post.getUser();
            ParseFile profileImage = user.getProfileImage();
            if (profileImage != null) {
                Glide.with(context).load(profileImage.getUrl()).circleCrop().into(ivProfileImage);}

            // clicking on profile pic --> profile page
            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity) context;
                    activity.goToProfileFragment(post.getUser());
                }
            });

            // clicking comment button --> comment page
            ibComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // move from here to the comment compose page
                    Intent intent = new Intent(context, CommentActivity.class);
                    // include the post we are moving from details
                    intent.putExtra("post", post);
                    context.startActivity(intent);
                }
            });
        }
    }
}


