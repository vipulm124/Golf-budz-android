package com.adcoretechnologies.golfbudz.home.comment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Adcore on 2/16/2017.
 */

public class BoCommnet implements Serializable {
    @SerializedName("userId")
    public String userId;
    @SerializedName("userName")
    public String userName;
    @SerializedName("postId")
    public String postId;
    @SerializedName("comment")
    public String comment;
    @SerializedName("userImgUrl")
    String userImgUrl;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }
}
