package com.adcoretechnologies.golfbudz.items;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Adcore on 2/1/2017.
 */

public class PojoItems {
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("error")
    public Error error;
    @SerializedName("data")
    public ArrayList<BoItems> allItems;

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

    public ArrayList<BoItems> getAllItems() {
        return allItems;
    }

    public void setAllItems(ArrayList<BoItems> allItems) {
        this.allItems = allItems;
    }
}
