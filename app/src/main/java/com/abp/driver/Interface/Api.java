package com.abp.driver.Interface;

import com.abp.driver.model.driver.DriverAttendance;
import com.abp.driver.model.driver.DriverAttendanceList;
import com.abp.driver.model.login.ModelLogin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("api/logindetails.php")
    Call<ModelLogin> loginUser(
            @Field("API_KEY") String API_KEY,
            @Field("type") String type,
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/get-attendance.php")
    Call<DriverAttendance> driverAttendance(
            @Field("API_KEY") String API_KEY,
            @Field("phone_no") String phone_no

    );
    @FormUrlEncoded
    @POST("api/get-attendance.php")
    Call<List<DriverAttendance>> driverAttendanceList(
            @Field("API_KEY") String API_KEY,
            @Field("phone_no") String phone_no

    );

//    @POST("api/logindetails.php")
//    Call<ModelLoginList> loginUser(@Body ModelLoginList modelDriver);

}
