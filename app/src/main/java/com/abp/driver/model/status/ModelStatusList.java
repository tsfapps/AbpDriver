package com.abp.driver.model.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class ModelStatusList extends SugarRecord {
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("police_station_name")
    @Expose
    private String policeStationName;
    @SerializedName("shifting_time")
    @Expose
    private String shiftingTime;
    @SerializedName("status_on_off")
    @Expose
    private String statusOnOff;
    @SerializedName("erv")
    @Expose
    private String erv;
    @SerializedName("total_working_hour")
    @Expose
    private String totalWorkingHour;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPoliceStationName() {
        return policeStationName;
    }

    public void setPoliceStationName(String policeStationName) {
        this.policeStationName = policeStationName;
    }

    public String getShiftingTime() {
        return shiftingTime;
    }

    public void setShiftingTime(String shiftingTime) {
        this.shiftingTime = shiftingTime;
    }

    public String getStatusOnOff() {
        return statusOnOff;
    }

    public void setStatusOnOff(String statusOnOff) {
        this.statusOnOff = statusOnOff;
    }

    public String getErv() {
        return erv;
    }

    public void setErv(String erv) {
        this.erv = erv;
    }

    public String getTotalWorkingHour() {
        return totalWorkingHour;
    }

    public void setTotalWorkingHour(String totalWorkingHour) {
        this.totalWorkingHour = totalWorkingHour;
    }
}
