package com.abp.driver.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.adapter.EvrAapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EvrFragment extends Fragment {

    private Unbinder mUnbinder;
    @BindView(R.id.rv_evr)
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evr, container, false);

      mUnbinder = ButterKnife.bind(this, view);
       mLayoutManager = new LinearLayoutManager(getActivity());
       mRecyclerView.setLayoutManager(mLayoutManager);
        EvrAapter evrAapter = new EvrAapter();
        mRecyclerView.setAdapter(evrAapter);


        init();
        return view;
    }
    private void init() {
        DashboardActivity mActivity = (DashboardActivity) getActivity();
        if (mActivity != null) {
            mActivity.setToolbarTitle("EVR");
        }
    }

}
