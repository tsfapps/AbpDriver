package com.abp.driver.main;

import android.content.Context;

import com.abp.driver.utils.CustomLog;
import com.orm.SugarApp;

public class AbpApplication extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            SugarApp.getSugarContext().onCreate();
        } catch (Exception e) {
            CustomLog.e("AbpApplication",e.toString());
        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        try {
            SugarApp.getSugarContext().onTerminate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
