package com.example.journeyjournal.ParseConnectorFiles;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public static final String KEY_AUTHOR = "commenter";
    public static final String KEY_BODY = "comment";
    public static final String KEY_POST = "post";
    public static final String KEY_CREATED_AT = "createdAt";

    public ParseUser getCommenter() {
        return (ParseUser) getParseObject(KEY_AUTHOR);
    }

    public void setCommenter(ParseUser user) {
        put(KEY_AUTHOR, user);
    }

    public String getComment() {
        return getString(KEY_BODY);
    }

    public void setComment(String body) {
        put(KEY_BODY, body);
    }

    public Post getPost() {
        return (Post) getParseObject(KEY_POST);
    }

    public void setPost(Post post) {
        put(KEY_POST, post);
    }

}
