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
import android.widget.TextView;

import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.utils.CustomLog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DriverFragment extends Fragment {

    private static final String TAG = "DriverFragment";
    private  DashboardActivity mActivity;
    private  FragmentManager mFragmentManger;
    private View view;
    @BindView(R.id.tv_user_name)
    TextView mUserName;

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


}
