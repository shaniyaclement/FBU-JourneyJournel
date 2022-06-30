package com.example.journeyjournal.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.journeyjournal.ParseConnectorFiles.User;
import com.example.journeyjournal.Utilities.EndlessScrollListener;
import com.example.journeyjournal.ParseConnectorFiles.Post;
import com.example.journeyjournal.Adapters.ProfileAdapter;
import com.example.journeyjournal.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends HelperFragment {
    public static final String TAG = "ProfileFragment";
    private static final int RESULT_OK = -1;

    RecyclerView rvProfile;
    ProfileAdapter adapter;
    List<Post> allPosts;
    SwipeRefreshLayout swipeContainer;
    TextView tvNumPostsNum;
    TextView tvUsernameProfile;
    int numPostsByThisUser;
    ImageView ivProfileImageProfile;
    TextView tvBio;
    Button btnFollow;

    public User user = (User) ParseUser.getCurrentUser();

    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvProfile = view.findViewById(R.id.rvProfile);

        allPosts = new ArrayList<>();

        adapter = new ProfileAdapter(getContext(), allPosts);
        rvProfile.setAdapter(adapter);

        int numberOfColumns = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);

        swipeContainer = view.findViewById(R.id.swipeContainer);
        tvNumPostsNum = view.findViewById(R.id.tvNumPostsNum);
        ivProfileImageProfile = view.findViewById(R.id.ivProfileImageProfile);
        tvUsernameProfile = view.findViewById(R.id.tvUsernameProfile);
        tvBio = view.findViewById(R.id.tvBio);
        btnFollow = view.findViewById(R.id.btnFollow);

        rvProfile.setLayoutManager(gridLayoutManager);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts(0);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        queryPosts(0);

        ivProfileImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        displayUserInfo();

    }

    private void displayUserInfo() {
        tvUsernameProfile.setText(user.getUsername());
        tvBio.setText(user.getBio());

        ParseFile profileImage = user.getProfileImage();
        if (profileImage != null) {
            Glide.with(this).load(profileImage.getUrl())
                    .circleCrop()
                    .into(ivProfileImageProfile);}

        if (user != ParseUser.getCurrentUser()) {
            btnFollow.setVisibility(View.VISIBLE);
        } else {
            btnFollow.setVisibility(View.GONE);
        }
    }

    protected void queryPosts(int i) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);

        query.setLimit(20);
        query.setSkip(i);

        query.addDescendingOrder(Post.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                swipeContainer.setRefreshing(false);

                if (e != null) {
                    Log.e(TAG, "Issue getting posts.", e);
                    return;
                }

                // at this point, we have gotten the posts successfully
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                numPostsByThisUser = posts.size();
                tvNumPostsNum.setText(String.valueOf(numPostsByThisUser));

                allPosts.clear();
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                Glide.with(this).load(takenImage).circleCrop().into(ivProfileImageProfile);

                ParseFile newPic = new ParseFile(photoFile);
                user.setProfileImage(newPic);
                user.saveInBackground();

            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}