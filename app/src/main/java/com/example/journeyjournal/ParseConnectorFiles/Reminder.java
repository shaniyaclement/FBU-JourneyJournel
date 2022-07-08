package com.example.journeyjournal.ParseConnectorFiles;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Reminder")
public class Reminder extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_REMIND = "remind";

    public ParseUser getUser() {
        return (ParseUser) getParseObject(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getReminder() {
        return getString(KEY_REMIND);
    }

    public void setReminder(String body) {
        put(KEY_REMIND, body);
    }
}
