package com.abp.driver.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;


public class SharedPreference {
    private Context mContext;

    public static Editor edt;
    private long mTimeLeft = 60000 * 5;

    public SharedPreference(Context ctx) {
        // TODO Auto-generated constructor stub
        this.mContext = ctx;
    }

    public void clearAllData(){
        android.content.SharedPreferences sp = mContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        edt = sp.edit();
        edt.remove(Constant.USER_NAME);
        edt.remove(Constant.USER_PHONE);
        edt.remove(Constant.USER_PIC);
        edt.remove(Constant.USER_STATE_ID);
        edt.remove(Constant.USER_DISTRICT_ID);
        edt.remove(Constant.USER_LOGIN_TYPE);
        edt.commit();
        edt.clear();
    }

    public void setUserData(String userName, String userPhone, String userPic, String stateId, String districtId, String loginType) {
        android.content.SharedPreferences sp = mContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        edt = sp.edit();
        edt.putString(Constant.USER_NAME, userName);
        edt.putString(Constant.USER_PHONE, userPhone);
        edt.putString(Constant.USER_PIC, userPic);
        edt.putString(Constant.USER_STATE_ID, stateId);
        edt.putString(Constant.USER_DISTRICT_ID, districtId);
        edt.putString(Constant.USER_LOGIN_TYPE, loginType);
        edt.commit();
    }

    public String getUserName() {
        android.content.SharedPreferences sp = mContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.USER_NAME, Constant.EMPTY);
    }
    public String getUserPhoneNo() {
        android.content.SharedPreferences sp = mContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.USER_PHONE, Constant.EMPTY);
    }

    public String getUserPic() {
        android.content.SharedPreferences sp = mContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.USER_PIC, Constant.EMPTY);
    }

    public String getUserStateId() {
        android.content.SharedPreferences sp = mContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.USER_STATE_ID, Constant.EMPTY);
    }

    public String getUserDistrictId() {
        android.content.SharedPreferences sp = mContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.USER_DISTRICT_ID, Constant.EMPTY);
    }

    public String getUserLoginType() {
        android.content.SharedPreferences sp = mContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.USER_LOGIN_TYPE, Constant.EMPTY);
    }



    public void setPermissionSettingPage(boolean value) {
        android.content.SharedPreferences sp = mContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        edt = sp.edit();
        edt.putBoolean(Constant.PREF_KEY_PERMISSION_SETTING, value);
        edt.commit();
    }

    public boolean getPermissionSettingPage() {
        android.content.SharedPreferences sp = mContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(Constant.PREF_KEY_PERMISSION_SETTING, false);
    }

    public void setCountLeftTime(long leftTime) {
        android.content.SharedPreferences sp = mContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        edt = sp.edit();
        edt.putLong(Constant.PREF_KEY_COUNT_TIME_LEFT, leftTime);
        edt.commit();
    }

    public long getCountLeftTime(){
        android.content.SharedPreferences sp = mContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getLong(Constant.PREF_KEY_COUNT_TIME_LEFT, mTimeLeft);
    }

}
