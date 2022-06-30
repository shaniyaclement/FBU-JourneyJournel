package com.example.journeyjournal.ParseConnectorFiles;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;


@ParseClassName("_User")
public class User extends ParseUser {

    public static final String KEY_PROFILE_PIC = "profilePic";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_BIO = "bio";

    public ParseFile getProfileImage() {
        return getParseFile(KEY_PROFILE_PIC);
    }

    public void setProfileImage(ParseFile profilePic) {
        put(KEY_PROFILE_PIC, profilePic);
    }

    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    public void setUsername(String username) {
        put(KEY_USERNAME, username);
    }

    public String getBio() {
        return getString(KEY_BIO);
    }

    public void setBio(String bio) {
        put(KEY_BIO, bio);
    }

}
