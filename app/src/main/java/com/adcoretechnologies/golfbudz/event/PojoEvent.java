package com.adcoretechnologies.golfbudz.event;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Irfan on 20/10/16.
 */

@Parcel
public class PojoEvent {

    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("error")
    public Error error;
    @SerializedName("data")
    public ArrayList<BoEvent> allItems;

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

    public ArrayList<BoEvent> getAllItems() {
        return allItems;
    }

    public void setAllItems(ArrayList<BoEvent> allItems) {
        this.allItems = allItems;
    }
}
