package com.abp.driver.pojo;

import com.google.gson.annotations.SerializedName;

public class ModelDriver {


    @SerializedName("logintype")
    public String logintype;
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("phoneno")
    public String phoneno;
    @SerializedName("adhaarno")
    public String adhaarno;
    @SerializedName("address")
    public String address;
    @SerializedName("user_name")
    public String user_name;
    @SerializedName("password")
    public String password;
    @SerializedName("create_date")
    public String create_date;
    @SerializedName("profile_pic")
    public String profile_pic;


    public ModelDriver() {
    }

    public ModelDriver(String logintype, String phoneno, String password) {
        this.logintype = logintype;
        this.phoneno = phoneno;
        this.password = password;
    }
}
