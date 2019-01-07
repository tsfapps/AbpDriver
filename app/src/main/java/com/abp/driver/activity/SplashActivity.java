package com.abp.driver.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.abp.driver.R;
import com.abp.driver.service.NetworkStateService;
import com.abp.driver.utils.CustomLog;
import com.abp.driver.utils.SharedPreference;

public class SplashActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    private final String TAG = "SplashActivity";
    private SharedPreference sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharePref = new SharedPreference(this);
        checkLocationPermissions();
        if (!isServiceRunning(NetworkStateService.class)) {
            startNetworkService();
        }
    }

    private void startNetworkService() {
        Intent intent = new Intent(this, NetworkStateService.class);
        startService(intent);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                CustomLog.d(TAG,"NetworkService is running");
                return true;
            }
        }
        CustomLog.d(TAG,"NetworkService is not running");
        return false;
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            startHandler();
        }
    }

    private void startHandler() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharePref.getUserPhoneNo().equalsIgnoreCase("") && sharePref.getUserLoginType().equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    callDashboardClass();
                }
            }
        }, 1500);
    }

    private void callDashboardClass() {
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startHandler();
                } else {
                    CustomLog.d(TAG,"onRequestPermission result denied finishing activity");
                    // todo  need to correct logic..
                  /*  if (sharePref.getPermissionSettingPage()) {
                        openAppPermissionSetting();
                    } else {
                        sharePref.setPermissionSettingPage(true);
                        finish();
                    }*/
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void openAppPermissionSetting(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
