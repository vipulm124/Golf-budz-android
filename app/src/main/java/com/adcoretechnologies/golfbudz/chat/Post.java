package com.adcoretechnologies.golfbudz.chat;

import com.adcoretechnologies.golfbudz.utils.Common;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Irfan on 06/09/16.
 */
@IgnoreExtraProperties
public class Post {
    public String postId;
    public String from;

    public String text;
    public String to;

    public String postedOn;


    public Post(String from, String text, String to) {
        this.from = from;
        this.to = to;

        this.postedOn = Common.getTimestamp();
        this.text=text;
    }


    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("text", text);
        result.put("from", from);
        result.put("to", to);
        result.put("postedOn", postedOn);

        return result;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }
}
