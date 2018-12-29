package com.abp.driver.model.driver;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDriver {

    @SerializedName("data")
    @Expose
    private List<ModelDriverList> data = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<ModelDriverList> getData() {
        return data;
    }

    public void setData(List<ModelDriverList> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}