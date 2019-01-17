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
import com.abp.driver.adapter.EvrDateAdapter;
import com.abp.driver.model.date.ModelDate;
import com.abp.driver.model.date.ModelDateList;
import com.abp.driver.model.police.ModelPoliceList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.SharedPreference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvrDateListFragment extends Fragment {

    private Context mContext;
    private FragmentManager mFragmentManager;
    @BindView(R.id.rv_evr_date)
    protected RecyclerView rv_date;
    @BindView(R.id.tv_no_evr_date)
    protected TextView mTvNoDate;

    private SharedPreference mSharedPreference;
    private List<ModelDateList> modelDateLists;
    private DashboardActivity mActivity;
    private String mDistrictId = null;

    public static EvrDateListFragment newInstance(String districtId) {
        EvrDateListFragment fragment = new EvrDateListFragment();
        fragment.mDistrictId = districtId;
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evr_date, container, false);
        ButterKnife.bind(this, view);
        mContext = getContext();
        mFragmentManager = getFragmentManager();
        mActivity = (DashboardActivity)getActivity();
        modelDateLists = ModelDateList.listAll(ModelDateList.class);
        if (modelDateLists.size() > 0) {
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

//apiCall();
        init();
        return view;
    }

    private void callRecyclerView() {
        List<ModelDateList> mList = ModelDateList.listAll(ModelDateList.class);
        if (mList.size() > 0) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
            rv_date.setLayoutManager(layoutManager);
            EvrDateAdapter evrDateAdapter = new EvrDateAdapter(mList, mContext, mFragmentManager);
            rv_date.setAdapter(evrDateAdapter);
            evrDateAdapter.notifyDataSetChanged();
        } else {
            rv_date.setVisibility(View.GONE);
            mTvNoDate.setVisibility(View.VISIBLE);
        }
    }

    private void apiCall() {
        try {
            mSharedPreference = new SharedPreference(mContext);
            String strApiKey = Constant.API_KEY;
            String strStateId = mSharedPreference.getUserStateId();
            Api api = ApiClients.getApiClients().create(Api.class);
            Log.d("danny","sid:"+strStateId+" dId:"+mDistrictId);
            Call<ModelDate> call = api.dateList(strApiKey, strStateId, mDistrictId);
            call.enqueue(new Callback<ModelDate>() {
                @Override
                public void onResponse(Call<ModelDate> call, Response<ModelDate> response) {
                    ModelDate modelDate = response.body();
                    if (modelDate.getSTATUS().equals(Constant.SUCCESS_CODE)){
                        mTvNoDate.setVisibility(View.GONE);
                        rv_date.setVisibility(View.VISIBLE);
                        ModelDateList.deleteAll(ModelDateList.class);
                        for (ModelDateList modelDateList : modelDate.getData()){
                            modelDateList.save();
                        }
                        callRecyclerView();
                    }else {
                        mTvNoDate.setVisibility(View.VISIBLE);
                        rv_date.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ModelDate> call, Throwable t) {
                    mTvNoDate.setVisibility(View.VISIBLE);
                    rv_date.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {
        DashboardActivity mActivity = (DashboardActivity)getActivity();
        if (mActivity !=null){
            mActivity.setToolbarTitle("Police Station");
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
