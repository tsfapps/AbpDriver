package com.abp.driver.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
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
import com.abp.driver.model.driver.DriverAttendance;
import com.abp.driver.model.driver.DriverAttendanceList;
import com.abp.driver.model.login.ModelLoginList;
import com.abp.driver.service.LocationService;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.CustomLog;
import com.abp.driver.utils.DateUtil;
import com.abp.driver.utils.SharedPreference;
import com.google.gson.Gson;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    @BindView(R.id.tvWorkingHour)
    protected TextView mTotalHours;
    @BindView(R.id.tv_checkedInTime)
    protected TextView mCheckInTime;
    @BindView(R.id.tv_checkIn_desc)
    protected TextView mCheckInDesc;
    private int REQUEST_LOCATION = 1;
    private double mLatitude;
    private double mLongitude;
    private List<ModelLoginList> mLoginList;
    private SharedPreference mSharedPreference;
    private List<ModelPunchInOutLocal> punchInOutLocalDetails;
    private String punchType = null;
    private boolean isCheckIn = false;
    private boolean isCheckOut = false;
    private ProgressDialog mDialog;
    private List<DriverAttendanceList> attendanceServerData;
    private Timer timer;
    private TimerTask timerTask;
    private String mTimeIn = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver,container,false);
        ButterKnife.bind(this, view);
        Intent intent=new Intent(getContext(), LocationService.class);
        getActivity().startService(intent);

        mActivity = (DashboardActivity) getActivity();
        mActivity.setToolbarTitle("Driver");
        mFragmentManger = mActivity.getSupportFragmentManager();
        mSharedPreference = new SharedPreference(getContext());
        attendanceServerData = DriverAttendanceList.listAll(DriverAttendanceList.class);
       if (attendanceServerData.size()>0 && !attendanceServerData.get(attendanceServerData.size() - 1).getTimeIn().equals("") && attendanceServerData.get(attendanceServerData.size() - 1).getTimeOut().equals("")){
           init();
           callAttendanceDetailApi();
       } else {
           callAttendanceDetailApi();
       }
        return view;
    }

    private void init() {
        CustomLog.d(TAG,"init called");
        attendanceServerData = DriverAttendanceList.listAll(DriverAttendanceList.class);
        if (attendanceServerData.size() > 0 && !attendanceServerData.get(attendanceServerData.size() - 1).getTimeIn().equals("") && attendanceServerData.get(attendanceServerData.size() - 1).getTimeOut().equals("")) {
            punchType = "check_out";
            mBtnPunchInOut.setText("Punch Out");
            setValueOfViewFromServer(attendanceServerData);
        } else {
            punchInOutLocalDetails = ModelPunchInOutLocal.listAll(ModelPunchInOutLocal.class);
            if (punchInOutLocalDetails.size() > 0) {
                if(attendanceServerData.size() > 0 && !attendanceServerData.get(attendanceServerData.size() - 1).getCheckInDate().equals(punchInOutLocalDetails.get(punchInOutLocalDetails.size() - 1).getCheckInDate())) {
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
                    ModelPunchInOutLocal.deleteAll(ModelPunchInOutLocal.class);
                }
            } else {
                punchType = "check_in";
                mBtnPunchInOut.setText("Punch In");
            }
            setValueOfViewFromLocal();
        }
        if (punchType.equals("check_out")) {
            startTimer();
        }
    }

    private void setValueOfViewFromLocal() {
        mUserName.setText("Mr. " +mSharedPreference.getUserName());
        punchInOutLocalDetails = ModelPunchInOutLocal.listAll(ModelPunchInOutLocal.class);
        if (punchInOutLocalDetails.size() > 0) {
            ModelPunchInOutLocal mLastData = punchInOutLocalDetails.get(punchInOutLocalDetails.size() - 1);
            if (!mLastData.getTimeIn().equals("") && mLastData.getTimeOut().equals("")) {
                mTimeIn = mLastData.getCheckInDate();
                mTotalHours.setText(DateUtil.timeDiff(mLastData.getCheckInDate(), DateUtil.getCurrentDateTime(), Constant.HOUR_SUFFIX, true));
            } else {
                mTotalHours.setText("00:00");
            }
            if (!mLastData.getTimeIn().equals("") && mLastData.getTimeOut().equals("")) {
                mCheckInDesc.setText("You clocked in at ");
                mCheckInTime.setVisibility(View.VISIBLE);
                mCheckInTime.setText(mLastData.getTimeIn());
            } else {
                mCheckInDesc.setText("You not yet clocked ");
                mCheckInTime.setVisibility(View.GONE);
            }
        } else {
            mTotalHours.setText("00:00");
            mCheckInDesc.setText("You not yet clocked ");
            mCheckInTime.setVisibility(View.GONE);
        }
    }

    private void setValueOfViewFromServer(List<DriverAttendanceList> attendanceServerData) {
        mUserName.setText("Mr. " +mSharedPreference.getUserName());
        if (attendanceServerData.size() > 0) {
            DriverAttendanceList mLastData = attendanceServerData.get(attendanceServerData.size() - 1);
            if (!mLastData.getTimeIn().equals("") && mLastData.getTimeOut().equals("")) {
                mTimeIn = mLastData.getCheckInDate();
                mTotalHours.setText(DateUtil.timeDiff(mLastData.getCheckInDate(), DateUtil.getCurrentDateTime(), Constant.HOUR_SUFFIX, true));
            } else {
                mTotalHours.setText("00:00");
            }
            if (!mLastData.getTimeIn().equals("") && mLastData.getTimeOut().equals("")) {
                mCheckInDesc.setText("You clocked in at ");
                mCheckInTime.setVisibility(View.VISIBLE);
                mCheckInTime.setText(mLastData.getTimeIn());
            } else {
                mCheckInDesc.setText("You not yet clocked ");
                mCheckInTime.setVisibility(View.GONE);
            }
        } else {
            mTotalHours.setText("00:00");
            mCheckInDesc.setText("You not yet clocked ");
            mCheckInTime.setVisibility(View.GONE);
        }
    }

    private void getLocation() {
        CustomLog.d(TAG,"getLocation called");
        if (null != LocationService.getCurrentLocation()) {
            Location location = LocationService.getCurrentLocation();
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            CustomLog.d(TAG,"new Location lat :"+mLatitude+ " long:"+mLongitude);
            if (punchType != null) {
                callPunchInOutApi(punchType);
            }
        }
    }

    private void callPunchInOutApi(final String type) {
        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Attendance "+punchType+" in progress....");
        mDialog.show();
        ModelPunchInOutLocal mModelValue = null;
        String mTypeIo = type;
        final String mPhoneNo = mSharedPreference.getUserPhoneNo();
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
            isCheckIn = true;
            isCheckOut =false;
            //mModelValue = saveAttendanceDetailsLocal(type,mPhoneNo,mInTime,mOutTime,mTotalTime,mLongitudeIn,mLongitudeOut,mLatitudeIn,mLatitudeOut,mCheckInDate,mCheckOutDate);
        } else {
            attendanceServerData = DriverAttendanceList.listAll(DriverAttendanceList.class);
            if (attendanceServerData.size() > 0 && !attendanceServerData.get(attendanceServerData.size() - 1).getTimeIn().equals("")
                    && attendanceServerData.get(attendanceServerData.size() - 1).getTimeOut().equals("")){
                DriverAttendanceList localDetails = attendanceServerData.get(attendanceServerData.size() - 1);
                mTypeIo = "check_out";
                mInTime = localDetails.getTimeIn();
                mLatitudeIn = localDetails.getLatitudeIn();
                mLongitudeIn = localDetails.getLongitudeIn();
                mCheckInDate = localDetails.getCheckInDate();
                mLatitudeOut = String.valueOf(mLatitude);
                mLongitudeOut = String.valueOf(mLongitude);
                mOutTime = DateUtil.getCurrentTime();
                mCheckOutDate = DateUtil.getCurrentDateTime();
                mTotalTime = DateUtil.timeDiff(mCheckInDate, mCheckOutDate, Constant.HOUR_SUFFIX);
                isCheckIn = true;
                isCheckOut = true;

            } else {
                List<ModelPunchInOutLocal> localDetails = ModelPunchInOutLocal.findWithQuery(ModelPunchInOutLocal.class,
                        "SELECT * FROM MODEL_PUNCH_IN_OUT_LOCAL where phone_no = '" + mSharedPreference.getUserPhoneNo() + "' AND time_out = '" + Constant.EMPTY + "' AND status = 'check_in' ORDER BY id DESC");
                if (localDetails.size() > 0) {
                    if (localDetails.get(0).getIsCheckInSynced().equals("N")) {
                        mTypeIo = "check_in";
                    } else {
                        mTypeIo = "check_out";
                    }
                    mInTime = localDetails.get(0).getTimeIn();
                    mLatitudeIn = localDetails.get(0).getLatitudeIn();
                    mLongitudeIn = localDetails.get(0).getLongitudeIn();
                    mCheckInDate = localDetails.get(0).getCheckInDate();
                    mLatitudeOut = String.valueOf(mLatitude);
                    mLongitudeOut = String.valueOf(mLongitude);
                    mOutTime = DateUtil.getCurrentTime();
                    mCheckOutDate = DateUtil.getCurrentDateTime();
                    mTotalTime = DateUtil.timeDiff(mCheckInDate, mCheckOutDate, Constant.HOUR_SUFFIX);
                    isCheckIn = true;
                    isCheckOut = true;

                }
            }
        }

        if (mActivity.isNetworkAvailable()) {
            Api api = ApiClients.getApiClients().create(Api.class);
            Call<ModelPunchInOut> call = api.driverPunchInOut(Constant.API_KEY, mTypeIo, mPhoneNo, mInTime, mOutTime, mTotalTime, mLongitudeIn, mLongitudeOut, mLatitudeIn,
                    mLatitudeOut, mCheckInDate, mCheckOutDate);
            final ModelPunchInOutLocal finalMModelValue = mModelValue;
            final String finalMInTime = mInTime;
            final String finalMOutTime = mOutTime;
            final String finalMTotalTime = mTotalTime;
            final String finalMLongitudeIn = mLongitudeIn;
            final String finalMLongitudeOut = mLongitudeOut;
            final String finalMLatitudeIn = mLatitudeIn;
            final String finalMLatitudeOut = mLatitudeOut;
            final String finalMCheckInDate = mCheckInDate;
            final String finalMCheckOutDate = mCheckOutDate;
            call.enqueue(new Callback<ModelPunchInOut>() {
                @Override
                public void onResponse(Call<ModelPunchInOut> call, Response<ModelPunchInOut> response) {
                    ModelPunchInOut modelPunchInOut = response.body();
                    if (modelPunchInOut.getSTATUS().equals(Constant.SUCCESS_CODE)) {
                        CustomLog.d(TAG, "callPunchInOutApi response success.");
                        mTimeIn = finalMInTime;
                        saveAttendanceDetailsLocal(type, mPhoneNo, finalMInTime, finalMOutTime, finalMTotalTime, finalMLongitudeIn, finalMLongitudeOut, finalMLatitudeIn, finalMLatitudeOut, finalMCheckInDate, finalMCheckOutDate,true,isCheckIn,isCheckOut);
                        startApiHandler();
                    }
                }

                @Override
                public void onFailure(Call<ModelPunchInOut> call, Throwable t) {
                    CustomLog.d(TAG, "callPunchInOutApi onFailure called..." + call.toString());
                    saveAttendanceDetailsLocal(type, mPhoneNo, finalMInTime, finalMOutTime, finalMTotalTime, finalMLongitudeIn, finalMLongitudeOut, finalMLatitudeIn, finalMLatitudeOut, finalMCheckInDate, finalMCheckOutDate,false,isCheckIn, isCheckOut);
                    startApiHandler();
                }
            });
        } else {
            Toast.makeText(getContext(),"No Internet available",Toast.LENGTH_SHORT).show();
            saveAttendanceDetailsLocal(type, mPhoneNo, mInTime, mOutTime, mTotalTime, mLongitudeIn, mLongitudeOut, mLatitudeIn, mLatitudeOut, mCheckInDate, mCheckOutDate,false, isCheckIn, isCheckOut);
            startApiHandler();
        }
    }

    private void saveAttendanceDetailsLocal(String type, String mPhoneNo, String mInTime, String mOutTime, String mTotalTime, String mLongitudeIn, String mLongitudeOut, String mLatitudeIn, String mLatitudeOut,
                                            String mCheckInDate, String mCheckOutDate, boolean isSynced, boolean isCheckIn, boolean isCheckOut) {
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
            if (isSynced && isCheckIn && !isCheckOut) {
                model.setIsCheckInSynced("Y");
                model.setIsSynced("Y");
            } else {
                model.setIsCheckInSynced("N");
                model.setIsSynced("N");
            }
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
                if (isSynced && isCheckIn && isCheckOut) {
                    model.setIsSynced("Y");
                } else {
                    model.setIsSynced("N");
                }
                if (isSynced) {
                    model.setIsCheckOutSynced("Y");
                } else {
                    model.setIsCheckOutSynced("N");
                }
                model.save();
            }
        }
    }

    private void callAttendanceDetailApi() {
        try {
            String api_key = Constant.API_KEY;
            String phone_no = mSharedPreference.getUserPhoneNo();
            Api api = ApiClients.getApiClients().create(Api.class);
            Call<DriverAttendance> listCall = api.driverAttendance(api_key, phone_no);
            listCall.enqueue(new Callback<DriverAttendance>() {
                @Override
                public void onResponse(Call<DriverAttendance> call, Response<DriverAttendance> response) {
                    DriverAttendance driverAttendance = response.body();
                    if (driverAttendance.getSTATUS().equals(Constant.SUCCESS_CODE)) {
                        CustomLog.d(TAG, "callAttendanceDetailApi onResponse called success");
                        DriverAttendanceList.deleteAll(DriverAttendanceList.class);
                        for (DriverAttendanceList driverAttendanceList : driverAttendance.getData()) {
                            driverAttendanceList.save();
                        }
                        init();
                    }

                }
                @Override
                public void onFailure(Call<DriverAttendance> call, Throwable t) {
                    CustomLog.d(TAG, "callAttendanceDetailApi onFailure called");
                    init();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void startApiHandler() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDialog.isShowing()) {
                    mDialog.cancel();
                }
                if (mBtnPunchInOut.getText().toString().equalsIgnoreCase("Punch In")) {
                    mBtnPunchInOut.setText("Punch Out");
                } else {
                    mBtnPunchInOut.setText("Punch In");
                    stoptimertask();
                }
                mTotalHours.setText("00:00");
                mCheckInDesc.setText("You clocked in at ");
                mCheckInTime.setVisibility(View.VISIBLE);
                mCheckInTime.setText(DateUtil.getCurrentTime());
                startTimer();
            }
        }, 500);
    }

    public void startTimer() {
        Log.d(TAG,"startTimer called ");
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 0, 60000); //
    }

    private void initializeTimerTask() {
        Log.d(TAG,"initializeTimerTask called ");
        final Handler handler = new Handler();
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if (mTimeIn != null) {
                            if (DateUtil.timeDiff(mTimeIn, DateUtil.getCurrentDateTime(), Constant.HOUR_SUFFIX, true).equals("0 : 0")) {
                                mTotalHours.setText("0 : 0");
                            } else {
                                mTotalHours.setText(DateUtil.timeDiff(mTimeIn, DateUtil.getCurrentDateTime(), Constant.HOUR_SUFFIX, true));
                            }
                        }
                    }
                });
            }
        };

    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stoptimertask();
    }
}
