package com.adcoretechnologies.golfbudz.auth.Register;

import com.adcoretechnologies.golfbudz.other.BoError;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by admin on 12/30/2016.
 */

public class PojoCity {
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("error")
    public BoError error;
    @SerializedName("data")
    public ArrayList<BoCity> allItems;

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

    public ArrayList<BoCity> getAllItems() {
        return allItems;
    }

    public void setAllItems(ArrayList<BoCity> allItems) {
        this.allItems = allItems;
    }
}
