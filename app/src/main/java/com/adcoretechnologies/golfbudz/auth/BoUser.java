package com.adcoretechnologies.golfbudz.auth;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Rehan on 12/6/2016.
 */

public class BoUser implements Serializable {
    @SerializedName("email")
    public String email;
    @SerializedName("fullName")
    public String fullName;
    @SerializedName("contact")
    public String contact;
    @SerializedName("password")
    public String password;
    @SerializedName("userId")
    public String userId;
    @SerializedName("firstName")
    public String firstName;
    @SerializedName("lastName")
    public String lastName;
    @SerializedName("profileImage")
    public String profileImage;
    @SerializedName("dob")
    public String dob;
    @SerializedName("handicap")
    public String handicap;
    @SerializedName("strength")
    public String strength;
    @SerializedName("weakness")
    public String weakness;
    @SerializedName("country")
    public String country;
    @SerializedName("deviceId")
    public String deviceId;
    @SerializedName("imeiNo")
    public String imeiNo;
    @SerializedName("deviceType")
    public String deviceType;
    @SerializedName("userType")
    public String userType;
    @SerializedName("clubName")
    public String clubName;
    @SerializedName("description")
    public String description;
    @SerializedName("address")
    public String address;
    @SerializedName("city")
    public String city;
    @SerializedName("subRub")
    public String subRub;
    @SerializedName("operatingHours")
    public String operatingHours;
    @SerializedName("golfbag")
    public String golfbag;
    @SerializedName("rounds")
    public String rounds;
    @SerializedName("status")
    public String status;

    @SerializedName("sex")
    public String sex;
    @SerializedName("affiliated")
    public String affiliated;
    @SerializedName("age")
    public String age;
    @SerializedName("profession")
    public String profession;
    @SerializedName("roundsPerMonth")
    public String roundsPerMonth;
    @SerializedName("refer")
    public String refer;
    @SerializedName("playWithUs")
    public String playWithUs;
    @SerializedName("playWithOther")
    public String playWithOther;
    @SerializedName("noOfHandicap")
    public String noOfHandicap;
    @SerializedName("course")
    public String course;
    @SerializedName("location")
    public String location;

    @SerializedName("socialToken")
    public String socialToken;

    @SerializedName("userlanguage")
    public String language;
@SerializedName("followStatus")
    public  String followStatus;



    @SerializedName("pushId")
    public String pushId;
    public String getNoOfHandicap() {
        return noOfHandicap;
    }

    public void setNoOfHandicap(String noOfHandicap) {
        this.noOfHandicap = noOfHandicap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHandicap() {
        return handicap;
    }

    public void setHandicap(String handicap) {
        this.handicap = handicap;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getWeakness() {
        return weakness;
    }

    public void setWeakness(String weakness) {
        this.weakness = weakness;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImeiNo() {
        return imeiNo;
    }

    public void setImeiNo(String imeiNo) {
        this.imeiNo = imeiNo;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubRub() {
        return subRub;
    }

    public void setSubRub(String subRub) {
        this.subRub = subRub;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    public String getGolfbag() {
        return golfbag;
    }

    public void setGolfbag(String golfbag) {
        this.golfbag = golfbag;
    }

    public String getRounds() {
        return rounds;
    }

    public void setRounds(String rounds) {
        this.rounds = rounds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFollowStatus() {
        if(followStatus==null) {
        followStatus="false";
        }
        return followStatus;
    }

    public void setFollowStatus(String status) {
        this.followStatus = followStatus;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAffiliated() {
        return affiliated;
    }

    public void setAffiliated(String affiliated) {
        this.affiliated = affiliated;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getRoundsPerMonth() {
        return roundsPerMonth;
    }

    public void setRoundsPerMonth(String roundsPerMonth) {
        this.roundsPerMonth = roundsPerMonth;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getPlayWithUs() {
        return playWithUs;
    }

    public void setPlayWithUs(String playWithUs) {
        this.playWithUs = playWithUs;
    }

    public String getPlayWithOther() {
        return playWithOther;
    }

    public void setPlayWithOther(String playWithOther) {
        this.playWithOther = playWithOther;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSocialToken() {
        return socialToken;
    }

    public void setSocialToken(String socialToken) {
        this.socialToken = socialToken;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }


}
