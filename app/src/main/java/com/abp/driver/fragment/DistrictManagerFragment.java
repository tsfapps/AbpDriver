package com.abp.driver.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.utils.CustomLog;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DistrictManagerFragment extends Fragment {

    private DashboardActivity mActivity;
    private FragmentManager mFragmentManager;
    private LinearLayout ll_profile_dm;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_district, container, false);
        unbinder = ButterKnife.bind(this , view);
        init();
        return view;
    }

    @OnClick(R.id.ll_profile_dm)
    public void llProfileClk(View view){

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new ProfileFragmentDistrict()).addToBackStack(null).commit();

      //  Fragment tFragment = new ProfileFragmentDistrict();
//        FragmentManager tFragmentManager = getActivity().getSupportFragmentManager();
      //   tFragmentManager.beginTransaction().replace(R.id.container_main, tFragment).addToBackStack(null).commit();
//        tFragmentTransaction.replace(R.id.container_main, tFragment).addToBackStack(null).commit();

    }
    private void init() {
        mActivity = (DashboardActivity) getActivity();
        if (mActivity != null) {
            mActivity.setToolbarTitle("District Manager");
        }
        mFragmentManager = mActivity.getSupportFragmentManager();
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
            CustomLog.e("error",""+e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
