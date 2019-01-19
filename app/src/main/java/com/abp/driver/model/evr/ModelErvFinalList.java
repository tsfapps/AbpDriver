package com.abp.driver.model.evr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class ModelErvFinalList extends SugarRecord {
    @SerializedName("evr_id")
    @Expose
    private String evrId;
    @SerializedName("registration_no")
    @Expose
    private String registrationNo;
    @SerializedName("driver1")
    @Expose
    private String driver1;
    @SerializedName("driverstatus1")
    @Expose
    private String driverstatus1;
    @SerializedName("driver2")
    @Expose
    private String driver2;
    @SerializedName("driverstatus2")
    @Expose
    private String driverstatus2;
    @SerializedName("driver3")
    @Expose
    private String driver3;
    @SerializedName("driverstatus3")
    @Expose
    private String driverstatus3;

    public String getEvrId() {
        return evrId;
    }

    public void setEvrId(String evrId) {
        this.evrId = evrId;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getDriver1() {
        return driver1;
    }

    public void setDriver1(String driver1) {
        this.driver1 = driver1;
    }

    public String getDriverstatus1() {
        return driverstatus1;
    }

    public void setDriverstatus1(String driverstatus1) {
        this.driverstatus1 = driverstatus1;
    }

    public String getDriver2() {
        return driver2;
    }

    public void setDriver2(String driver2) {
        this.driver2 = driver2;
    }

    public String getDriverstatus2() {
        return driverstatus2;
    }

    public void setDriverstatus2(String driverstatus2) {
        this.driverstatus2 = driverstatus2;
    }

    public String getDriver3() {
        return driver3;
    }

    public void setDriver3(String driver3) {
        this.driver3 = driver3;
    }

    public String getDriverstatus3() {
        return driverstatus3;
    }

    public void setDriverstatus3(String driverstatus3) {
        this.driverstatus3 = driverstatus3;
    }

}
