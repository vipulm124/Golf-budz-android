package com.adcoretechnologies.golfbudz.other;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Irfan on 13/10/16.
 */

@Parcel
public class Error {

    @SerializedName("internalMessage")
    public String message;

    @SerializedName("internalMessageJson")
    public String internalMessageJson;

}
