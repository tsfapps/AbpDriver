package com.abp.driver.fragment;

import android.content.Context;
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
import com.abp.driver.adapter.EvrPoliceAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EvrPoliceListFragment extends Fragment {

    private Context mContext;
    private FragmentManager mFragmentManager;
    @BindView(R.id.rv_evr_police)
    RecyclerView rv_police;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_evr_police, container, false);
        ButterKnife.bind(this, view);
        mContext = getContext();
        mFragmentManager = getFragmentManager();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        rv_police.setLayoutManager(layoutManager);
        EvrPoliceAdapter evrPoliceAdapter = new EvrPoliceAdapter(mContext, mFragmentManager);
        rv_police.setAdapter(evrPoliceAdapter);
        apiCall();
        init();
        return view;
    }

    private void apiCall() {

    }

    private void init() {
        DashboardActivity mActivity = (DashboardActivity)getActivity();
        if (mActivity !=null){
            mActivity.setToolbarTitle("Evr Police List");
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
