package com.adcoretechnologies.golfbudz.myapplication;

import android.app.Application;
import android.util.Log;

import com.adcoretechnologies.golfbudz.boxing_ui.impl.BoxingFrescoLoader;
import com.adcoretechnologies.golfbudz.boxing_ui.impl.BoxingUcrop;
import com.bilibili.boxing.BoxingCrop;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
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
        IBoxingMediaLoader loader = new BoxingFrescoLoader(this);

        BoxingMediaLoader.getInstance().init(loader);
        BoxingCrop.getInstance().init(new BoxingUcrop());

    }
}
