package com.abp.driver.model.status;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusDistrict {

    @SerializedName("data")
    @Expose
    private List<StatusDistrictList> data = null;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;

    public List<StatusDistrictList> getData() {
        return data;
    }

    public void setData(List<StatusDistrictList> data) {
        this.data = data;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

}
