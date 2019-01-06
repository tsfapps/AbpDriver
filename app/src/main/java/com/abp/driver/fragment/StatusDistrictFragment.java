package com.abp.driver.fragment;

import android.content.Context;
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
import android.widget.Toast;

import com.abp.driver.ApiClient.ApiClients;
import com.abp.driver.Interface.Api;
import com.abp.driver.Interface.RecyclerClickListner;
import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.adapter.StatusDistrictAdapter;
import com.abp.driver.model.status.StatusDistrict;
import com.abp.driver.model.status.StatusDistrictList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusDistrictFragment extends Fragment{

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
        Call<StatusDistrict> call = api.districtList(strApi, strId);
        call.enqueue(new Callback<StatusDistrict>() {
            @Override
            public void onResponse(Call<StatusDistrict> call, Response<StatusDistrict> response) {
                StatusDistrict statusDistrict = response.body();
                StatusDistrictList.deleteAll(StatusDistrictList.class);
                for (StatusDistrictList statusDistrictList : statusDistrict.getData()){
                    statusDistrictList.save();
                }

            }

            @Override
            public void onFailure(Call<StatusDistrict> call, Throwable t) {
//                CustomLog.d("districtList", "NotResponding");
            }
        });
    }


}
