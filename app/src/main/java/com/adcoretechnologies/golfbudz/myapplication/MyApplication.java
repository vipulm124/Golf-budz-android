package com.adcoretechnologies.golfbudz.myapplication;

import android.app.Application;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

/**
 * Created by Adcore Tech on 9/8/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        Twitter.initialize(this);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("pWR4FbMTekDFxmeLjIwQOl98G", "IId80LVZ9DejYZzfzaWl7pVhUUYXr6CWKIGyxmdQpXI4xwIfQ6"))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }
}
