package com.abp.driver.model.police;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class ModelPoliceList extends SugarRecord {
    @SerializedName("police_id")
    @Expose
    private String policeId;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("district_name")
    @Expose
    private String districtName;
    @SerializedName("policestationname")
    @Expose
    private String policestationname;
    @SerializedName("no_of_erv")
    @Expose
    private Integer noOfErv;

    public String getPoliceId() {
        return policeId;
    }

    public void setPoliceId(String policeId) {
        this.policeId = policeId;
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

    public Integer getNoOfErv() {
        return noOfErv;
    }

    public void setNoOfErv(Integer noOfErv) {
        this.noOfErv = noOfErv;
    }
}
