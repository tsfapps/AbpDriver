package com.abp.driver.model.PunchInOut;

import com.orm.SugarRecord;

public class ModelPunchInOutLocal extends SugarRecord {

    private String longitudeOut;
    private String latitudeOut;
    private String typeIo;
    private String phoneNo;
    private String timeIn;
    private String timeOut;
    private String totalTime;
    private String longitudeIn;
    private String latitudeIn;
    private String checkInDate;
    private String checkOutDate;
    private String status;
    private String isSynced;
    private String isCheckInSynced;
    private String isCheckOutSynced;

    public String getLongitudeOut() {
        return longitudeOut;
    }

    public void setLongitudeOut(String longitudeOut) {
        this.longitudeOut = longitudeOut;
    }

    public String getLatitudeOut() {
        return latitudeOut;
    }

    public void setLatitudeOut(String latitudeOut) {
        this.latitudeOut = latitudeOut;
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

    public String getLongitudeIn() {
        return longitudeIn;
    }

    public void setLongitudeIn(String longitude) {
        this.longitudeIn = longitude;
    }

    public String getIsCheckInSynced() {
        return isCheckInSynced;
    }

    public void setIsCheckInSynced(String isCheckInSynced) {
        this.isCheckInSynced = isCheckInSynced;
    }

    public String getIsCheckOutSynced() {
        return isCheckOutSynced;
    }

    public void setIsCheckOutSynced(String isCheckOutSynced) {
        this.isCheckOutSynced = isCheckOutSynced;
    }

    public String getLatitudeIn() {
        return latitudeIn;
    }

    public void setLatitudeIn(String latitude) {
        this.latitudeIn = latitude;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(String isSynced) {
        this.isSynced = isSynced;
    }

}
