package com.adcoretechnologies.golfbudz;

/**
 * Created by seocor1 on 9/14/2016.
 */

import android.util.Log;


import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Belal on 5/27/2016.
 */


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    String refreshedToken;

    @Override
    public void onTokenRefresh() {

        //Getting registration token
         refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
         saveDeviceToken(refreshedToken);
    }

    private void saveDeviceToken(String refreshedToken) {
        Pref.Write(this, Const.PREF_USER_DEVICE_TOKEN, refreshedToken);
    }


}