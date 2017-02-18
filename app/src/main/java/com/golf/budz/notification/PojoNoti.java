package com.golf.budz.notification;

import com.golf.budz.auth.BoUser;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Adcore on 2/6/2017.
 */

public class PojoNoti {

    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("error")
    public Error error;
    @SerializedName("data")
    public ArrayList<BoNoti> allItems;

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

    public ArrayList<BoNoti> getAllItems() {
        return allItems;
    }

    public void setAllItems(ArrayList<BoNoti> allItems) {
        this.allItems = allItems;
    }
}
