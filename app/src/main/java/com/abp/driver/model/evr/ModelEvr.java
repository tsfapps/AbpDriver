package com.abp.driver.model.evr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelEvr {
    @SerializedName("data")
    @Expose
    private List<ModelEvrList> data = null;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;

    public List<ModelEvrList> getData() {
        return data;
    }

    public void setData(List<ModelEvrList> data) {
        this.data = data;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }
}
