package com.adcoretechnologies.golfbudz.auth.Register;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 12/30/2016.
 */

public class BoCity {
    @SerializedName("name")
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
