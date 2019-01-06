package com.abp.driver.Interface;

import com.abp.driver.model.PunchInOut.ModelPunchInOut;
import com.abp.driver.model.driver.DriverAttendance;
import com.abp.driver.model.driver.DriverAttendanceList;
import com.abp.driver.model.login.ModelLogin;
import com.abp.driver.model.status.DistrictDetail;
import com.abp.driver.model.status.StatusDistrict;

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
    @POST("api/view-statewise-districtapi.php")
    Call<StatusDistrict> districtList(
            @Field("API_KEY") String API_KEY,
            @Field("stateid") String stateid
    );
    @FormUrlEncoded
    @POST("api/view-statewise-districtapi.php")
    Call<StatusDistrict> districtDetail(
            @Field("API_KEY") String API_KEY,
            @Field("stateid") String stateid,
            @Field("districtid") String districtid
    );

    @FormUrlEncoded
    @POST("api/attendanceapi.php")
    Call<ModelPunchInOut> driverPunchInOut(
            @Field("API_KEY") String API_KEY,
            @Field("type_io") String type_io,
            @Field("phone_no") String phone_no,
            @Field("time_in") String time_in,
            @Field("time_out") String time_out,
            @Field("total_time") String total_time,
            @Field("longitude_in") String longitude_in,
            @Field("longitude_out") String longitude_out,
            @Field("latitude_in") String latitude_in,
            @Field("latitude_out") String latitude_out,
            @Field("check_in_date") String check_in_date,
            @Field("check_out_date") String check_out_date

    );

//    @POST("api/logindetails.php")
//    Call<ModelLoginList> loginUser(@Body ModelLoginList modelDriver);

}
