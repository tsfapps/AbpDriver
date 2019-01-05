package com.abp.driver.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.abp.driver.ApiClient.ApiClients;
import com.abp.driver.Interface.Api;
import com.abp.driver.model.PunchInOut.ModelPunchInOut;
import com.abp.driver.model.PunchInOut.ModelPunchInOutLocal;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.CustomLog;
import com.abp.driver.utils.SharedPreference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkStateChangeReceiver extends BroadcastReceiver {

    public static final String TAG = "NetworkStateReceiver";
    private boolean isReceived = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isReceived) {
            Log.d(TAG, "onReceive called");
            isReceived = true;
            SharedPreference mSharedPreference = new SharedPreference(context);
            boolean mIsConnected = isConnectedToInternet(context);
            Toast.makeText(context, "network connected : " + mIsConnected, Toast.LENGTH_SHORT).show();
            List<ModelPunchInOutLocal> localDetails = ModelPunchInOutLocal.findWithQuery(ModelPunchInOutLocal.class,
                    "SELECT * FROM MODEL_PUNCH_IN_OUT_LOCAL where phone_no = '" + mSharedPreference.getUserPhoneNo() + "' AND is_synced = 'N' ORDER BY id ASC");
            if (localDetails.size() > 0 && mIsConnected) {
                syncDataToServer(context, localDetails, mIsConnected);
            } else {
                isReceived = false;
            }
        }


    }

    private void syncDataToServer(Context context, List<ModelPunchInOutLocal> localDetails, boolean isConnected) {
        CustomLog.d(TAG,"syncDataToServer called. list size :"+ localDetails.size());
        String mTypeIO = null, mPhoneNo = null, mInTime = null, mOutTime = null, mTotalTime = null, mLongitudeIn = null, mLongitudeOut = null, mLatitudeIn = null,
                mLatitudeOut = null, mCheckInDate = null, mCheckOutDate = null;
        for (int i = 0; i < localDetails.size(); i++) {
            ModelPunchInOutLocal local = localDetails.get(i);
            if (local.getIsCheckInSynced().equals("N")) {
                mTypeIO = "check_in";
            } else {
                mTypeIO = "check_out";
            }
            mPhoneNo = local.getPhoneNo();
            mInTime = local.getTimeIn();
            mOutTime = local.getTimeOut();
            mTotalTime = local.getTotalTime();
            mLongitudeIn = local.getLongitudeIn();
            mLongitudeOut = local.getLongitudeOut();
            mLatitudeIn = local.getLatitudeIn();
            mLatitudeOut = local.getLatitudeOut();
            mCheckInDate = local.getCheckInDate();
            mCheckOutDate = local.getCheckOutDate();
         /* new ApiCallAsyncTask(mTypeIO, mPhoneNo, mInTime, mOutTime, mTotalTime, mLongitudeIn, mLongitudeOut, mLatitudeIn,
                  mLatitudeOut, mCheckInDate, mCheckOutDate,local).execute(); */
            callAttendanceApi(mTypeIO, mPhoneNo, mInTime, mOutTime, mTotalTime, mLongitudeIn, mLongitudeOut, mLatitudeIn,
                  mLatitudeOut, mCheckInDate, mCheckOutDate,local);
        }
    }

    private synchronized void callAttendanceApi(String mTypeIO, String mPhoneNo, String mInTime, String mOutTime, String mTotalTime, String mLongitudeIn, String mLongitudeOut, String mLatitudeIn, String mLatitudeOut, String mCheckInDate, String mCheckOutDate, ModelPunchInOutLocal local) {
        Log.d(TAG,"callAttendanceApi called");
        Api api = ApiClients.getApiClients().create(Api.class);
        Call<ModelPunchInOut> call = api.driverPunchInOut(Constant.API_KEY, mTypeIO, mPhoneNo, mInTime, mOutTime, mTotalTime, mLongitudeIn, mLongitudeOut, mLatitudeIn,
                mLatitudeOut, mCheckInDate, mCheckOutDate);
        final ModelPunchInOutLocal finalModelValue = local;
        call.enqueue(new Callback<ModelPunchInOut>() {
            @Override
            public void onResponse(Call<ModelPunchInOut> call, Response<ModelPunchInOut> response) {
                Log.d(TAG," reponse status:"+ response.body().getSTATUS()+" msg: "+response.body().getMessage());
                ModelPunchInOut modelPunchInOut = response.body();
                if (modelPunchInOut.getSTATUS().equals(Constant.SUCCESS_CODE)) {
                    Log.d(TAG, "api response success.");
                    if (finalModelValue.getTimeOut().equals("")) {
                        finalModelValue.setIsCheckInSynced("Y");
                    } else {
                        finalModelValue.setIsCheckInSynced("Y");
                        finalModelValue.setIsCheckOutSynced("Y");
                        finalModelValue.setIsSynced("Y");
                    }
                    finalModelValue.save();
                    isReceived = false;
                }
            }

            @Override
            public void onFailure(Call<ModelPunchInOut> call, Throwable t) {
                Log.d(TAG, "api response error..." + call.toString());
                t.printStackTrace();
                finalModelValue.setIsSynced("N");
                finalModelValue.save();
                isReceived = false;
            }
        });
    }

    private class ApiCallAsyncTask extends AsyncTask<String, Void, String> {
        String mTypeIO = null, mPhoneNo = null, mInTime = null, mOutTime = null, mTotalTime = null, mLongitudeIn = null, mLongitudeOut = null, mLatitudeIn = null,
                mLatitudeOut = null, mCheckInDate = null, mCheckOutDate = null;
        ModelPunchInOutLocal modelLocal = null;
        public ApiCallAsyncTask(String mTypeIO, String mPhoneNo, String mInTime, String mOutTime, String mTotalTime, String mLongitudeIn, String mLongitudeOut, String mLatitudeIn, String mLatitudeOut, String mCheckInDate, String mCheckOutDate, ModelPunchInOutLocal local) {
        Log.d(TAG,"ApiCallAsyncTask called");
        this.mTypeIO = mTypeIO;
        this.mPhoneNo = mPhoneNo;
        this.mInTime = mInTime;
        this.mOutTime = mOutTime;
        this.mTotalTime = mTotalTime;
        this.mLongitudeIn = mLongitudeIn;
        this.mLongitudeOut = mLongitudeOut;
        this.mLatitudeIn = mLatitudeIn;
        this.mLatitudeOut = mLatitudeOut;
        this.mCheckInDate = mCheckInDate;
        this.mCheckOutDate = mCheckOutDate;
        this.modelLocal = local;
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG,"ApiCallAsyncTask doInBackground called.");
            try {
                Api api = ApiClients.getApiClients().create(Api.class);
                Call<ModelPunchInOut> call = api.driverPunchInOut(Constant.API_KEY, mTypeIO, mPhoneNo, mInTime, mOutTime, mTotalTime, mLongitudeIn, mLongitudeOut, mLatitudeIn,
                        mLatitudeOut, mCheckInDate, mCheckOutDate);
                final ModelPunchInOutLocal finalModelValue = modelLocal;
                call.enqueue(new Callback<ModelPunchInOut>() {
                    @Override
                    public void onResponse(Call<ModelPunchInOut> call, Response<ModelPunchInOut> response) {
                        ModelPunchInOut modelPunchInOut = response.body();
                        if (modelPunchInOut.getSTATUS().equals(Constant.SUCCESS_CODE)) {
                            Log.d(TAG, "response success called.");
                            finalModelValue.setIsSynced("Y");
                            finalModelValue.save();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelPunchInOut> call, Throwable t) {
                        CustomLog.d(TAG, "api error..." + call.toString());
                        finalModelValue.setIsSynced("N");
                        finalModelValue.save();
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
          return null;
        }


        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG,"onPostExecute called");
        }
    }

    private boolean isConnectedToInternet(Context context) {
        try {
            if (context != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            return false;
        } catch (Exception e) {
            Log.e(NetworkStateChangeReceiver.class.getName(), e.getMessage());
            return false;
        }
    }
}