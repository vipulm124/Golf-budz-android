package com.adcoretechnologies.golfbudz.utils;

/**
 * Created by Gaurav on 2/1/2016.
 */
public class Const {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String FIREBASE_STORAGE_BUCKET_PATH = "gs://golfbudz-a605d.appspot.com";//"gs://golfbudz-a605d.appspot.com";
    public static final String PREF_IS_INTRO_DONE = "isIntroDone";
    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_FAILED = 100;
    public static final int STATUS_ERROR = 300;
    public static final String PREF_USER_DEVICE_TOKEN = "prefToken";
    public static final String PREF_USER_ID = "prefuserId";
    public static final String PREF_USER_DISPLAY_NAME = "prefName";
    public static final String PREF_USER_TYPE= "prefUserType";
    public static final String PREF_USER_LNAME = "prefLName";
    public static final String PREF_USER_EMAIL = "prefEmail";
    public static final String PREF_USER_PHONE = "prefContactNo";
    public static final String PREF_USE_IMAGE_PATH = "imagePath";
    public static final String DEBUG_TAG = "golf";
    public static final String DEVICE_TYPE = "android";
    public static final String EXTRA_BLOG_ID = "blogId";
    public static final String EXTRA_REQ_ID = "playReqId";
    public static final String EXTRA_USER_ID = "userId";
    public static final String EXTRA_POST_ID = "postId";
    public static final String EXTRA_POST = "post";
    public static final String USER = "user";
    public static final String FROM = "from";

    public static boolean IS_TEST = false;
    public static boolean IS_PRODUCTION=false;
    public static String API_BASE_URL_DEV="http://golfingbudzapi-env.5havpaustr.us-east-2.elasticbeanstalk.com/api/";//https://golfbudz-api.herokuapp.com/api/";
    public static final String API_BASE_URL_PROD = "http://golfingbudzapi-env.5havpaustr.us-east-2.elasticbeanstalk.com/api/";//https://golfbudz-staging.herokuapp.com/api/
    public static String API_BASE_URL_DEV_CHANGE="https://www.adcoretechnologies.com/golf-admin/api/";//https://golfbudz-api.herokuapp.com/api/";
    public static final String API_BASE_URL_PROD_CHANGE = "https://www.adcoretechnologies.com/golf-admin/api/";//https://golfbudz-staging.herokuapp.com/api/

    public static final String DUMMYID = "45a4a2ac-a0e6-11e6-b706-90b11c086519";
    public static final String EXTRA_EVENT_ID = "evenId";
    public static final String UNFRIEND = "unfriend";
    public static final String BLOCK = "block";
    public static final String PENDING = "pending";
    public static final String SENT = "sent";
    public static final String ACCEPT = "accept";
    public static final String CANCEL = "cancel";
    public static final String UNBLOCK = "unblock";
    public static final String IMAGE = "image";
    public static final String VIDEO = "video";
    public static final String TEXT = "text";
    public static final String  ADD_FRIENDS= "addFriend";
    public static final String EXTRA_CHAT_WITH = "chatWith";
    public static final String EXTRA_CHANNEL_ID = "channelId";
    public static final String EXTRA_FRIEND_ID = "friendId";
    public static final String EXTRA_CHATWITH_ID = "chatwithId";
    public static final String FIREBASE_DB_CONVERSATIONS = "chat";
    public static final String FIREBASE_DB_CHANNELS = "channel";
    public static final String EXTRA_IMAGE_URL = "image";
    public static final String EXTRA_CLUB_DETAIL = "club_detail";
    public static final String EXTRA_FRAGMENT_DISPLAY_COUNT = "fragmentDisplayCount";
    public static String PROJECT_FOLDER = "GolfBudz";
    public static final String PROJECT_IMAGES = "IMAGE";
    public static String FILE_PROVIDER_SUFFIX = ".file.provider";
}
