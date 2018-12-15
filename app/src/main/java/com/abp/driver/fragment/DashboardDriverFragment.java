package com.abp.driver.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abp.driver.R;

public class DashboardDriverFragment extends Fragment {

    private  Activity mActivity;
    private  FragmentManager mFragmentManger;
    private  TextView textView;
    private View view;

    public static DashboardDriverFragment newInstance(Activity activity, FragmentManager fragmentManager) {
        Log.d("danny","newInstance called");
        DashboardDriverFragment fragment = new DashboardDriverFragment();
        fragment.mActivity = activity;
        fragment.mFragmentManger = fragmentManager;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("danny","onCreate called");
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_driver_fragment,container,false);

        textView = view.findViewById(R.id.tv_id_drv_frg);
        String user = getActivity().getIntent().getExtras().getString("EMAIL");
        textView.setText("Welcome\n"+user);
        init();
        return view;
    }

    private void init() {

        mActivity.setTitle("Driver");
    }


}
