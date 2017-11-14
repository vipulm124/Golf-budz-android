package com.adcoretechnologies.golfbudz.utils.api;




import com.adcoretechnologies.golfbudz.utils.Common;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Irfan on 22/09/15.
 */
public class APIHelper {

    public static IApiService getAppServiceMethod() {
        String url = Common.getAPIUrl();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ProtoConverterFactory.create())
                .build();
        return retrofit.create(IApiService.class);
    }


    public static IApiService getAppServiceMethodChange() {
        String url = Common.getAPIUrlChange();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ProtoConverterFactory.create())
                .build();
        return retrofit.create(IApiService.class);
    }


}
