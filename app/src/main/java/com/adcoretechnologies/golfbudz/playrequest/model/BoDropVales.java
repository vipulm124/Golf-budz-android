package com.adcoretechnologies.golfbudz.playrequest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adcore on 5/29/2017.
 */

public class BoDropVales {
    @SerializedName("displayName")
    public String displayName;
    @SerializedName("Id")
    public String id;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
