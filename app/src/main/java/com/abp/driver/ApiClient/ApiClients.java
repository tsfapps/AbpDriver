package com.abp.driver.ApiClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClients {

    private static final String BASE_URL = "http://www.abpsolutions.in/apps/";
    private static Retrofit retrofit;

    public static Retrofit getApiClients(){
        if (retrofit==null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
