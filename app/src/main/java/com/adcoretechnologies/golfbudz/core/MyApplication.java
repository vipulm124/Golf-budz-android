package com.adcoretechnologies.golfbudz.core;

import android.app.Application;

/**
 * Created by Rehan on 12/13/2016.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


}

