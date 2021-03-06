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
    private String sTATUS;
    @SerializedName("message")
    @Expose
    private String message;

    public List<ModelLoginList> getData() {
        return data;
    }

    public void setData(List<ModelLoginList> data) {
        this.data = data;
    }

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