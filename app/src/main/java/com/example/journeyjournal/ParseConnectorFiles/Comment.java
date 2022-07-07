package com.example.journeyjournal.ParseConnectorFiles;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public static final String KEY_COMMENTER = "commenter";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_POST = "post";

    public ParseUser getCommenter() {
        return (ParseUser) getParseObject(KEY_COMMENTER);
    }

    public void setCommenter(ParseUser user) {
        put(KEY_COMMENTER, user);
    }

    public String getComment() {
        return getString(KEY_COMMENT);
    }

    public void setComment(String body) {
        put(KEY_COMMENT, body);
    }

    public Post getPost() {
        return (Post) getParseObject(KEY_POST);
    }

    public void setPost(Post post) {
        put(KEY_POST, post);
    }

}
