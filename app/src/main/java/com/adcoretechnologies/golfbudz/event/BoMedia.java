package com.adcoretechnologies.golfbudz.event;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Irfan on 08/11/16.
 */

@Parcel
public class BoMedia {

    @SerializedName("mediaPath")
    public String mediaPath;

    public String getMediaPath() {
        return mediaPath;
    }
}
