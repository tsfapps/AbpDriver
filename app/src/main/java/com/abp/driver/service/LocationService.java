package com.abp.driver.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.abp.driver.utils.Constant;
import com.abp.driver.utils.CustomLog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;


public class LocationService extends Service implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    public static long updateInterval = Constant.FUSED_API_DEFAULT_VALUE;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected static Location mCurrentLocation = null;
    protected static final String TAG = "LocationService";



    /**
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;

   



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        CustomLog.d(TAG, "on create ");
        mRequestingLocationUpdates = false;
        buildGoogleApiClient();
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        CustomLog.e(TAG, "onStartCommand  " + " interval time " + updateInterval);
        return START_STICKY;
    }


    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        CustomLog.d(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
       onStart();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(updateInterval);
        mLocationRequest.setFastestInterval(updateInterval);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        startLocationUpdates();
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
            CustomLog.e(TAG,e.toString());
        }
    }


    protected void onStart() {
        try {

            mGoogleApiClient.connect();
            ConnectionCallbacks callbacks=new ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                  CustomLog.d(TAG,"ConnectionCallbacks onConnected");
                    createLocationRequest();
                }

                @Override
                public void onConnectionSuspended(int i) {

                }
            };
            mGoogleApiClient.registerConnectionCallbacks(callbacks);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CustomLog.d(TAG, "onDestroy called. stop location service");
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        CustomLog.d(TAG, "onConnected");
        if (mCurrentLocation == null) {
            try {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                onReceiveLocation(mCurrentLocation);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("onConnectionSuspended", "onConnected");
    }

    @Override
    public void onLocationChanged(Location location) {
       CustomLog.d(TAG, "onLocationChanged connected");
        if (location != null) {
            CustomLog.d(TAG, "onLocationChanged accuracy" + location.getAccuracy());
            mCurrentLocation = location;
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            onReceiveLocation(mCurrentLocation);
            CustomLog.e("location interval ","interval "+mLocationRequest.getInterval()+" fastest interval "+mLocationRequest.getFastestInterval());
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        CustomLog.d(TAG, "onConnected");
    }

    public void onReceiveLocation(Location location){

    }

    public static Location getCurrentLocation() {
        return mCurrentLocation;
    }



}
