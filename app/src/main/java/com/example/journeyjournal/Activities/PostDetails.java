package com.example.journeyjournal.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.example.journeyjournal.Adapters.PostsAdapter;
import com.example.journeyjournal.ParseConnectorFiles.Post;
import com.example.journeyjournal.ParseConnectorFiles.User;
import com.example.journeyjournal.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PostDetails extends AppCompatActivity {
    private static final String TAG = "PostDetails";
    private SwipeRefreshLayout swipeContainer;
    RecyclerView rvProfile;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    public User user = (User) ParseUser.getCurrentUser();
    // Your code to refresh the list here.
// Make sure you call swipeContainer.setRefreshing(false)
// once the network request has completed successfully.



    @Override
    public void onResume() {
        super.onResume();
        // query posts from the database
        Log.i(TAG, "onResume");
        adapter.clear();
        queryPosts(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.click_profile);

        rvProfile = findViewById(R.id.rvProfile);
        // initialize the array that will hold posts and create a PostsAdapter
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(this, allPosts);
        // set the adapter on the recycler view
        rvProfile.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvProfile.setLayoutManager(new LinearLayoutManager(this));
        queryPosts(0);


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void queryPosts(int i) {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        // query for only the current user
        query.whereEqualTo(Post.KEY_USER, user);

        query.setLimit(20);
        // how many posts to skip before getting to current
        query.setSkip(i);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                // save received posts to list and notify adapter of new data
                allPosts.clear();
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }
}