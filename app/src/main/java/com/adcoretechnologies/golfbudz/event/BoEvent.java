package com.adcoretechnologies.golfbudz.event;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Rehan on 12/6/2016.
 */

public class BoEvent {
    @SerializedName("_id")
    public String _id;

    @SerializedName("createdAt")
    public String createdAt;
    @SerializedName("updatedAt")
    public String updatedAt;
    @SerializedName("time")
    public String time;
    @SerializedName("date")
    public String date;
    @SerializedName("description")
    public String description;
    @SerializedName("title")
    public String title;
    @SerializedName("userId")
    public String userId;
    @SerializedName("userName")
    public String userName;
    @SerializedName("likeCount")
    public String likeCount;
    @SerializedName("image")
    String image;
    @SerializedName("likes")
    ArrayList<String> likes;
    boolean likeStatus;

    public boolean isAttendingStatus() {
        return attendingStatus;
    }

    public void setAttendingStatus(boolean attendingStatus) {
        this.attendingStatus = attendingStatus;
    }

    boolean attendingStatus;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isLikeStatus() {

        return likeStatus;
    }

    public void setLikeStatus(boolean likeStatus) {
        this.likeStatus = likeStatus;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }
}
