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
import android.widget.Toast;

import com.abp.driver.ApiClient.ApiClients;
import com.abp.driver.Interface.Api;
import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.adapter.EvrPoliceAdapter;
import com.abp.driver.model.police.ModelPolice;
import com.abp.driver.model.police.ModelPoliceList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.CustomLog;
import com.abp.driver.utils.SharedPreference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvrPoliceListFragment extends Fragment {

    private Context mContext;
    private FragmentManager mFragmentManager;
    @BindView(R.id.rv_evr_police)
    protected RecyclerView rv_police;
    @BindView(R.id.tv_no_data)
    protected TextView mTvNoData;

    private SharedPreference mSharedPreference;
    private List<ModelPoliceList> mPolicList;
    private DashboardActivity mActivity;
    private String mDistrictId = null;

    public static EvrPoliceListFragment newInstance(String districtId) {
        EvrPoliceListFragment fragment = new EvrPoliceListFragment();
        fragment.mDistrictId = districtId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_evr_police, container, false);
        ButterKnife.bind(this, view);
        mContext = getContext();
        mFragmentManager = getFragmentManager();
        mActivity = (DashboardActivity)getActivity();

        mPolicList = ModelPoliceList.listAll(ModelPoliceList.class);
        if (mPolicList.size() > 0) {
            callRecyclerView();
            if (mActivity.isNetworkAvailable()) {
                apiCall();
            }
        } else {
            if (mActivity.isNetworkAvailable()) {
                apiCall();
            } else {
                Toast.makeText(getContext(),"No Internet available",Toast.LENGTH_SHORT).show();
            }
        }
        init();
        return view;
    }

    private void callRecyclerView() {
        List<ModelPoliceList> mList = ModelPoliceList.listAll(ModelPoliceList.class);
        if (mList.size() > 0) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
            rv_police.setLayoutManager(layoutManager);
            EvrPoliceAdapter evrPoliceAdapter = new EvrPoliceAdapter(mContext, mFragmentManager, mList);
            rv_police.setAdapter(evrPoliceAdapter);
            evrPoliceAdapter.notifyDataSetChanged();
        } else {
            rv_police.setVisibility(View.GONE);
            mTvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void apiCall() {
        try {
            mSharedPreference = new SharedPreference(mContext);
            String strApiKey = Constant.API_KEY;
            String strStateId = mSharedPreference.getUserStateId();
            Api api = ApiClients.getApiClients().create(Api.class);
            Log.d("danny","sid:"+strStateId+" dId:"+mDistrictId);
            Call<ModelPolice> call = api.districtDetail(strApiKey, strStateId, mDistrictId);
            call.enqueue(new Callback<ModelPolice>() {
                @Override
                public void onResponse(Call<ModelPolice> call, Response<ModelPolice> response) {
                    ModelPolice modelPolice = response.body();
                    if (modelPolice.getSTATUS().equals(Constant.SUCCESS_CODE)) {
                        mTvNoData.setVisibility(View.GONE);
                        rv_police.setVisibility(View.VISIBLE);
                        ModelPoliceList.deleteAll(ModelPoliceList.class);
                        for (ModelPoliceList modelPoliceList : modelPolice.getData()) {
                            modelPoliceList.save();
                        }
                        callRecyclerView();
                    } else {
                        rv_police.setVisibility(View.GONE);
                        mTvNoData.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ModelPolice> call, Throwable t) {
                    rv_police.setVisibility(View.GONE);
                    mTvNoData.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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
