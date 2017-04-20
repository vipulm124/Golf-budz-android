package com.adcoretechnologies.golfbudz.friends;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Adcore on 2/9/2017.
 */

public class PojoFriend {
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("error")
    public Error error;
    @SerializedName("data")
    public ArrayList<BoFriend> allItems;

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

    public ArrayList<BoFriend> getAllItems() {
        return allItems;
    }

    public void setAllItems(ArrayList<BoFriend> allItems) {
        this.allItems = allItems;
    }
}
