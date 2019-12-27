package com.bikram.taskmanagerapi.urlapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Url {
    public static final String BASE_URL = "http://10.0.2.2:3000/";
    //    public static final String BASE_URL = "http://172.100.100.5:3000/"; soft server

    //create an instance of Retrofit
    public static Retrofit getInstance() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
