package com.adcoretechnologies.golfbudz.chat;

import com.adcoretechnologies.golfbudz.utils.Common;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adcore on 2/9/2017.
 */

public class MyChat {
    public String chatId;
    public String name;
    public String image;

    public String message;

    public String postedOn;
    public String chatWithId;

    public MyChat(String name, String image, String message, String chatWithId) {
        this.name = name;
        this.image = image;
        this.postedOn = Common.getTimestamp();
        this.message=message;
        this.chatWithId=chatWithId;
    }


    public MyChat() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("image", image);
        result.put("message", message);
        result.put("postedOn", postedOn);
        result.put("chatWithId", chatWithId);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatWithId() {
        return chatWithId;
    }

    public void setChatWithId(String chatWithId) {
        this.chatWithId = chatWithId;
    }
}