package com.abp.driver.model.date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class ModelDateList extends SugarRecord {
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("stateid")
    @Expose
    private String stateid;
    @SerializedName("districtid")
    @Expose
    private String districtid;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    public String getDistrictid() {
        return districtid;
    }

    public void setDistrictid(String districtid) {
        this.districtid = districtid;
    }
}
