package com.abp.driver.model.driver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DriverAttendance {


    @SerializedName("data")
    @Expose
    private List<DriverAttendanceList> data = null;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;

    public List<DriverAttendanceList> getData() {
        return data;
    }

    public void setData(List<DriverAttendanceList> data) {
        this.data = data;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

}
