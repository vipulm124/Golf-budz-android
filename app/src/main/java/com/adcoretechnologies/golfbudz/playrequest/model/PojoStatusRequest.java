package com.adcoretechnologies.golfbudz.playrequest.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Adcore on 5/30/2017.
 */

public class PojoStatusRequest {
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("error")
    public Error error;
    @SerializedName("data")
    public ArrayList<BoStatusRequest> allItems;

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

    public ArrayList<BoStatusRequest> getAllItems() {
        return allItems;
    }

    public void setAllItems(ArrayList<BoStatusRequest> allItems) {
        this.allItems = allItems;
    }

}
