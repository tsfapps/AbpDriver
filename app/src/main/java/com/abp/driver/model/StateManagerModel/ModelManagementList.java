package com.abp.driver.model.StateManagerModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelManagementList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("no_of_district")
    @Expose
    private Integer noOfDistrict;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Integer getNoOfDistrict() {
        return noOfDistrict;
    }

    public void setNoOfDistrict(Integer noOfDistrict) {
        this.noOfDistrict = noOfDistrict;
    }

}
