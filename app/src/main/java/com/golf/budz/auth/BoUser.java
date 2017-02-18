package com.golf.budz.auth;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Rehan on 12/6/2016.
 */

public class BoUser implements Serializable {
    @SerializedName("email")
    public String email;

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
}
