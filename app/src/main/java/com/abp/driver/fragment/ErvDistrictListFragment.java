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
import com.abp.driver.adapter.EvrDistrictAdapter;
import com.abp.driver.model.date.ModelDateList;
import com.abp.driver.model.district.ModelDistrict;
import com.abp.driver.model.district.ModelDistrictList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.SharedPreference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ErvDistrictListFragment extends Fragment {

    @BindView(R.id.rv_evr_district)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.tv_no_data)
    protected TextView mNoDataText;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private SharedPreference mSharedPreference;
    private List<ModelDistrictList> modelDistrictLists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evr_district, container, false);
        ButterKnife.bind(this, view);
        mContext = getContext();
        mFragmentManager = getFragmentManager();
        DashboardActivity mActivity = (DashboardActivity)getActivity();
        modelDistrictLists = ModelDistrictList.listAll(ModelDistrictList.class);

        if (modelDistrictLists.size() > 0) {
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

    private void apiCall() {
        try {
            mSharedPreference = new SharedPreference(mContext);
            String strApiKey = Constant.API_KEY;
            String strStateId = mSharedPreference.getUserStateId();
            Api api = ApiClients.getApiClients().create(Api.class);
            Call<ModelDistrict> call = api.districtList(strApiKey, strStateId);
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
                    Toast.makeText(getContext(),"Server error coming !",Toast.LENGTH_SHORT).show();
                    callRecyclerView();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callRecyclerView() {
      List<ModelDistrictList>  mList = ModelDistrictList.listAll(ModelDistrictList.class);
        if (mList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoDataText.setVisibility(View.GONE);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
            mRecyclerView.setLayoutManager(layoutManager);
            EvrDistrictAdapter evrDistrictAdapter = new EvrDistrictAdapter(mContext, mFragmentManager,mList);
            mRecyclerView.setAdapter(evrDistrictAdapter);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mNoDataText.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        DashboardActivity mActivity = (DashboardActivity) getActivity();
        if (mActivity !=null){
            mActivity.setToolbarTitle("District List");
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