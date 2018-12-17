package com.abp.driver.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedprefManager {

    private static final String SHARED_PREF_NAME = "AbpDrive";
    private Context mCtx;


    public boolean isLoggedIn(){
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return preferences.getInt("user_id", -1) != -1;
    }
}
