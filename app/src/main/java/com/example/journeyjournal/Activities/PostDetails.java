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

public class PostDetails extends AppCompatActivity {

    Post post;
    TextView tvBody;
    TextView tvUser;
    ImageView ivImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        tvBody = findViewById(R.id.tvBody);
        tvUser = findViewById(R.id.tvUser);
        ivImg = findViewById(R.id.ivImg);

        tvBody.setText(post.getDescription());
        tvUser.setText(post.getUser().getUsername());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivImg);}

    }
}