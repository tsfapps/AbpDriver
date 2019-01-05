package com.abp.driver.model.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class StatusDistrictList extends SugarRecord {
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("district_name")
    @Expose
    private String districtName;
    @SerializedName("no_of_policestation")
    @Expose
    private Integer noOfPolicestation;

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

    public Integer getNoOfPolicestation() {
        return noOfPolicestation;
    }

    public void setNoOfPolicestation(Integer noOfPolicestation) {
        this.noOfPolicestation = noOfPolicestation;
    }
}
