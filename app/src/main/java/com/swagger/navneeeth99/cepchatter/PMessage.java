package com.swagger.navneeeth99.cepchatter;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

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

    public String getmRead(){
        return getString("read");
    }

    public void setmRead(String mRead){
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

    @Override
    public String toString() {
        return getmTitle() + " - " + getmContent();
    }
}
