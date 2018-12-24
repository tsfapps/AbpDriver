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
