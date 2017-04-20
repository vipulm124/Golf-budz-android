package com.adcoretechnologies.golfbudz.home;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Rehan on 11/16/2016.
 */

public interface RequestInterface {

    @GET("android/jsonandroid")
    Call<JSONResponse> getJSON();
}