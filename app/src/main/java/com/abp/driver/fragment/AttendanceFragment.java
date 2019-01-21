package com.abp.driver.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceFragment extends Fragment {

    private static final String TAG = "AttendanceFragment";
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
    @BindView(R.id.tv_pass_code)
    protected TextView mTvPassCode;
    @BindView(R.id.iv_copy)
    protected ImageView mIvCopy;
    @BindView(R.id.tv_shiftTime)
    protected TextView mTvShiftTime;
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
    private Handler mTimerTaskHandler = null;
    private Runnable mRunnable = null;
    private String mCheckInCode = null;
    private String mCheckOutCode = null;
    private Dialog mPassCodeDialog = null;
    private final String WORK_IN_PROGRESS = "In progress";
    private final String WORK_COMPLETED = "Completed";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CustomLog.d(TAG,"onCreateView called");
        View view = inflater.inflate(R.layout.fragment_attendance,container,false);
        ButterKnife.bind(this, view);
        Intent intent=new Intent(getContext(), LocationService.class);
        getActivity().startService(intent);

        mActivity = (DashboardActivity) getActivity();
        mActivity.setToolbarTitle("Attendance");
        mFragmentManger = mActivity.getSupportFragmentManager();
        mSharedPreference = new SharedPreference(getContext());
        List<ModelLoginList> loginList = ModelLoginList.listAll(ModelLoginList.class);
        if (loginList.size() > 0) {
            String mShift = loginList.get(0).getDriverShift();
            if (mShift != null && !mShift.equals("")) {
                if (mShift.equals("morning")) {
                    mTvShiftTime.setText("06:00 Am से 02:00 Pm");
                } else if (mShift.equals("afternoon")) {
                    mTvShiftTime.setText("02:00 Pm से 10:00 Pm");
                } else if (mShift.equals("evening")) {
                    mTvShiftTime.setText("10:00 Pm से 06:00 Am");
                }
            } else {
                mTvShiftTime.setText("असाइन नहीं किया गया");
            }
        } else {
            mTvShiftTime.setText("असाइन नहीं किया गया");
        }
        getDataFromServer();
        return view;
    }

    private void getDataFromServer() {
        CustomLog.d(TAG,"getDataFromServer called");
        if (mActivity.isNetworkAvailable()) {
            attendanceServerData = DriverAttendanceList.listAll(DriverAttendanceList.class);
            if (attendanceServerData.size() > 0 && !attendanceServerData.get(attendanceServerData.size() - 1).getTimeIn().equals("") && attendanceServerData.get(attendanceServerData.size() - 1).getTimeOut().equals("")) {
                init();
                callAttendanceDetailApi();
            } else {
                callAttendanceDetailApi();
                init();
            }
        } else {
            init();
        }

        if (mSharedPreference.getUserLoginType().equals(Constant.LOGIN_TYPE_DRIVER)){
            mTvPassCode.setVisibility(View.VISIBLE);
            mIvCopy.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        CustomLog.d(TAG,"init called");
        attendanceServerData = DriverAttendanceList.listAll(DriverAttendanceList.class);
        if (mActivity.isNetworkAvailable() && attendanceServerData.size() > 0 && !attendanceServerData.get(attendanceServerData.size() - 1).getTimeIn().equals("") && attendanceServerData.get(attendanceServerData.size() - 1).getTimeOut().equals("")) {
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
                    //ModelPunchInOutLocal.deleteAll(ModelPunchInOutLocal.class);
                }
            } else {
                punchType = "check_in";
                mBtnPunchInOut.setText("Punch In");
            }
            setValueOfViewFromLocal();
            if (mActivity.isNetworkAvailable() && attendanceServerData.size() > 0) {
                mCheckOutCode = attendanceServerData.get(attendanceServerData.size() -1).getCheckOutCode();
                mTvPassCode.setText("Your Pass code : " +mCheckOutCode);
                showCopyIcon();
            } else {
                if (punchInOutLocalDetails.size() > 0) {
                    mCheckOutCode = punchInOutLocalDetails.get(punchInOutLocalDetails.size() - 1).getCheckOutCode();
                    mTvPassCode.setText("Your Pass code : " + mCheckOutCode);
                    showCopyIcon();
                }
            }
        }

       /* if (punchType.equals("check_out")) {
            startTimer();
        } else {
            stopTimerTask();
        }*/

    }

    private void showCopyIcon(){
        if (mSharedPreference.getUserLoginType().equals(Constant.LOGIN_TYPE_DRIVER)) {
            mIvCopy.setVisibility(View.VISIBLE);
        } else {
            mIvCopy.setVisibility(View.GONE);
        }
    }

    private void setValueOfViewFromLocal() {
        mUserName.setText("Mr. " +mSharedPreference.getUserName());
        punchInOutLocalDetails = ModelPunchInOutLocal.listAll(ModelPunchInOutLocal.class);
        if (punchInOutLocalDetails.size() > 0) {
            ModelPunchInOutLocal mLastData = punchInOutLocalDetails.get(punchInOutLocalDetails.size() - 1);
            if (!mLastData.getTimeIn().equals("") && mLastData.getTimeOut().equals("")) {
                mTimeIn = mLastData.getCheckInDate();
                //mTotalHours.setText(DateUtil.timeDiff(mLastData.getCheckInDate(), DateUtil.getCurrentDateTime(), Constant.HOUR_SUFFIX, true));
                mTotalHours.setText(WORK_IN_PROGRESS);
                mCheckInDesc.setText("You clocked in at ");
                mCheckInTime.setVisibility(View.VISIBLE);
                mCheckInTime.setText(mLastData.getTimeIn());
                mCheckInCode = mLastData.getCheckInCode();
                mTvPassCode.setText("Pass code not generated yet !");
                mIvCopy.setVisibility(View.GONE);
            } else {
                mTotalHours.setText(WORK_COMPLETED);
                mCheckInDesc.setText("You not yet clocked ");
                mCheckInTime.setVisibility(View.GONE);
                mCheckOutCode = mLastData.getCheckOutCode();
                mTvPassCode.setText("Your Pass code : "+mCheckOutCode);
                showCopyIcon();
            }
        } else {
            mTotalHours.setText(WORK_COMPLETED);
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
                //mTotalHours.setText(DateUtil.timeDiff(mLastData.getCheckInDate(), DateUtil.getCurrentDateTime(), Constant.HOUR_SUFFIX, true));
                mTotalHours.setText(WORK_IN_PROGRESS);
                mCheckInDesc.setText("You clocked in at ");
                mCheckInTime.setVisibility(View.VISIBLE);
                mCheckInTime.setText(mLastData.getTimeIn());
                mCheckInCode = mLastData.getCheckInCode();
                mTvPassCode.setText("Pass code not generated yet !");
                mIvCopy.setVisibility(View.GONE);
            } else {
                mTotalHours.setText(WORK_COMPLETED);
                mCheckInDesc.setText("You not yet clocked ");
                mCheckInTime.setVisibility(View.GONE);
                mCheckOutCode = mLastData.getCheckOutCode();
                mTvPassCode.setText("Your Pass code : "+mCheckOutCode);
                showCopyIcon();
            }
        } else {
            mTotalHours.setText(WORK_COMPLETED);
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
                if (mBtnPunchInOut.getText().toString().equals("Punch In")) {
                    if (mSharedPreference.getUserLoginType().equals(Constant.LOGIN_TYPE_DRIVER)) {
                        showPassCodeDialog();
                    } else {
                        mCheckInCode = "000000";
                        callPunchInOutApi(punchType,"");
                    }
                } else {
                    if (mSharedPreference.getUserLoginType().equals(Constant.LOGIN_TYPE_DRIVER)) {
                        mCheckOutCode = getRandomNumberString().toUpperCase();
                        if (mCheckOutCode != null) {
                            callPunchInOutApi(punchType, mCheckOutCode);
                        } else {
                            Toast.makeText(getContext(), "Error generating pass code, Please retry.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mCheckOutCode = "000000";
                        callPunchInOutApi(punchType, mCheckOutCode);
                    }
                }

            }
        } else {
            Toast.makeText(getContext(),"Error fetching GPS location, please try again.",Toast.LENGTH_SHORT).show();
        }
    }

    private void showPassCodeDialog(){
        if (mPassCodeDialog != null ){
            mPassCodeDialog = null;
        }
         mPassCodeDialog = new Dialog(getContext());
         mPassCodeDialog.setCancelable(true);
         mPassCodeDialog.setCanceledOnTouchOutside(false);
         mPassCodeDialog.setContentView(R.layout.custom_dailog_code);
        final EditText mEtCode = (EditText) mPassCodeDialog.findViewById(R.id.et_pass_code);
         mEtCode.requestFocusFromTouch();
         mEtCode.setFilters(new InputFilter[] {new InputFilter.AllCaps(),new InputFilter.LengthFilter(6)});
         Button mSubmit = (Button) mPassCodeDialog.findViewById(R.id.btn_code_submit);
         Button mCancel = (Button) mPassCodeDialog.findViewById(R.id.btn_code_cancel);

         mSubmit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String mCode = mEtCode.getText().toString().toUpperCase();
                 if (mCode!= null && !mCode.equals("")) {
                     if (mBtnPunchInOut.getText().toString().equals("Punch In")){
                         mCheckInCode = mCode;
                     }
                     hidePassCodeDialog();
                     callPunchInOutApi(punchType,"");
                 } else {
                     mEtCode.setError("Please enter pass code");
                 }
             }
         });

         mCancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 hidePassCodeDialog();
             }
         });
         mPassCodeDialog.show();

    }

    private void hidePassCodeDialog() {
        if (mPassCodeDialog != null && mPassCodeDialog.isShowing()) {
            Log.d(TAG,"code IN :"+mCheckInCode +" out :"+mCheckOutCode);
            mPassCodeDialog.dismiss();
        }
    }

    private void callPunchInOutApi(final String type, final String checkOutCode) {
        Log.d(TAG,"callPunchInOutApi called,, type :"+type+" checkOutCode :"+checkOutCode+" checkIncode :"+mCheckInCode);
        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Attendance "+punchType+" in progress....");
        mDialog.show();
        ModelPunchInOutLocal mModelValue = null;
        String mTypeIo = type;
        final String mPhoneNo = mSharedPreference.getUserPhoneNo();
        final String mEvrId;
        if (mSharedPreference.getUserLoginType().equals(Constant.LOGIN_TYPE_DRIVER)) {
            mEvrId = mSharedPreference.getUserEvrId();
        } else {
            mEvrId = "";
        }
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
                    mLatitudeOut, mCheckInDate, mCheckOutDate,mCheckInCode, checkOutCode,mEvrId);
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
                    try {
                        assert modelPunchInOut != null;
                        if (modelPunchInOut.getSTATUS().equals(Constant.SUCCESS_CODE)) {
                            CustomLog.d(TAG, "callPunchInOutApi response success.");
                            mTimeIn = finalMCheckInDate;
                            saveAttendanceDetailsLocal(type, mPhoneNo, finalMInTime, finalMOutTime, finalMTotalTime, finalMLongitudeIn, finalMLongitudeOut, finalMLatitudeIn, finalMLatitudeOut, finalMCheckInDate, finalMCheckOutDate,true,isCheckIn,isCheckOut,
                                    mCheckInCode, checkOutCode,mEvrId);
                            startApiHandler();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ModelPunchInOut> call, Throwable t) {
                    CustomLog.d(TAG, "callPunchInOutApi onFailure called..." + call.toString());
                    mTimeIn = finalMCheckInDate;
                    saveAttendanceDetailsLocal(type, mPhoneNo, finalMInTime, finalMOutTime, finalMTotalTime, finalMLongitudeIn, finalMLongitudeOut, finalMLatitudeIn, finalMLatitudeOut, finalMCheckInDate, finalMCheckOutDate,false,isCheckIn, isCheckOut,
                            mCheckInCode, checkOutCode,mEvrId);
                    startApiHandler();
                }
            });
        } else {
            Toast.makeText(getContext(),"No Internet available",Toast.LENGTH_SHORT).show();
            mTimeIn = mCheckInDate;
            saveAttendanceDetailsLocal(type, mPhoneNo, mInTime, mOutTime, mTotalTime, mLongitudeIn, mLongitudeOut, mLatitudeIn, mLatitudeOut, mCheckInDate, mCheckOutDate,false, isCheckIn, isCheckOut,
                    mCheckInCode, checkOutCode,mEvrId);
            startApiHandler();
        }
    }

    private void saveAttendanceDetailsLocal(String type, String mPhoneNo, String mInTime, String mOutTime, String mTotalTime, String mLongitudeIn, String mLongitudeOut, String mLatitudeIn, String mLatitudeOut,
                                            String mCheckInDate, String mCheckOutDate, boolean isSynced, boolean isCheckIn, boolean isCheckOut, String checkInCode,String checkOutCode, String evrId) {
        CustomLog.d(TAG,"saveAttendanceDetailsLocal called.. isSynced :"+isSynced+" type :"+type);
        if (type.equals("check_in")) {
            ModelPunchInOutLocal model = new ModelPunchInOutLocal();
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
            model.setCheckInCode(checkInCode);
            model.setCheckOutCode(checkOutCode);
            model.setEvrId(evrId);
            if (isCheckIn && !isCheckOut) {
                if (isSynced) {
                    model.setIsCheckInSynced("Y");
                    model.setIsSynced("Y");
                } else {
                    model.setIsCheckInSynced("N");
                    model.setIsSynced("N");
                }
            }
            model.save();
        } else {
            Long id;
            List<ModelPunchInOutLocal> localDetails = ModelPunchInOutLocal.findWithQuery(ModelPunchInOutLocal.class,
                    "SELECT * FROM MODEL_PUNCH_IN_OUT_LOCAL where phone_no = '" + mSharedPreference.getUserPhoneNo() + "' AND time_in != '" + Constant.EMPTY + "'  AND time_out = '" + Constant.EMPTY + "' ORDER BY id DESC");
            if (localDetails.size() > 0) {
                id = localDetails.get(0).getId();
                ModelPunchInOutLocal model = ModelPunchInOutLocal.findById(ModelPunchInOutLocal.class, id);
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
                model.setCheckInCode(checkInCode);
                model.setCheckOutCode(checkOutCode);
                model.setEvrId(evrId);
                if (isCheckIn && isCheckOut) {
                    if (isSynced) {
                        model.setIsCheckOutSynced("Y");
                        model.setIsSynced("Y");
                    } else {
                        model.setIsCheckOutSynced("N");
                        model.setIsSynced("N");
                    }
                }
                model.save();
            } else {
                ModelPunchInOutLocal model = new ModelPunchInOutLocal();
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
                model.setCheckInCode(checkInCode);
                model.setCheckOutCode(checkOutCode);
                model.setEvrId(evrId);
                model.setIsCheckInSynced("Y");
                if (isCheckIn && isCheckOut) {
                    if (isSynced) {
                        model.setIsSynced("Y");
                        model.setIsCheckOutSynced("Y");
                    } else {
                        model.setIsSynced("N");
                        model.setIsCheckOutSynced("N");
                    }
                }
                model.save();
            }
        }
    }

    private void callAttendanceDetailApi() {
        try {
            CustomLog.d(TAG,"callAttendanceDetailApi called");
            String api_key = Constant.API_KEY;
            String phone_no = mSharedPreference.getUserPhoneNo();
            Api api = ApiClients.getApiClients().create(Api.class);
            Call<DriverAttendance> listCall = api.driverAttendance(api_key, phone_no);
            listCall.enqueue(new Callback<DriverAttendance>() {
                @Override
                public void onResponse(Call<DriverAttendance> call, Response<DriverAttendance> response) {
                    try {
                        DriverAttendance driverAttendance = response.body();
                        if (driverAttendance.getSTATUS().equals(Constant.SUCCESS_CODE)) {
                            CustomLog.d(TAG, "callAttendanceDetailApi onResponse called success");
                            DriverAttendanceList.deleteAll(DriverAttendanceList.class);
                            for (DriverAttendanceList driverAttendanceList : driverAttendance.getData()) {
                                driverAttendanceList.save();
                            }
                            init();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onFailure(Call<DriverAttendance> call, Throwable t) {
                    t.printStackTrace();
                    CustomLog.d(TAG, "callAttendanceDetailApi onFailure called");
                    init();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_check_in_out,R.id.iv_copy})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_check_in_out:
                if (mActivity.isGpsEnable()) {
                    getLocation();
                } else {
                    mActivity.showDialogueGps(false);
                }
                break;
            case R.id.iv_copy:
                copyPassCode();
                break;
        }
    }

    private void copyPassCode() {
        CustomLog.d("danny","copyPassCode called");
        if (mCheckOutCode != null) {
            ClipboardManager cm = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(mCheckOutCode);
            Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CustomLog.d(TAG,"onResume called");
        //startTimer();
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
                            if (mSharedPreference.getUserLoginType().equals(Constant.LOGIN_TYPE_DRIVER)) {
                                mActivity.onBackPressedCalled();
                            } else {
                                mFragmentManger.popBackStack();
                            }
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
    public void onPause() {
        super.onPause();
        //stopTimerTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopTimerTask();
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
                if (mBtnPunchInOut.getText().toString().equals("Punch In")) {
                    mBtnPunchInOut.setText("Punch Out");
                } else {
                    mBtnPunchInOut.setText("Punch In");
                    //stopTimerTask();
                }
                mTotalHours.setText(WORK_COMPLETED);
                mCheckInDesc.setText("You clocked in at ");
                mCheckInTime.setVisibility(View.VISIBLE);
                mCheckInTime.setText(DateUtil.getCurrentTime());
                //startTimer();
                getDataFromServer();
                if (mCheckOutCode != null) {
                    mTvPassCode.setText("Your Pass code : " + mCheckOutCode);
                    showCopyIcon();
                }
            }
        }, 500);
    }

    public void startTimer() {
        CustomLog.d(TAG,"startTimer called..");
        if (mBtnPunchInOut.getText().toString().equals("Punch In")) {
            CustomLog.d(TAG,"startTimer : punchType == Punch In ... return");
            //stopTimerTask();
            return;
        }

        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 1000, 1000); //
    }

    private void initializeTimerTask() {
        Log.d(TAG,"initializeTimerTask called ");
        mTimerTaskHandler = new Handler();
        timerTask = new TimerTask() {
            public void run() {
                if (mTimerTaskHandler == null){
                    Log.d(TAG,"initializeTimerTask called return timerHandler = null");
                    return;
                }
                mTimerTaskHandler.post(mRunnable = new Runnable() {
                    public void run() {
                        if (mTimeIn != null) {
                            if (mBtnPunchInOut.getText().toString().equals("Punch Out")) {
                                mTotalHours.setText(DateUtil.timeDiff(mTimeIn, DateUtil.getCurrentDateTime(), Constant.HOUR_SUFFIX, true));
                            } else {
                                mTotalHours.setText("00:00:00");
                            }
                        }
                    }
                });
            }
        };

    }

    public void stopTimerTask() {
        CustomLog.d(TAG,"stopTimerTask called");
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (mTimerTaskHandler != null) {
            mTimerTaskHandler.removeCallbacksAndMessages(mRunnable);
            mTimerTaskHandler = null;
        }
    }

    public String getRandomNumberString() {
        String zeros = "000000";
        Random rnd = new Random();
        String s = Integer.toString(rnd.nextInt(0X1000000), 16);
        s = zeros.substring(s.length()) + s;
        CustomLog.d(TAG,"random no :"+ s);
        return s;
    }

}
