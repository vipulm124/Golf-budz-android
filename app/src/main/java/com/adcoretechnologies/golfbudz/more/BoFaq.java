package com.adcoretechnologies.golfbudz.more;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adcore on 2/6/2017.
 */

public class BoFaq {
    @SerializedName("createdAt")
    public String createdAt;

    @SerializedName("description")
    public String description;

    @SerializedName("title")
    public String title;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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
}
