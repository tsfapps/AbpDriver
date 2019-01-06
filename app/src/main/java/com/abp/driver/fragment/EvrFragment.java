package com.abp.driver.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.adapter.EvrAapter;
import com.abp.driver.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EvrFragment extends Fragment {

    @BindView(R.id.rv_evr)
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private String mDistrictId;
    private String mPoliceId;
    private DashboardActivity mActivity;
    private SharedPreference mSharePref;

    public static EvrFragment newInstance(String districtId, String policeId) {
        EvrFragment fragment = new EvrFragment();
        fragment.mDistrictId = districtId;
        fragment.mPoliceId = policeId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evr, container, false);

       ButterKnife.bind(this, view);
        init();
        return view;
    }
    private void init() {
        mActivity = (DashboardActivity) getActivity();
        if (mActivity != null) {
            mActivity.setToolbarTitle("EVR");
        }
        mSharePref = new SharedPreference(getContext());

    }

    private void callRecyclerView(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        EvrAapter evrAapter = new EvrAapter();
        mRecyclerView.setAdapter(evrAapter);
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
