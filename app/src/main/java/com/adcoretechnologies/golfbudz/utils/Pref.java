package com.adcoretechnologies.golfbudz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import com.adcoretechnologies.golfbudz.auth.BoUser;


public class Pref {
    SharedPreferences pref;
    Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;
    public static final String IS_FIRST_TIME = "isFirst";

    private static final String PREF_NAME = "kaamkaaj-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public static void clearOtherPref(Context context) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
    public Pref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
    // Int
    public static int ReadInt(Context context, final String key) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        return pref.getInt(key, 0);
    }

    public static void WriteInt(Context context, final String key,
                                final int value) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    // Boolean
    public static boolean ReadBoolean(Context context, final String key,
                                      final boolean defaultValue) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }
    public static void Write(Context context, final String key,
                             final String value) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static void WriteBoolean(Context context, final String key,
                                    final boolean value) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public static String Read(Context context, final String key) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        return pref.getString(key, "");
    }
    public static void saveUserDetail(Context context, BoUser user) {
        Pref.Write(context, Const.PREF_USER_EMAIL, user.getEmail());
        Pref.Write(context, Const.PREF_USER_ID, user.getUserId());
        Pref.Write(context, Const.PREF_USER_DISPLAY_NAME, user.getFirstName());
        Pref.Write(context, Const.PREF_USE_IMAGE_PATH, user.getProfileImage());
        Pref.Write(context, Const.PREF_USER_TYPE, user.getUserType());
    }

    public static BoUser getUserDetail(Context context) {
        BoUser user = new BoUser();
        user.setEmail(Pref.Read(context, Const.PREF_USER_PHONE));
        user.setUserId(Pref.Read(context, Const.PREF_USER_ID));

        return user;
    }

    public static boolean isLoggedIn(Context context) {
        String userId = Pref.Read(context, Const.PREF_USER_ID);
        return userId.isEmpty() ? false : true;
    }
}
