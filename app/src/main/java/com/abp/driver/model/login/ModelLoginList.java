package com.abp.driver.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class ModelLoginList extends SugarRecord {

    @SerializedName("logintype")
    @Expose
    private String logintype;
    @SerializedName("evr_id")
    @Expose
    private String evrId;
    @SerializedName("erv_no")
    @Expose
    private String ervNo;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phoneno")
    @Expose
    private String phoneno;
    @SerializedName("adhaarno")
    @Expose
    private String adhaarno;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("driver_shift")
    @Expose
    private String driverShift;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;

    public String getLogintype() {
        return logintype;
    }

    public void setLogintype(String logintype) {
        this.logintype = logintype;
    }

    public String getEvrId() {
        return evrId;
    }

    public void setEvrId(String evrId) {
        this.evrId = evrId;
    }

    public String getErvNo() {
        return ervNo;
    }

    public void setErvNo(String ervNo) {
        this.ervNo = ervNo;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getAdhaarno() {
        return adhaarno;
    }

    public void setAdhaarno(String adhaarno) {
        this.adhaarno = adhaarno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDriverShift() {
        return driverShift;
    }

    public void setDriverShift(String driverShift) {
        this.driverShift = driverShift;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
