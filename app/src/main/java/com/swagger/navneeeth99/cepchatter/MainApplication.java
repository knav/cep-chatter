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
        Parse.initialize(this, "BaJWEoAN2k3vLhmkBWSBsJrVCdRMCKGC8Zre4UGt", "s5f56aG5af1L7IqZtP2KbkjVHzgHanKUwmZplnfI");
    }

}
