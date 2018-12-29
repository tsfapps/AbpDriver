package com.abp.driver.model.login;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelLogin {

    @SerializedName("data")
    @Expose
    private List<ModelLoginList> data = null;
    @SerializedName("STATUS")
    @Expose
    private String status;

    public List<ModelLoginList> getData() {
        return data;
    }

    public void setData(List<ModelLoginList> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}