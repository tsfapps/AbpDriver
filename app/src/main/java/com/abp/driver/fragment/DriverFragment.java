package com.abp.driver.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.model.driver.ModelDriverList;
import com.abp.driver.service.LocationService;
import com.abp.driver.utils.CustomLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DriverFragment extends Fragment {

    private static final String TAG = "DriverFragment";
    private  DashboardActivity mActivity;
    private  FragmentManager mFragmentManger;
    private View view;
    @BindView(R.id.tv_user_name)
    TextView mUserName;
    private int REQUEST_LOCATION = 1;
    private double mLatitude;
    private double mLongitude;
  //  private ModelDriverList modelDriver;


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
       ModelDriverList modelDriverList = new ModelDriverList();
        Intent intent=new Intent(getContext(), LocationService.class);
        getActivity().startService(intent);

        mActivity = (DashboardActivity) getActivity();
        mActivity.setToolbarTitle("Driver");
        mFragmentManger = mActivity.getSupportFragmentManager();
        String user = getActivity().getIntent().getExtras().getString("EMAIL");
        mUserName.setText("Mr. "+user);
    }

    private void getLocation() {
        CustomLog.d(TAG,"getLocation called");
        if (null != LocationService.getCurrentLocation()) {
            Location location = LocationService.getCurrentLocation();
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            CustomLog.d(TAG,"new Location lat :"+mLatitude+ " long:"+mLongitude);
            Toast.makeText(getContext(),"new lat: "+mLatitude+" long: "+mLongitude,Toast.LENGTH_SHORT).show();
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
}
