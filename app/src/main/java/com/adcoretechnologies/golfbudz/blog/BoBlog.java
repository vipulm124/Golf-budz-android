package com.adcoretechnologies.golfbudz.blog;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Adcore on 2/4/2017.
 */

public class BoBlog implements Serializable{
    @SerializedName("userId")
    public String userId;
    @SerializedName("username")
    public String username;
    @SerializedName("title")
    public String title;
    @SerializedName("userImgUrl")
    public String userImgUrl;
    @SerializedName("text")
    public String text;
    @SerializedName("shortText")
    public String shortText;
    @SerializedName("createdAt")
    public String createdAt;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
