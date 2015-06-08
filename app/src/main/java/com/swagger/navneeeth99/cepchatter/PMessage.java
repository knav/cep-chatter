package com.swagger.navneeeth99.cepchatter;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by navneeeth99 on 17/5/15.
 */
@ParseClassName("PMessage")
public class PMessage extends ParseObject{
    public PMessage() {
        super();
    }

    public String getmSender() {
        return getString("from");
    }

    public void setmSender(String mSender) {
        put("from", mSender);
    }

    public Boolean getmRead(){
        return getBoolean("read");
    }

    public void setmRead(Boolean mRead){
        put("read", mRead);
    }

    public String getmReceiver() {
        return getString("to");
    }

    public void setmReceiver(String mReceiver) {
        put("to", mReceiver);
    }

    public String getmTitle() {
        return getString("title");
    }

    public void setmTitle(String mTitle) {
        put("title", mTitle);
    }

    public String getmContent() {
        return getString("content");
    }

    public void setmContent(String mContent) {
        put("content", mContent);
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile file) {
        put("photo", file);
    }

    @Override
    public String toString() {
        return getmTitle() + " - " + getmContent();
    }


}
