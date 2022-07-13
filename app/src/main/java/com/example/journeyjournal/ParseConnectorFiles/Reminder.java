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
    public static final String KEY_ID = "objectId";
    public static final String KEY_USER = "user";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_REMIND = "remind";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_DATE = "date";
    private static final String KEY_NAME = "locationName";

    public String getKeyId() {return getString(KEY_ID);}

    public void setKeyId(String id) { put(KEY_ID, id);}

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

    public String getLocationName() {
        return getString(KEY_NAME);
    }

    public void setLocationName(String locationName) {
        put(KEY_NAME, locationName);
    }

    public Date getRemindDate() {
        return getDate(KEY_DATE);
    }

    public void setRemindDate(Date date) {
        put(KEY_DATE, date);
    }

    public String getFormattedDate(Date date) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("MMM dd, yyyy");
        return simpleFormat.format(date);
    }

    public String getDateString(Reminder remind) {
        Date date = remind.getRemindDate();
        String string = getFormattedDate(date);
        return string;
    }

}
