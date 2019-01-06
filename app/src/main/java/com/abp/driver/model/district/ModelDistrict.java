package com.abp.driver.model.district;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDistrict {

    @SerializedName("data")
    @Expose
    private List<ModelDistrictList> data = null;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;

    public List<ModelDistrictList> getData() {
        return data;
    }

    public void setData(List<ModelDistrictList> data) {
        this.data = data;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

}
