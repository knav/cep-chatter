package com.swagger.navneeeth99.cepchatter;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by navneeeth99 on 8/6/15.
 */
@ParseClassName("PostIt")
public class PostIt extends ParseObject {
    public PostIt() {
        super();
    }

    public String getmPoster() {
        return getString("poster");
    }

    public void setmPoster(String mPoster) {
        put("poster", mPoster);
    }

    public String getmHeader() {
        return getString("header");
    }

    public void setmHeader(String mHeader) {
        put("header", mHeader);
    }

    public String getmDescription() {
        return getString("desc");
    }

    public void setmDescription(String mDescription) {
        put("desc", mDescription);
    }

    public List<ParseUser> getmLikedBy() {
        return getList("likes");
    }

    public void setmLikedBy(List<ParseUser> mLikedUsers) {
        put("likes", mLikedUsers);
    }

    public void addmLikedBy(ParseUser mLikedUser) {
        add("likes", mLikedUser);
    }

    @Override
    public String toString() {
        return getmHeader() + " - " + getmDescription() + "—> Post-It from: " + getmPoster();
    }
}
