package com.adcoretechnologies.golfbudz.notification;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adcore on 2/6/2017.
 */

public class BoNoti {
    @SerializedName("_id")
    public String _id;
    @SerializedName("text")
    public String text;
    @SerializedName("userName")
    public String userName;

    @SerializedName("userImgUrl")
    public String userImgUrl;
    @SerializedName("title")
    public String title;
    @SerializedName("userId")
    public String userId;
    @SerializedName("friendId")
    public String friendId;
    @SerializedName("type")
    public String type;
    @SerializedName("requestId")
    public String requestId;
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
