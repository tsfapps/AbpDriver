package com.abp.driver.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.abp.driver.adapter.StatusAdapter;
import com.abp.driver.model.police.ModelPolice;
import com.abp.driver.model.police.ModelPoliceList;
import com.abp.driver.model.district.ModelDistrictList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.CustomLog;
import com.abp.driver.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusFragment extends Fragment {

    @BindView(R.id.rv_status)
    RecyclerView mRecyclerView;
    private SharedPreference mSharedPreference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_final, container, false);
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        StatusAdapter statusAdapter = new StatusAdapter();
        mRecyclerView.setAdapter(statusAdapter);
        callApi();
        init();
        return view;
    }
    private void init() {
        DashboardActivity mActivity = (DashboardActivity) getActivity();
        if (mActivity != null) {
            mActivity.setToolbarTitle("Status");
        }
    }

    private void callApi(){
        mSharedPreference = new SharedPreference(getContext());
        String strApi = Constant.API_KEY;
        String strStateId = mSharedPreference.getUserStateId();
        String strDistrictId = mSharedPreference.getUserDistrictId();
        Api api = ApiClients.getApiClients().create(Api.class);
        Call<ModelPolice> call = api.districtDetail(strApi, strStateId, strDistrictId);
        call.enqueue(new Callback<ModelPolice>() {
            @Override
            public void onResponse(Call<ModelPolice> call, Response<ModelPolice> response) {

                ModelPolice modelPolice = response.body();
                ModelDistrictList.deleteAll(ModelDistrictList.class);
                for (ModelPoliceList modelPoliceList : modelPolice.getData()){
                    modelPoliceList.save();
                    CustomLog.d("StatusState", modelPoliceList.getStateName());
                    CustomLog.d("StatusState", "Responding");
                }
            }

            @Override
            public void onFailure(Call<ModelPolice> call, Throwable t) {
                CustomLog.d("StatusState", "NotResponding");

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
