package com.abp.driver.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class StateManagerFragment extends Fragment {

    private DashboardActivity mActivity;
    private FragmentManager mFragmentManager;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_state, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mActivity = (DashboardActivity) getActivity();
        if (mActivity != null) {
            mActivity.setToolbarTitle("State Manager");
        }
        mFragmentManager = mActivity.getSupportFragmentManager();
    }
    @OnClick(R.id.ll_sm_attendance)
    public void llSmAttendanceClk(View view){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new DriverFragment()).addToBackStack(null).commit();
    }

    @OnClick(R.id.ll_sm_status)
    public void llSmStatusClk(View view){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new StatusDistrictListFragment()).addToBackStack(null).commit();
    }

    @OnClick(R.id.ll_sm_evr)
    public void llSmEvrClk(View view){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new EvrDistrictListFragment()).addToBackStack(null).commit();
    }

    @OnClick(R.id.ll_sm_profile)
    public void onSmProClk(View view){
//        Fragment tFragment = new ProfileFragmentState();
//        FragmentManager tFragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction tFragmentTransaction = tFragmentManager.beginTransaction();
//        tFragmentTransaction.replace(R.id.container_main, tFragment);
//        tFragmentTransaction.addToBackStack(null);
//        tFragmentTransaction.commit();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new ProfileFragmentState()).addToBackStack(null).commit();
    }
    @Override
    public void onResume() {
        super.onResume();
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
            Log.e("error",""+e);
        }
    }
}
