package com.abp.driver.model.driver;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class DriverAttendanceList extends SugarRecord{
    @SerializedName("longitude_in")
    @Expose
    private String longitudeIn;
    @SerializedName("longitude_out")
    @Expose
    private String longitudeOut;
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
    @SerializedName("latitude_in")
    @Expose
    private String latitudeIn;
    @SerializedName("latitude_out")
    @Expose
    private String latitudeOut;
    @SerializedName("check_in_date")
    @Expose
    private String checkInDate;
    @SerializedName("check_out_date")
    @Expose
    private String checkOutDate;
    @SerializedName("check_in_code")
    @Expose
    private String checkInCode;
    @SerializedName("check_out_code")
    @Expose
    private String checkOutCode;

    private Context mContext;

    public DriverAttendanceList(Context mContext, String longitudeIn, String longitudeOut, String typeIo, String phoneNo, String timeIn, String timeOut, String totalTime, String latitudeIn, String latitudeOut, String checkInDate, String checkOutDate) {
        this.mContext = mContext;
        this.longitudeIn = longitudeIn;
        this.longitudeOut = longitudeOut;
        this.typeIo = typeIo;
        this.phoneNo = phoneNo;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.totalTime = totalTime;
        this.latitudeIn = latitudeIn;
        this.latitudeOut = latitudeOut;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public DriverAttendanceList() {
    }

    public String getLongitudeIn() {
        return longitudeIn;
    }

    public void setLongitudeIn(String longitudeIn) {
        this.longitudeIn = longitudeIn;
    }

    public String getLongitudeOut() {
        return longitudeOut;
    }

    public void setLongitudeOut(String longitudeOut) {
        this.longitudeOut = longitudeOut;
    }

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

    public String getLatitudeIn() {
        return latitudeIn;
    }

    public void setLatitudeIn(String latitudeIn) {
        this.latitudeIn = latitudeIn;
    }

    public String getLatitudeOut() {
        return latitudeOut;
    }

    public void setLatitudeOut(String latitudeOut) {
        this.latitudeOut = latitudeOut;
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

    public String getCheckInCode() {
        return checkInCode;
    }

    public void setCheckInCode(String checkInCode) {
        this.checkInCode = checkInCode;
    }

    public String getCheckOutCode() {
        return checkOutCode;
    }

    public void setCheckOutCode(String checkOutCode) {
        this.checkOutCode = checkOutCode;
    }
}
