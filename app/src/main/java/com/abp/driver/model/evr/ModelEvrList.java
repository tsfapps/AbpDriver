package com.abp.driver.model.evr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelEvrList {

    @SerializedName("evr_id")
    @Expose
    private String evrId;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("policestaton_id")
    @Expose
    private String policestatonId;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("district_name")
    @Expose
    private String districtName;
    @SerializedName("policestationname")
    @Expose
    private String policestationname;
    @SerializedName("vehicle_number")
    @Expose
    private String vehicleNumber;
    @SerializedName("driver1_id")
    @Expose
    private String driver1Id;
    @SerializedName("driver2_id")
    @Expose
    private String driver2Id;
    @SerializedName("driver3_id")
    @Expose
    private String driver3Id;
    @SerializedName("driver1_name")
    @Expose
    private String driver1Name;
    @SerializedName("driver2_name")
    @Expose
    private String driver2Name;
    @SerializedName("driver3_name")
    @Expose
    private Object driver3Name;

    public String getEvrId() {
        return evrId;
    }

    public void setEvrId(String evrId) {
        this.evrId = evrId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getPolicestatonId() {
        return policestatonId;
    }

    public void setPolicestatonId(String policestatonId) {
        this.policestatonId = policestatonId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getPolicestationname() {
        return policestationname;
    }

    public void setPolicestationname(String policestationname) {
        this.policestationname = policestationname;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getDriver1Id() {
        return driver1Id;
    }

    public void setDriver1Id(String driver1Id) {
        this.driver1Id = driver1Id;
    }

    public String getDriver2Id() {
        return driver2Id;
    }

    public void setDriver2Id(String driver2Id) {
        this.driver2Id = driver2Id;
    }

    public String getDriver3Id() {
        return driver3Id;
    }

    public void setDriver3Id(String driver3Id) {
        this.driver3Id = driver3Id;
    }

    public String getDriver1Name() {
        return driver1Name;
    }

    public void setDriver1Name(String driver1Name) {
        this.driver1Name = driver1Name;
    }

    public String getDriver2Name() {
        return driver2Name;
    }

    public void setDriver2Name(String driver2Name) {
        this.driver2Name = driver2Name;
    }

    public Object getDriver3Name() {
        return driver3Name;
    }

    public void setDriver3Name(Object driver3Name) {
        this.driver3Name = driver3Name;
    }
}
