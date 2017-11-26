package com.adcoretechnologies.golfbudz.playrequest.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Adcore on 2/7/2017.
 */

public class BoPlay implements Serializable {
    @SerializedName("noOfHoles")
    public String noOfHoles;
    @SerializedName("day")
    public String day;
    @SerializedName("venue")
    public String venue;
    @SerializedName("teeOffTime")
    public String teeOffTime;
    @SerializedName("type")
    public String type;
    @SerializedName("requestInfo")
    public String requestInfo;
    @SerializedName("redefineRequest")
    public String redefineRequest;
    @SerializedName("handicap")
    public String handicap;
    @SerializedName("locations")
    public String locations;
    @SerializedName("industry")
    public String industry;
    @SerializedName("profession")
    public String profession;
    @SerializedName("gender")
    public String age;
    @SerializedName("age")
    public String gender;
    @SerializedName("userId")
    public String userId;
    @SerializedName("userName")
    public String userName;
    @SerializedName("userImgUrl")
    public String userImgUrl;
    @SerializedName("players")
    public String players;
    @SerializedName("Id")
    public int Id;
    @SerializedName("status")
    public String userStatus;
    @SerializedName("dateCreated")
    public String dateCreated;

    @SerializedName("NoOfHandicaps")
    public String NoOfHandicaps;
    @SerializedName("affiliated")
    public String affiliated;
    @SerializedName("golfClub")
    public String golfClub;
    @SerializedName("requestType")
    public String requestType;
    @SerializedName("recieverIds")
    public String recieverIds;
    public String pairtext;

    public String getPairtext() {
        return pairtext;
    }

    public void setPairtext(String pairtext) {
        this.pairtext = pairtext;
    }



    public String getNoOfHoles() {
        return noOfHoles;
    }

    public void setNoOfHoles(String noOfHoles) {
        this.noOfHoles = noOfHoles;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTeeOffTime() {
        return teeOffTime;
    }

    public void setTeeOffTime(String teeOffTime) {
        this.teeOffTime = teeOffTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(String requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getRedefineRequest() {
        return redefineRequest;
    }

    public void setRedefineRequest(String redefineRequest) {
        this.redefineRequest = redefineRequest;
    }

    public String getHandicap() {
        return handicap;
    }

    public void setHandicap(String handicap) {
        this.handicap = handicap;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getNoOfHandicaps() {
        return NoOfHandicaps;
    }

    public void setNoOfHandicaps(String noOfHandicaps) {
        NoOfHandicaps = noOfHandicaps;
    }

    public String getAffiliated() {
        return affiliated;
    }

    public void setAffiliated(String affiliated) {
        this.affiliated = affiliated;
    }

    public String getGolfClub() {
        return golfClub;
    }

    public void setGolfClub(String golfClub) {
        this.golfClub = golfClub;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRecieverIds() {
        return recieverIds;
    }

    public void setRecieverIds(String recieverIds) {
        this.recieverIds = recieverIds;
    }
}
