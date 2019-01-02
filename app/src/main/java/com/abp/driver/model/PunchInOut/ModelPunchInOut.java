package com.abp.driver.model.PunchInOut;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPunchInOut {

    @SerializedName("STATUS")
    @Expose
    private String sTATUS;
    @SerializedName("message")
    @Expose
    private String message;

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
