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
import com.example.journeyjournal.Activities.EditProfile;
import com.example.journeyjournal.ParseConnectorFiles.User;
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

@SuppressWarnings("deprecation")
public class ProfileFragment extends HelperFragment {
    public static final String TAG = "ProfileFragment";
    private static final int RESULT_OK = -1;

    RecyclerView rvProfile;
    ProfileAdapter adapter;
    List<Post> allPosts;
    SwipeRefreshLayout swipeContainer;
    TextView tvPostsNum;
    TextView tvProfileUsername;
    int numPostsByThisUser;
    ImageView ivProfileImageProfile;
    TextView tvBio;
    Button btnFollow;
    Button btnEditProfile;

    public User user = (User) ParseUser.getCurrentUser();
    int numsFollowers;
    int numsFollowing;
    TextView tvFollowersNum;
    TextView tvFollowingNum;


    public ProfileFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        // query posts from the database
        Log.i(TAG, "onResume");
        tvProfileUsername.setText("");
        tvBio.setText("");
        displayUserInfo();
    }

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
        tvPostsNum = view.findViewById(R.id.tvPostsNum);
        tvFollowersNum = view.findViewById(R.id.tvFollowersNum);
        tvFollowingNum = view.findViewById(R.id.tvFollowingNum);
        ivProfileImageProfile = view.findViewById(R.id.ivProfileImageProfile);
        tvProfileUsername = view.findViewById(R.id.tvProfileUsername);
        tvBio = view.findViewById(R.id.tvBio);
        btnFollow = view.findViewById(R.id.btnFollow);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                startActivity(intent);
            }
        });

        rvProfile.setLayoutManager(gridLayoutManager);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        queryPosts();

        ivProfileImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        displayUserInfo();

    }

    private void displayUserInfo() {
        tvProfileUsername.setText(user.getUsername());
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
            btnEditProfile.setVisibility(View.VISIBLE);
        }
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);

        query.setLimit(20);
        query.setSkip(0);

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

                // sets Number of posts
                numPostsByThisUser = posts.size();
                tvPostsNum.setText(String.valueOf(numPostsByThisUser));

                //sets number of followers
                numsFollowers = posts.size();
                tvFollowersNum.setText(String.valueOf(numsFollowers));

                //sets number of following
                numsFollowing = posts.size();
                tvFollowingNum.setText(String.valueOf(numsFollowing));

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

                // Add new profile image in parse
                ParseFile newPic = new ParseFile(photoFile);
                user.setProfileImage(newPic);
                user.saveInBackground();

            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}