package com.abp.driver.model.driver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverAttendanceList {
    @SerializedName("type_io")
    @Expose
    private String typeIo;
    @SerializedName("phone_no")
    @Expose
    private String phoneNo;
    @SerializedName("time_in")
    @Expose
    private String timeIn;
    @SerializedName("time_out")
    @Expose
    private String timeOut;
    @SerializedName("total_time")
    @Expose
    private String totalTime;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("check_in_date")
    @Expose
    private String checkInDate;
    @SerializedName("check_out_date")
    @Expose
    private String checkOutDate;

    public String getTypeIo() {
        return typeIo;
    }

    public void setTypeIo(String typeIo) {
        this.typeIo = typeIo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}
