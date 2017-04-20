package com.adcoretechnologies.golfbudz.more;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adcore on 2/4/2017.
 */

public class BoContact {
    @SerializedName("userId")
    public String userId;
    @SerializedName("title")
    public String title;
    @SerializedName("message")
    public String message;
    @SerializedName("email")
    public String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
