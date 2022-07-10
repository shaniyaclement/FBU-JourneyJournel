package com.example.journeyjournal.ParseConnectorFiles;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

@ParseClassName("Reminder")
public class Reminder extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_REMIND = "remind";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_DATE = "date";

    public ParseUser getUser() {
        return (ParseUser) getParseObject(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getReminder() {
        return getString(KEY_REMIND);
    }

    public void setReminder(String remind) {
        put(KEY_REMIND, remind);
    }

    public String getNotes() {return getString(KEY_NOTES);}

    public void setNotes(String notes) { put(KEY_NOTES, notes);}

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(KEY_LOCATION);
    }

    public void setLocation(ParseGeoPoint location) {
        put(KEY_LOCATION, location);
    }

    public Date getRemindDate() {
        return getDate(KEY_DATE);
    }

    public void setRemindDate(Date date) {
        put(KEY_DATE, date);
    }

    public String getFormattedDate(Date date) {
        Format format = new SimpleDateFormat("mm/dd/yyyy");
        return format.format(date);
    }
}
