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

import com.abp.driver.ApiClient.ApiClients;
import com.abp.driver.Interface.Api;
import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.adapter.StatusDistrictAdapter;
import com.abp.driver.model.status.District;
import com.abp.driver.model.status.DistrictList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusDistrictListFragment extends Fragment{

    @BindView(R.id.rv_status_district)
    RecyclerView mRecyclerView;
    private StatusDistrictAdapter districtAdapter;

    private SharedPreference mSharedPreference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_district, container, false);
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        Context mContext = getContext();
        FragmentManager mFragmentManager = getFragmentManager();
        mRecyclerView.setLayoutManager(mLayoutManager);
        districtAdapter = new StatusDistrictAdapter(mContext, mFragmentManager);
        mRecyclerView.setAdapter(districtAdapter);
        callApi();
        init();
        return view;
    }
private void init(){
    DashboardActivity mActivity = (DashboardActivity)getActivity();
    if (mActivity!=null){
        mActivity.setToolbarTitle("District List");
    }
}
        private void callApi(){
        mSharedPreference = new SharedPreference(getContext());
        String strApi = Constant.API_KEY;
        String strId =  mSharedPreference.getUserStateId();
        Api api = ApiClients.getApiClients().create(Api.class);
        Call<District> call = api.districtList(strApi, strId);
        call.enqueue(new Callback<District>() {
            @Override
            public void onResponse(Call<District> call, Response<District> response) {
                District district = response.body();
                DistrictList.deleteAll(DistrictList.class);
                for (DistrictList districtList : district.getData()){
                    districtList.save();
                }

            }

            @Override
            public void onFailure(Call<District> call, Throwable t) {
//                CustomLog.d("districtList", "NotResponding");
            }
        });
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
