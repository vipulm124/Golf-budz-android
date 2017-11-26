package com.adcoretechnologies.golfbudz;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.adcoretechnologies.golfbudz.boxing_ui.impl.BoxingFrescoLoader;
import com.adcoretechnologies.golfbudz.boxing_ui.impl.BoxingUcrop;
import com.bilibili.boxing.BoxingCrop;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by admin on 26/5/2017.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private GoogleApiClient mGoogleApiClient;
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
    private GoogleSignInOptions gso;
    public AppCompatActivity activity;
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        MultiDex.install(this);
        mInstance = this;
        IBoxingMediaLoader loader = new BoxingFrescoLoader(this);

        BoxingMediaLoader.getInstance().init(loader);
        BoxingCrop.getInstance().init(new BoxingUcrop());

    }



    @Override
    protected void attachBaseContext(Context newBase) {
        // MultiDex.install(newBase);
        super.attachBaseContext(newBase);
    }

    public GoogleSignInOptions getGoogleSignInOptions(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        return gso;
    }
    public GoogleApiClient getGoogleApiClient(AppCompatActivity activity, GoogleApiClient.OnConnectionFailedListener listener){
        this.activity = activity;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this.activity, listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, getGoogleSignInOptions())
                .build();
        return mGoogleApiClient;
    }
}

