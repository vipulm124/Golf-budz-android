package com.adcoretechnologies.golfbudz.auth.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 10/9/2017.
 */

class BoProfession {

    @SerializedName("name")
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
