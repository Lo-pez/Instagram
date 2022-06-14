package com.example.instagram.data.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_USER = "user";
    public static final String KEY_IMAGE = "image";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser parseUser) {
        put(KEY_USER, parseUser);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }
}


/*
For loading parse files use:

// getPhotoFileUri() is defined in
// https://guides.codepath.com/android/Accessing-the-Camera-and-Stored-Media#using-capture-intent
val photoFile = getPhotoFileUri(photoFileName)

val parseFile = ParseFile(photoFile)

<com.parse.ParseImageView
        android:layout_width="match_parent"
        android:layout_height="300dp"/>
 */