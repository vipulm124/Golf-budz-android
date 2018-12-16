package com.adcoretechnologies.golfbudz.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Adcore on 2/4/2017.
 */

public class BoPost implements Serializable {
    @SerializedName("id")
    String id;
    @SerializedName("userId")
    String userId;
    @SerializedName("blogType")
    String blogType;
    @SerializedName("postType")
    String postType;
    @SerializedName("blogImage")
    String blogImage;
    @SerializedName("text")
    String text;
    @SerializedName("title")
    String title;
    @SerializedName("likeCount")
    String likeCount;
    @SerializedName("likeStatus")
    String likeStatus;
    @SerializedName("commentCount")
    String commentCount;
    @SerializedName("thumbUrl")
    String thumbUrl;
    @SerializedName("likeUsers")
    String likeUsers;
    @SerializedName("profileImage")
    String profileImage;

    public String getLikes(){
        return likeUsers;
    }

    public void setLikes(String likeUsers){
        this.likeUsers=likeUsers;
    }
    public  String getLikeStatus(){return  likeStatus;}
    public  void setLikeStatus(String likeStatus){this.likeStatus = likeStatus;}

    public  String getLikeCount(){return  likeCount;}
    public  void setLikeCount(String like){this.likeCount = like;}

    public  String getCommentCount(){return  commentCount;}
    public  void setCommentCount(String commentCount){this.commentCount = commentCount;}

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getblogType() {
        return blogType;
    }

    public void setblogType(String blogType) {
        this.blogType = blogType;
    }



    public String getblogImage() {
        return blogImage;
    }

    public void setblogImage(String blogImage) {
        this.blogImage = blogImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }
    public String getVideo() {
        return blogImage;
    }

    public void setVideo(String blogImage) {
        this.blogImage = blogImage;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getProfileImage(){ return profileImage;}
    public void setProfileImage(String profileImage){this.profileImage = profileImage;}
}
