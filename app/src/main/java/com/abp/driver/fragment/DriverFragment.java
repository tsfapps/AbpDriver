package com.abp.driver.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abp.driver.ApiClient.ApiClients;
import com.abp.driver.Interface.Api;
import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.model.PunchInOut.ModelPunchInOut;
import com.abp.driver.model.PunchInOut.ModelPunchInOutLocal;
import com.abp.driver.model.login.ModelLoginList;
import com.abp.driver.service.LocationService;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.CustomLog;
import com.abp.driver.utils.DateUtil;
import com.abp.driver.utils.SharedPreference;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverFragment extends Fragment {

    private static final String TAG = "DriverFragment";
    private  DashboardActivity mActivity;
    private  FragmentManager mFragmentManger;
    private View view;
    @BindView(R.id.tv_user_name)
    protected TextView mUserName;
    @BindView(R.id.btn_check_in_out)
    protected Button mBtnPunchInOut;
    private int REQUEST_LOCATION = 1;
    private double mLatitude;
    private double mLongitude;
    private List<ModelLoginList> mLoginList;
    private SharedPreference mSharedPreference;
    private List<ModelPunchInOutLocal> punchInOutLocalDetails;
    private String punchType = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver,container,false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        CustomLog.d(TAG,"init called");
        //mLoginList = ModelLoginList.listAll(ModelLoginList.class);
        Intent intent=new Intent(getContext(), LocationService.class);
        getActivity().startService(intent);

        mActivity = (DashboardActivity) getActivity();
        mActivity.setToolbarTitle("Driver");
        mFragmentManger = mActivity.getSupportFragmentManager();
        mSharedPreference = new SharedPreference(getContext());
        mUserName.setText("Mr. " +mSharedPreference.getUserName());
        punchInOutLocalDetails = ModelPunchInOutLocal.listAll(ModelPunchInOutLocal.class);
        if (punchInOutLocalDetails.size() > 0) {
            Gson gson = new Gson();
            Log.d("dannyali","init() list ::"+ gson.toJson(punchInOutLocalDetails));
            if (punchInOutLocalDetails.get(punchInOutLocalDetails.size() - 1).getTimeOut().equals("") && punchInOutLocalDetails.get(punchInOutLocalDetails.size() - 1).getStatus().equals("check_in")) {
                punchType = "check_out";
                mBtnPunchInOut.setText("Punch Out");
            } else {
                punchType = "check_in";
                mBtnPunchInOut.setText("Punch In");
            }
        } else {
            punchType = "check_in";
            mBtnPunchInOut.setText("Punch In");
        }

    }

    private void getLocation() {
        CustomLog.d(TAG,"getLocation called");
        if (null != LocationService.getCurrentLocation()) {
            Location location = LocationService.getCurrentLocation();
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            CustomLog.d(TAG,"new Location lat :"+mLatitude+ " long:"+mLongitude);
            Toast.makeText(getContext(),"new lat: "+mLatitude+" long: "+mLongitude,Toast.LENGTH_SHORT).show();
            if (punchType != null) {
                callPunchInOutApi(punchType);
            }
        }
    }

    private void callPunchInOutApi(String type) {
        ModelPunchInOutLocal mModelValue = null;
        String mTypeIO = type;
        String mPhoneNo = mSharedPreference.getUserPhoneNo();
        String mInTime = "";
        String mOutTime = "";
        String mTotalTime = "";
        String mLongitudeIn = "";
        String mLongitudeOut = "";
        String mLatitudeIn = "";
        String mLatitudeOut = "";
        String mCheckInDate = "";
        String mCheckOutDate = "";

        if (type.equals("check_in")) {
            mInTime = DateUtil.getCurrentTime();
            mLatitudeIn = String.valueOf(mLatitude);
            mLongitudeIn = String.valueOf(mLongitude);
            mCheckInDate = DateUtil.getCurrentDateTime();
            mModelValue = saveAttendanceDetailsLocal(type,mPhoneNo,mInTime,mOutTime,mTotalTime,mLongitudeIn,mLongitudeOut,mLatitudeIn,mLatitudeOut,mCheckInDate,mCheckOutDate);
        } else {
            List<ModelPunchInOutLocal> localDetails = ModelPunchInOutLocal.findWithQuery(ModelPunchInOutLocal.class,
                    "SELECT * FROM MODEL_PUNCH_IN_OUT_LOCAL where phone_no = '" + mSharedPreference.getUserPhoneNo() + "' AND time_out = '" + Constant.EMPTY + "' AND status = 'check_in' ORDER BY id DESC");
            if (localDetails.size() > 0) {
                mInTime = localDetails.get(0).getTimeIn();
                mLatitudeIn = localDetails.get(0).getLatitudeIn();
                mLongitudeIn = localDetails.get(0).getLongitudeIn();
                mCheckInDate = localDetails.get(0).getCheckInDate();
                mLatitudeOut = String.valueOf(mLatitude);
                mLongitudeOut = String.valueOf(mLongitude);
                mOutTime = DateUtil.getCurrentTime();
                mCheckOutDate = DateUtil.getCurrentDateTime();
                mTotalTime = DateUtil.timeDiff(mInTime, mOutTime, Constant.HOUR_SUFFIX);
                Log.d("dannyali","time diff : mCheckInDate :"+mCheckInDate+" mCheckOutDate:"+mCheckOutDate+ " total time :"+ mTotalTime);
                mModelValue = saveAttendanceDetailsLocal(type, mPhoneNo, mInTime, mOutTime, mTotalTime, mLongitudeIn, mLongitudeOut, mLatitudeIn, mLatitudeOut, mCheckInDate, mCheckOutDate);
            }
        }

        if (mActivity.isNetworkAvailable()) {
            Api api = ApiClients.getApiClients().create(Api.class);
            Call<ModelPunchInOut> call = api.driverPunchInOut(Constant.API_KEY, mTypeIO, mPhoneNo, mInTime, mOutTime, mTotalTime, mLongitudeIn, mLongitudeOut, mLatitudeIn,
                    mLatitudeOut, mCheckInDate, mCheckOutDate);
            final ModelPunchInOutLocal finalMModelValue = mModelValue;
            call.enqueue(new Callback<ModelPunchInOut>() {
                @Override
                public void onResponse(Call<ModelPunchInOut> call, Response<ModelPunchInOut> response) {
                    ModelPunchInOut modelPunchInOut = response.body();
                    if (modelPunchInOut.getSTATUS().equals(Constant.SUCCESS_CODE)) {
                        CustomLog.d("danny", "response success.");
                        finalMModelValue.setIsSynced("Y");
                        finalMModelValue.save();
                    }
                }

                @Override
                public void onFailure(Call<ModelPunchInOut> call, Throwable t) {
                    CustomLog.d("danny", "error..." + call.toString());
                    finalMModelValue.setIsSynced("N");
                    finalMModelValue.save();
                }
            });
        } else {
            Toast.makeText(getContext(),"No Internet available",Toast.LENGTH_SHORT).show();
        }
    }

    private ModelPunchInOutLocal saveAttendanceDetailsLocal(String type, String mPhoneNo, String mInTime, String mOutTime, String mTotalTime, String mLongitudeIn, String mLongitudeOut, String mLatitudeIn, String mLatitudeOut,
                                            String mCheckInDate, String mCheckOutDate) {
        ModelPunchInOutLocal model = null;
        if (type.equals("check_in")) {
            model = new ModelPunchInOutLocal();
            model.setPhoneNo(mPhoneNo);
            model.setTimeIn(mInTime);
            model.setTimeOut(mOutTime);
            model.setTotalTime(mTotalTime);
            model.setLongitudeIn(mLongitudeIn);
            model.setLongitudeOut(mLongitudeOut);
            model.setLatitudeIn(mLatitudeIn);
            model.setLatitudeOut(mLatitudeOut);
            model.setCheckInDate(mCheckInDate);
            model.setCheckOutDate(mCheckOutDate);
            model.setStatus(type);
            model.setIsSynced("N");
            model.save();
        } else {
            Long id;
            List<ModelPunchInOutLocal> localDetails = ModelPunchInOutLocal.findWithQuery(ModelPunchInOutLocal.class,
                    "SELECT * FROM MODEL_PUNCH_IN_OUT_LOCAL where phone_no = '" + mSharedPreference.getUserPhoneNo() + "' AND time_out = '" + Constant.EMPTY + "' AND status = 'check_in' ORDER BY id DESC");
            if (localDetails.size() > 0) {
                id = localDetails.get(0).getId();
                model = ModelPunchInOutLocal.findById(ModelPunchInOutLocal.class, id);
                model.setPhoneNo(mPhoneNo);
                model.setTimeIn(mInTime);
                model.setTimeOut(mOutTime);
                model.setTotalTime(mTotalTime);
                model.setLongitudeIn(mLongitudeIn);
                model.setLongitudeOut(mLongitudeOut);
                model.setLatitudeIn(mLatitudeIn);
                model.setLatitudeOut(mLatitudeOut);
                model.setCheckInDate(mCheckInDate);
                model.setCheckOutDate(mCheckOutDate);
                model.setStatus(type);
                model.setIsSynced("N");
                model.save();
            }
            Gson gson = new Gson();
            Log.d("dannyali","attendance local list :: "+ gson.toJson(ModelPunchInOutLocal.listAll(ModelPunchInOutLocal.class)));
        }
        return model;
    }

    @OnClick(R.id.btn_check_in_out)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_check_in_out:
                if (mActivity.isGpsEnable()) {
                    getLocation();
                } else {
                    mActivity.showDialogueGps(false);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CustomLog.d(TAG,"onResume called");
        if (!mActivity.isGpsEnable()) {
            mActivity.showDialogueGps(false);
        }
        try {
            if (getView() != null) {
                getView().setFocusableInTouchMode(true);
                getView().requestFocus();
                getView().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                            mActivity.onBackPressedCalled();
                        }
                        return true;
                    }
                });
            }
        } catch (Exception e) {
            CustomLog.e("error",""+e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent=new Intent(getContext(), LocationService.class);
        getActivity().stopService(intent);
    }
}
