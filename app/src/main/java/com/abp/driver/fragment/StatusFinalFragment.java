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
import android.widget.TextView;
import android.widget.Toast;

import com.abp.driver.ApiClient.ApiClients;
import com.abp.driver.Interface.Api;
import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.adapter.StatusFinalAdapter;
import com.abp.driver.model.status.ModelStatus;
import com.abp.driver.model.status.ModelStatusList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.CustomLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusFinalFragment extends Fragment {

    @BindView(R.id.rv_status_final)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_no_data_status)
    protected TextView mNoDataStatus;
    private String mStateId;
    private String mDistrictId;
    private String mDate;
    List<ModelStatusList> mList;

    public static StatusFinalFragment newInstance( String mDistrictId, String mDate, String mStateId) {
        StatusFinalFragment fragment = new StatusFinalFragment();
        fragment.mStateId = mStateId;
        fragment.mDistrictId = mDistrictId;
        fragment.mDate = mDate;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_final, container, false);
        ButterKnife.bind(this, view);
        DashboardActivity mActivity = (DashboardActivity)getActivity();
        //callRecyclerView();
        mList =  ModelStatusList.listAll(ModelStatusList.class);
        if (mList.size() > 0) {
           // callRecyclerView();
            if (mActivity.isNetworkAvailable()) {
                callApi();
            }
        } else {
            if (mActivity.isNetworkAvailable()) {
                callApi();
            } else {
                Toast.makeText(getContext(),"No Internet available",Toast.LENGTH_SHORT).show();
            }
        }
        init();
        return view;
    }
   public void callRecyclerView(){
        mList = ModelStatusList.listAll(ModelStatusList.class);
        CustomLog.d("tsfapps", String.valueOf(mList.size()));
       if (mList.size()>0) {
           mRecyclerView.setVisibility(View.VISIBLE);
           mNoDataStatus.setVisibility(View.GONE);
           RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
           mRecyclerView.setLayoutManager(mLayoutManager);
           StatusFinalAdapter statusFinalAdapter = new StatusFinalAdapter(mList);
           mRecyclerView.setAdapter(statusFinalAdapter);
       }
       else {
           mRecyclerView.setVisibility(View.GONE);
           mNoDataStatus.setVisibility(View.VISIBLE);
       }
    }
    private void init() {
        DashboardActivity mActivity = (DashboardActivity) getActivity();
        if (mActivity != null) {
            mActivity.setToolbarTitle("Status");
        }
    }

    private void callApi(){
        String strApi = Constant.API_KEY;
        String stateId = mStateId;
        String districtId = mDistrictId;
        String strDate = mDate;
        Api api = ApiClients.getApiClients().create(Api.class);
        CustomLog.d("tsfapps", "stId : "+mStateId+" disId : "+mDistrictId+" date : "+mDate);
        Call<ModelStatus> call = api.finalStatus(strApi, stateId, districtId, strDate);
        call.enqueue(new Callback<ModelStatus>() {
            @Override
            public void onResponse(Call<ModelStatus> call, Response<ModelStatus> response) {
                ModelStatus modelStatus = response.body();
                CustomLog.d("tsfapps", "Responding");
                if (modelStatus.getSTATUS().equals(Constant.SUCCESS_CODE)){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mNoDataStatus.setVisibility(View.GONE);
                    ModelStatusList.deleteAll(ModelStatusList.class);
                    for (ModelStatusList modelStatusList : modelStatus.getData()){
                        modelStatusList.save();
                    }
                    callRecyclerView();
                }
               else {
                    mRecyclerView.setVisibility(View.GONE);
                    mNoDataStatus.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ModelStatus> call, Throwable t) {

                CustomLog.d("tsfapps", "Not Responding");
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
