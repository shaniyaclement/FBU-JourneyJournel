package com.example.journeyjournal.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.journeyjournal.ParseConnectorFiles.Post;
import com.example.journeyjournal.R;
import com.parse.ParseFile;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.Date;

public class PostDetails extends AppCompatActivity {

    Post post;
    TextView tvDescription;
    TextView tvUsername;
    ImageView ivImage;
    TextView tvUsernameBottom;
    TextView tvCreatedAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        post = Parcels.unwrap(this.getIntent().getParcelableExtra("post"));
        tvDescription = findViewById(R.id.tvDescription);
        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvUsernameBottom = findViewById(R.id.tvUsernameBottom);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);


        tvDescription.setText(post.getDescription());
        tvUsername.setText(post.getUser().getUsername());
        tvUsernameBottom.setText(post.getUser().getUsername());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivImage);}

        // getting how long ago the post was made and updating textview
        Date createdAt = post.getCreatedAt();
        String timeAgo = Post.timeAgo(createdAt);
        tvCreatedAt.setText(timeAgo);

    }
}