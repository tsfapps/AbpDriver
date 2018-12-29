package com.abp.driver.model.StateManagerModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class ModelManagement {


    @SerializedName("data")
    @Expose
    private List<ModelManagementList> data = null;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;

    public List<ModelManagementList> getData() {
        return data;
    }

    public void setData(List<ModelManagementList> data) {
        this.data = data;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

}
