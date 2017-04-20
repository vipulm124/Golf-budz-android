package com.adcoretechnologies.golfbudz.items;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adcore on 2/1/2017.
 */

public class BoItems {
    @SerializedName("userId")
    public String userId;
    @SerializedName("price")
    public String price;
    @SerializedName("image")
    public String image;
    @SerializedName("description")
    public String description;
    @SerializedName("title")
    public String title;
    @SerializedName("id")
    public String id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
