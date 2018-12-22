package com.abp.driver.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.utils.CustomLog;
import com.github.akashandroid90.googlesupport.location.AppLocationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DriverFragment extends AppLocationFragment {

    private static final String TAG = "DriverFragment";
    private  DashboardActivity mActivity;
    private  FragmentManager mFragmentManger;
    private View view;
    @BindView(R.id.tv_user_name)
    TextView mUserName;
    private int REQUEST_LOCATION = 1;

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
        mActivity = (DashboardActivity) getActivity();
        mActivity.setToolbarTitle("Driver");
        mFragmentManger = mActivity.getSupportFragmentManager();
        String user = getActivity().getIntent().getExtras().getString("EMAIL");
        mUserName.setText("Mr. "+user);
    }

    @Override
    public void onResume() {
        super.onResume();
        CustomLog.d(TAG,"onResume called");
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
    public void newLocation(Location location) {
        if (location != null) {
            CustomLog.d(TAG,"new Location lat :"+location.getLatitude()+ " long:"+location.getLongitude());
        }
    }

    @Override
    public void myCurrentLocation(Location currentLocation) {
        if (currentLocation != null) {
            CustomLog.d(TAG,"myCurrentLocation lat :"+currentLocation.getLatitude()+ " long:"+currentLocation.getLongitude());
        }
    }
}
