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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.model.login.ModelLoginList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.CustomLog;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {

    private DashboardActivity mActivity;
    private FragmentManager mFragmentManager;
    private List<ModelLoginList> loginLists = null;
    @BindView(R.id.tv_driver_ervNumber)
    protected TextView tv_ervNumber;
    @BindView(R.id.tv_profile_name)
   protected TextView tv_name;
    @BindView(R.id.tv_profile_phone)
    protected TextView tv_phone;
    @BindView(R.id.tv_profile_email)
    protected TextView tv_email;
    @BindView(R.id.tv_profile_address)
    protected TextView tv_address;
    @BindView(R.id.tv_profile_adhar_no)
    protected TextView tv_adhar;
    @BindView(R.id.tv_profile_username)
    protected TextView tv_username;
    @BindView(R.id.tv_profile_user_type)
    protected TextView tv_user_type;
    @BindView(R.id.iv_pro_pic)
    protected ImageView iv_image;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }
    private void profileInfo(){
        String ervNumber = loginLists.get(0).getErvNo();
        String name = loginLists.get(0).getName();
        String email = loginLists.get(0).getEmail();
        String phone = loginLists.get(0).getPhoneno();
        String adhar = loginLists.get(0).getAdhaarno();
        String address = loginLists.get(0).getAddress();
        String image = loginLists.get(0).getProfilePic();
        String userName = loginLists.get(0).getUserName();
        String userType = loginLists.get(0).getLogintype();
        if (userType.equals(Constant.LOGIN_TYPE_STATE_MANAGER)) {
            userType = Constant.LOGIN_SPINNER_TYPE_STATE_MANAGER;
        } else if (userType.equals(Constant.LOGIN_TYPE_DISTRICT_MANAGER)) {
            userType = Constant.LOGIN_SPINNER_TYPE_DISTRICT_MANAGER;
        }
        if (userType.equals(Constant.LOGIN_SPINNER_TYPE_STATE_MANAGER) || userType.equals(Constant.LOGIN_SPINNER_TYPE_DISTRICT_MANAGER)){
            tv_ervNumber.setVisibility(View.GONE);
        } else {
            tv_ervNumber.setVisibility(View.VISIBLE);
            tv_ervNumber.setText(ervNumber);
        }
        tv_name.setText(name);
        tv_email.setText(email);
        tv_phone.setText(phone);
        tv_adhar.setText(adhar);
        tv_address.setText(address);
        tv_username.setText(userName);
        tv_user_type.setText(userType);
        Glide.with(this).load(image).into(iv_image);
    }
    private void init() {
        mActivity = (DashboardActivity) getActivity();
        if (mActivity != null) {
            mActivity.setToolbarTitle("Profile");
            mFragmentManager = mActivity.getSupportFragmentManager();
        }
        loginLists = ModelLoginList.listAll(ModelLoginList.class);
        if (loginLists.size() > 0) {
            profileInfo();
        }
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
                            getFragmentManager().popBackStack();
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
