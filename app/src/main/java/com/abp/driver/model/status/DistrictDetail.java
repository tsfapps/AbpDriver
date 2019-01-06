package com.abp.driver.model.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistrictDetail {
    @SerializedName("data")
    @Expose
    private List<DistrictDetailList> data = null;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;

    public List<DistrictDetailList> getData() {
        return data;
    }

    public void setData(List<DistrictDetailList> data) {
        this.data = data;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }
}
