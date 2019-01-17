package com.abp.driver.model.date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelDate {
    @SerializedName("data")
    @Expose
    private List<ModelDateList> data = null;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;

    public List<ModelDateList> getData() {
        return data;
    }

    public void setData(List<ModelDateList> data) {
        this.data = data;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }
}
