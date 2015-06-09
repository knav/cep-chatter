package com.swagger.navneeeth99.cepchatter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by navneeeth99 on 16/5/15.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate () {
        super.onCreate();
        //Enable Local Database.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(PMessage.class);
        ParseObject.registerSubclass(PostIt.class);
        Parse.initialize(this, "L7oqq5TcpOqhx2WNUvVMUzgBkjtY4QqLgkoWijI8", "eyPKMWCnfpDM90n8y46kI1A8eIk6G71ZcQQGXr7d");
    }

}
