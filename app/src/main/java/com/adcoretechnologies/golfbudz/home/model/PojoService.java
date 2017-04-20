package com.adcoretechnologies.golfbudz.home.model;

import com.adcoretechnologies.golfbudz.other.BoError;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Adcore Technologies on 10/21/2016.
 */
@Parcel
public class PojoService {

    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("error")
    public BoError error;
    @SerializedName("data")
    public ArrayList<BoService> allItems;


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

    public ArrayList<BoService> getAllItems() {
        return allItems;
    }

    public void setAllItems(ArrayList<BoService> allItems) {
        this.allItems = allItems;
    }

}
