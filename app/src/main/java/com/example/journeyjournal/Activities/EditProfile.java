package com.example.journeyjournal.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.journeyjournal.ParseConnectorFiles.User;
import com.example.journeyjournal.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;

public class EditProfile extends AppCompatActivity {
    private static final String TAG = "EditProfile";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    ImageView ivProfileImageEdit;
    TextView tvComplete;
    TextView tvCancel;
    TextView tvProfileImageChange;
    EditText etEditUsername;
    EditText etEditBio;

    private File photoFile;
    public String photoFileName = "photo.jpg";

    public User user = (User) ParseUser.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        tvComplete = findViewById(R.id.tvComplete);
        tvCancel = findViewById(R.id.tvCancel);
        tvProfileImageChange = findViewById(R.id.tvProfileImageChange);

        etEditUsername = findViewById(R.id.etEditUsername);
        etEditBio = findViewById(R.id.etEditBio);
        ivProfileImageEdit = findViewById(R.id.ivProfileImageEdit);

        //  populate iv with profile image
        User user = (User) ParseUser.getCurrentUser();
        ParseFile profileImage = user.getProfileImage();
        if (profileImage != null) {
            Glide.with(this).load(profileImage.getUrl())
                    .circleCrop()
                    .into(ivProfileImageEdit);}

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // change profile image
        tvProfileImageChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // trigger a pop up that asks take photo or upload
                launchCamera();
            }
        });

        // change user information in parse and on the profile page on click of done
        tvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(etEditUsername.getText().toString());
                user.saveInBackground();
                user.setBio(etEditBio.getText().toString());
                user.saveInBackground();
                etEditUsername.setText("");
                etEditBio.setText("");
                finish();
            }
        });
    }

    // launches implicit intent to open the phone camera and take the photo for the post
    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider, needed for URI >= 24
        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile);
        // make app a file provider
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
        Throwable e = null;
        if(e != null){
            Log.e(TAG, "Error launching camera", e);}
    }

    // adds image to imageView if photo is taken
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                Glide.with(this).load(takenImage).circleCrop().into(ivProfileImageEdit);

                ParseFile newPic = new ParseFile(photoFile);
                user.setProfileImage(newPic);
                user.saveInBackground();
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos using `getExternalFilesDir` on Context to access package-specific directories
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }
}