package com.adcoretechnologies.golfbudz.playrequest.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Adcore on 5/30/2017.
 */

public class BoStatusRequest implements Serializable {

    @SerializedName("profileImage")
    public String profileImage;
    @SerializedName("lastName")
    public String lastName;
    @SerializedName("firstName")
    public String firstName;
    @SerializedName("status")
    public String status;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
