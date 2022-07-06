package com.example.journeyjournal.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.journeyjournal.ParseConnectorFiles.Comment;
import com.example.journeyjournal.ParseConnectorFiles.Post;
import com.example.journeyjournal.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class CommentActivity extends AppCompatActivity {

    public static final String TAG = "CommentActivity";

    Post post;
    TextView tvComment;
    EditText etComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        tvComment = findViewById(R.id.tvComment);
        etComment = findViewById(R.id.etComment);

        // stores post comment was made on
        post = getIntent().getParcelableExtra("post");

        // click comment button --> comment compose
        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // stores text from editText
                String body = etComment.getText().toString();

                // constructing new Comment
                Comment commentAdd = new Comment();
                commentAdd.setComment(body);
                commentAdd.setPost(post);
                commentAdd.setCommenter(ParseUser.getCurrentUser());

                // add to Parse
                commentAdd.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error adding comment", e);
                            return;
                        }
//                        // go back to feed
//                        finish();
                    }
                });
            }
        });
    }
}