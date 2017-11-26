package com.adcoretechnologies.golfbudz.blog;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by admin on 10/25/2017.
 */

public class PojoBlogs {

    @SerializedName("text")
    public String text;

    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;
    @SerializedName("image")
    public String image;
    @SerializedName("postType")
    public String postType;
    @SerializedName("userImgUrl")
    public String userImgUrl;
    @SerializedName("updatedAt")
    public String updatedAt;
    @SerializedName("userName")
    public String userName;
    @SerializedName("data")

    public ArrayList<BoBlogs> allItems;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public ArrayList<BoBlogs> getAllItems() {
        return allItems;
    }

    public void setAllItems(ArrayList<BoBlogs> allItems) {
        this.allItems = allItems;
    }




}
