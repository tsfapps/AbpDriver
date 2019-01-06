package com.abp.driver.model.police;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelPolice {
    @SerializedName("data")
    @Expose
    private List<ModelPoliceList> data = null;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;

    public List<ModelPoliceList> getData() {
        return data;
    }

    public void setData(List<ModelPoliceList> data) {
        this.data = data;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }
}
