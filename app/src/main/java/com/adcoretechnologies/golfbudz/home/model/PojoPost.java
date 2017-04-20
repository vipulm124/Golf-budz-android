package com.adcoretechnologies.golfbudz.home.model;

import com.adcoretechnologies.golfbudz.other.BoError;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Adcore on 2/4/2017.
 */

public class PojoPost {
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("error")
    public BoError error;
    @SerializedName("data")
    public ArrayList<BoPost> allItems;


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

    public BoError getError() {
        return error;
    }

    public void setError(BoError error) {
        this.error = error;
    }

    public ArrayList<BoPost> getAllItems() {
        return allItems;
    }

    public void setAllItems(ArrayList<BoPost> allItems) {
        this.allItems = allItems;
    }

}
