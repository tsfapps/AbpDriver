package com.abp.driver.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;


public class SharedPreference {
    private Context mContext;

    public static Editor edt;

    public SharedPreference(Context ctx) {
        // TODO Auto-generated constructor stub
        this.mContext = ctx;
    }
    public void setUserData(String userName, String userPhone, String userPic) {
        android.content.SharedPreferences sp = mContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        edt = sp.edit();
        edt.putString(Constant.USER_NAME, userName);
        edt.putString(Constant.USER_PHONE, userPhone);
        edt.putString(Constant.USER_PIC, userPic);
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

}
