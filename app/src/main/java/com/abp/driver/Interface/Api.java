package com.abp.driver.Interface;

import com.abp.driver.pojo.ModelDriver;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("api/logindetails.php")
    Call<ModelDriver> loginUser(
            @Field("API_KEY") String API_KEY,
            @Field("type") String type,
            @Field("username") String username,
            @Field("password") String password
    );

//    @POST("api/logindetails.php")
//    Call<ModelDriver> loginUser(@Body ModelDriver modelDriver);

}
