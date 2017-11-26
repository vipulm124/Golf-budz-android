package com.adcoretechnologies.golfbudz.core.base;

/**
 * Created by Irfan on 07/10/15.
 */
public class BoEventData {
    public static final int EVENT_POST_IMAGE_UPLOAD = 1;
    public static final int EVENT_POST_IMAGE_UPLOAD_REMOVE = 2;
    public static final int EVENT_EVENT_SERVICE_CLICK = 3;
    public static final int EVENT_EVENT_LIKE_CLICK = 4;
    public static final int EVENT_EVENT_SHARE_CLICK = 5;
    public static final int EVENT_EVENT_ATTENDING_CLICK = 6;
    public static final int EVENT_BUY_ITEM_CLICK = 7;
    public static final int EVENT_POST_VIDEO_UPLOAD = 8;

    public static final int EVENT_EVENT_FPROFILE_CLICK = 9;
    public static final int EVENT_EVENT_FUNFRIEND_CLICK = 10;
    public static final int EVENT_EVENT_FMESSAGECLICK = 11;
    public static final int EVENT_EVENT_FSENDREQ_CLICK = 12;

    public static final int EVENT_CLUB_JOIN_CLICK = 13;
    public static final int EVENT_BLOG_ITEM_CLICK = 14;


    public static final int EVENT_PLAY_REQ_CLICK = 15;
    public static final int EVENT_CHAT_ITEM_CLICK = 16;
    public static final int EVENT_NEWS_COMMENT_CLICK = 17;
    public static final int EVENT_NEWS_LIKE_CLICK = 18;
    public static final int EVENT_NEWS_SHARE_CLICK = 19;

    public static final int EVENT_CLUB_CLICK = 20;
    public static final int EVENT_FRIEND_ITEM_CLICK = 21;
    public static final int EVENT_NOTI_ACCEPT_CLICK = 22;
    public static final int EVENT_NOTI_CANCEL_CLICK = 23;
    public static final int EVENT_PLAY_REQ_SEND_CLICK = 24;
    public static final int EVENT_POST_IMAGES_CLICK = 25;
    public static final int EVENT_NEWS_FEED_USER_CLICK = 26;
    public static final int EVENT_PLAY_REQ_UPCOMING_CLICK = 27;
    public static final int EVENT_REQUEST_FILTER_APPLY = 28;
    public static final int EVENT_POST_IMAGE_UPLOAD_EDIT = 29;
    public static final int EVENT_POST_PARED_UP_SUCESS = 30;
    public final int eventType;

    public int getId() {
        return Id;
    }

    public String getData() {
        return data;
    }

    public Object getObject() {
        return object;
    }

    public int Id;
    public String data;
    public Object object;

    public BoEventData(int eventType, int Id) {
        this.eventType = eventType;
        this.Id = Id;
    }

    public BoEventData(int eventType, int Id, String data) {
        this.eventType = eventType;
        this.data = data;
        this.Id = Id;
    }
    public BoEventData(int eventType, int Id, Object object) {
        this.eventType = eventType;
        this.object = object;
        this.Id = Id;
    }
    public BoEventData(int eventType, int Id, String data, Object object) {
        this.eventType = eventType;
        this.Id = Id;
        this.data = data;
        this.object = object;
    }

}
