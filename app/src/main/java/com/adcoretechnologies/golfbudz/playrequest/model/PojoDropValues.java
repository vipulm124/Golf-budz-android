package com.adcoretechnologies.golfbudz.playrequest.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Adcore on 5/29/2017.
 */

public class PojoDropValues {
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("error")
    public Error error;
    @SerializedName("data")
    public ArrayList<BoDropVales> allItems;

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

    public ArrayList<BoDropVales> getAllItems() {
        return allItems;
    }

    public void setAllItems(ArrayList<BoDropVales> allItems) {
        this.allItems = allItems;
    }
}
