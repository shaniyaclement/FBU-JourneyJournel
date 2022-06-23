package com.example.journeyjournal;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

//@Parcel
@ParseClassName("Journals")
public class Journals extends ParseObject {


    public static final String KEY_TITLE = "title";
    public static final String KEY_ENTRY = "entry";
    public static final String KEY_USER = "user";


    public Journals(){}

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public String getEntry() {
        return getString(KEY_ENTRY);
    }

    public void setEntry(String entry) {
        put(KEY_ENTRY, entry);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
}
