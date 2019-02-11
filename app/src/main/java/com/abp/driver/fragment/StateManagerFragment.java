package com.abp.driver.fragment;

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

import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.utils.SharedPreference;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class StateManagerFragment extends Fragment {

    private DashboardActivity mActivity;
    private FragmentManager mFragmentManager;
    private Unbinder unbinder;
    private SharedPreference mSharePref;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_state_manager, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mActivity = (DashboardActivity) getActivity();
        if (mActivity != null) {
            mActivity.setToolbarTitle("State Manager");
        }
        mSharePref = new SharedPreference(getContext());
        mFragmentManager = mActivity.getSupportFragmentManager();
    }
    @OnClick(R.id.ll_sm_attendance)
    public void llSmAttendanceClk(View view){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new AttendanceFragment()).addToBackStack(null).commit();
    }

    @OnClick(R.id.ll_sm_status)
    public void llSmStatusClk(View view){
       // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_main, DistrictListFragment.newInstance(mSharePref.getUserStateId(), "strStatus")).addToBackStack(null).commit();
    }

    @OnClick(R.id.ll_sm_evr)
    public void llSmEvrClk(View view){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_main, DistrictListFragment.newInstance(mSharePref.getUserStateId(), "strErv")).addToBackStack(null).commit();
    }

    @OnClick(R.id.ll_sm_profile)
    public void onSmProClk(View view){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new ProfileFragment()).addToBackStack(null).commit();
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
