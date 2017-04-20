package com.adcoretechnologies.golfbudz.group;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adcore on 2/17/2017.
 */

public class BoGroup {
    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;
    @SerializedName("description")
    public String description;
    @SerializedName("category")
    public String category;
    @SerializedName("operatingHours")
    public String operatingHours;
    @SerializedName("userId")
    public String userId;
    @SerializedName("image")
    public String image;
    @SerializedName("video")
    public String video;
    @SerializedName("userName")
    public String userName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
