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
import android.widget.TextView;

import com.abp.driver.ApiClient.ApiClients;
import com.abp.driver.Interface.Api;
import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.adapter.StatusDistrictAdapter;
import com.abp.driver.model.district.ModelDistrict;
import com.abp.driver.model.district.ModelDistrictList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.CustomLog;
import com.abp.driver.utils.SharedPreference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusDistrictListFragment extends Fragment{

    @BindView(R.id.rv_status_district)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_no_data)
    protected TextView mNoDataText;
    private StatusDistrictAdapter districtAdapter;

    private SharedPreference mSharedPreference;
    private FragmentManager mFragmentManager;
    private Context mContext;
    private LinearLayoutManager mLayoutManager;
    private List<ModelDistrictList> mList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_district, container, false);
        ButterKnife.bind(this, view);
        mContext = getContext();
        mFragmentManager = getFragmentManager();
        init();
        return view;
    }
    private void init(){
        DashboardActivity mActivity = (DashboardActivity)getActivity();
        if (mActivity!=null){
            mActivity.setToolbarTitle("District List");
        }
        mList = ModelDistrictList.listAll(ModelDistrictList.class);
        if (mList.size() >0) {
            callRecyclerView();
            callApi();
        } else {
            callApi();
            mNoDataText.setVisibility(View.VISIBLE);
        }

    }

    private void callRecyclerView() {
        mList = ModelDistrictList.listAll(ModelDistrictList.class);
        if (mList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoDataText.setVisibility(View.GONE);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            districtAdapter = new StatusDistrictAdapter(mContext, mFragmentManager, mList);
            mRecyclerView.setAdapter(districtAdapter);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mNoDataText.setVisibility(View.VISIBLE);
        }
    }

    private void callApi(){
        mSharedPreference = new SharedPreference(getContext());
        String strApi = Constant.API_KEY;
        String strId =  mSharedPreference.getUserStateId();
        Api api = ApiClients.getApiClients().create(Api.class);
        Call<ModelDistrict> call = api.districtList(strApi, strId);
        call.enqueue(new Callback<ModelDistrict>() {
            @Override
            public void onResponse(Call<ModelDistrict> call, Response<ModelDistrict> response) {
                ModelDistrict modelDistrict = response.body();
                if (modelDistrict.getSTATUS().equals(Constant.SUCCESS_CODE)) {
                    ModelDistrictList.deleteAll(ModelDistrictList.class);
                    for (ModelDistrictList modelDistrictList : modelDistrict.getData()) {
                        modelDistrictList.save();
                    }
                    callRecyclerView();
                } else {
                    callRecyclerView();
                }

            }

            @Override
            public void onFailure(Call<ModelDistrict> call, Throwable t) {
               CustomLog.d("districtList", "onFailure");
               callRecyclerView();
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
