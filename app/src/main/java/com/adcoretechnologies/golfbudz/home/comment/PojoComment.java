package com.adcoretechnologies.golfbudz.home.comment;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Adcore on 2/10/2017.
 */

public class PojoComment {


    @SerializedName("commentId")
    public int commentId;
    @SerializedName("postId")
    public int postId;
    @SerializedName("userId")
    public int userId;
    @SerializedName("comment")
    public String comment;
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("error")
    public Error error;
    @SerializedName("data")
    public ArrayList<BoCommnet> allItems;

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

    public ArrayList<BoCommnet> getAllItems() {
        return allItems;
    }

    public int getCommentId(){
        return commentId;
    }
    public  void  setCommentId(int commentId){this.commentId=commentId;}

    public int getUserId(){return userId;}
    public  void setUserId(int userId){this.userId=userId;}

    public int getPostId(){return  postId;}
        public void setPostId(int postId){this.postId = postId;}

        public  String getComment(){return comment;}
        public void setComment(String comment){this.comment=comment;}
    public void setAllItems(ArrayList<BoCommnet> allItems) {
        this.allItems = allItems;
    }
}
