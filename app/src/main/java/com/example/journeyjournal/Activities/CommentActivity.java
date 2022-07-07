package com.example.journeyjournal.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journeyjournal.Adapters.CommentsAdapter;
import com.example.journeyjournal.ParseConnectorFiles.Comment;
import com.example.journeyjournal.ParseConnectorFiles.Post;
import com.example.journeyjournal.ParseConnectorFiles.User;
import com.example.journeyjournal.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

//
public class CommentActivity extends AppCompatActivity {

    public static final String TAG = "CommentActivity";
    private RecyclerView rvComments;
    protected CommentsAdapter adapter;
    protected List<Comment> allComments;

    public User user = (User) ParseUser.getCurrentUser();
    Post post;
    TextView tvPost;
    EditText etComment;
    ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        rvComments = findViewById(R.id.rvComments);

        allComments = new ArrayList<>();
        adapter = new CommentsAdapter(this, allComments);

        tvPost = findViewById(R.id.tvPost);
        etComment = findViewById(R.id.etComment);
        ivProfileImage = findViewById(R.id.ivProfileImage);

        // set the adapter on the RV
        rvComments.setAdapter(adapter);
        // set the layout manager on RV
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        //query comments from Parse
        queryComments();

        // stores post comment was made on
        post = getIntent().getParcelableExtra("post");

        // click post button --> comment compose
        tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // constructing new Comment
                Comment comment = new Comment();
                comment.setComment(etComment.getText().toString());
                comment.setPost(post);
                comment.setCommenter(ParseUser.getCurrentUser());

                ParseFile profileImage = user.getProfileImage();
                if (profileImage != null) {
                    Glide.with(CommentActivity.this).load(profileImage.getUrl()).circleCrop().into(ivProfileImage);}

                // add to Parse
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error adding comment", e);
                            return;
                        }
                        etComment.setText("");


                    }
                });
            }
        });
    }

    private void queryComments() {
        // specify type of data to query - Comments.class
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        // include data referred by user key
        query.include(Comment.KEY_COMMENTER);
        // limit query to latest 20 comments
        query.setLimit(20);
        // order comments by create (newest first)
        query.addDescendingOrder("createdAt");
        // start asynchronous call for comments
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                // check for failure
                if (e != null) {
                    Log.e(TAG, "Failure to load comments", e);
                    return;
                }
                // prints every comment description for debugging purposes
                for (Comment comment : comments) {
                    Log.i(TAG, "Comment: " + comment.getComment() + ", username: " + comment.getCommenter().getUsername());
                }

                // save received comments to list and notify adapter of change
                allComments.addAll(comments);
                adapter.notifyDataSetChanged();
            }
        });
    }
}