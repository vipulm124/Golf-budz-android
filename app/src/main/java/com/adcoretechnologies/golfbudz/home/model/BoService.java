package com.adcoretechnologies.golfbudz.home.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Irfan on 20/10/16.
 */
@Parcel
public class BoService implements Serializable {

    @SerializedName("serviceName")
    String serviceName;
    @SerializedName("price")
    String amount;
    @SerializedName("categoryId")
    String categoryId;
    @SerializedName("latitude")
    String latitude;
    @SerializedName("longitude")
    String longitude;

    public String getPerCriteria() {
        return perCriteria;
    }

    public void setPerCriteria(String perCriteria) {
        this.perCriteria = perCriteria;
    }

    public String getDisplayPrice() {
        //return "₹" + amount + "/" + perCriteria;
        return "" + amount;
    }

    @SerializedName("perCriteria")
    String perCriteria;
    @SerializedName("serviceId")
    String serviceId;
    @SerializedName("serviceDescription")
    String serviceDescription;
    @SerializedName("heroImagePath")
    String imagePath;
    @SerializedName("isFavourite")
    String isFavourite;
    @SerializedName("media")
    ArrayList<BoMedia> allImages;

    @SerializedName("categoryName")
    String categoryName;

    public String getImportantFacts() {
        return importantFacts;
    }

    public void setImportantFacts(String importantFacts) {
        this.importantFacts = importantFacts;
    }

    @SerializedName("needToKnow")
    String importantFacts;
    @SerializedName("clickCount")
    String clickCount;
    @SerializedName("duration")
    String duration;
    @SerializedName("goodFor")
    String goodFor;
    @SerializedName("isShippingRequired")
    String isShippingRequired;
    @SerializedName("descriptionHeading")
    String descriptionHeading;
    @SerializedName("displayAddress")
    String displayAddress;

    @SerializedName("shortDescription")
    String shortDescription;
    @SerializedName("detailedDescription")
    String detailedDescription;



    public String getExcludes() {
        return excludes;
    }

    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }

    public String getIncludes() {
        return includes;
    }

    public void setIncludes(String includes) {
        this.includes = includes;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDisplayAddress() {
        return displayAddress;
    }

    public void setDisplayAddress(String displayAddress) {
        this.displayAddress = displayAddress;
    }

    public String getDescriptionHeading() {
        return descriptionHeading;
    }

    public void setDescriptionHeading(String descriptionHeading) {
        this.descriptionHeading = descriptionHeading;
    }

    public boolean isShippingRequired() {
        return isShippingRequired.equals("Y") ? true : false;
    }

    public void setShippingRequired(String shippingRequired) {
        isShippingRequired = shippingRequired;
    }

    public String getGoodFor() {
        return goodFor;
    }

    public void setGoodFor(String goodFor) {
        this.goodFor = goodFor;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getClickCount() {
        return clickCount;
    }

    public void setClickCount(String clickCount) {
        this.clickCount = clickCount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @SerializedName("includes")
    String includes;
    @SerializedName("excludes")
    String excludes;


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getAmount() {
        return Double.parseDouble(amount);
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIsShippingRequired() {
        return isShippingRequired;
    }

    public void setIsShippingRequired(String isShippingRequired) {
        this.isShippingRequired = isShippingRequired;
    }
}
