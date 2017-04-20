package com.adcoretechnologies.golfbudz.group;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Adcore on 2/17/2017.
 */

public class PojoGroup {
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("error")
    public Error error;
    @SerializedName("data")
    public ArrayList<BoGroup> allItems;

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

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public ArrayList<BoGroup> getAllItems() {
        return allItems;
    }

    public void setAllItems(ArrayList<BoGroup> allItems) {
        this.allItems = allItems;
    }

}
