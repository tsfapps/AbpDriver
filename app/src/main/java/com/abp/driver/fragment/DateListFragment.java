package com.abp.driver.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abp.driver.ApiClient.ApiClients;
import com.abp.driver.Interface.Api;
import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.activity.LoginActivity;
import com.abp.driver.adapter.ErvDateAdapter;
import com.abp.driver.model.date.ModelDate;
import com.abp.driver.model.date.ModelDateList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.SharedPreference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateListFragment extends Fragment {

    private Context mContext;
    private FragmentManager mFragmentManager;
    @BindView(R.id.rv_evr_date)
    protected RecyclerView rv_date;
    @BindView(R.id.tv_no_erv_date)
    protected TextView mTvNoDate;
    @BindView(R.id.pbDate)
    protected ProgressBar pbDate;

    private List<ModelDateList> modelDateLists;
    private String mDistrictId = null;
    private String mCheck;
    private DashboardActivity mActivity;

    public static DateListFragment newInstance(String districtId, String mCheck) {
        DateListFragment fragment = new DateListFragment();
        fragment.mDistrictId = districtId;
        fragment.mCheck = mCheck;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date, container, false);
        ButterKnife.bind(this, view);
        mFragmentManager = getFragmentManager();
        init();
        return view;
    }
    private void startHandler() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pbDate.setVisibility(View.GONE);
            }
        }, 1000);
    }

    private void callRecyclerView() {
        List<ModelDateList> mList = ModelDateList.listAll(ModelDateList.class);
        if (mList.size() > 0) {
            rv_date.setVisibility(View.VISIBLE);
            mTvNoDate.setVisibility(View.GONE);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
            rv_date.setLayoutManager(layoutManager);
            ErvDateAdapter ervDateAdapter = new ErvDateAdapter(mList, mContext, mFragmentManager, mCheck);
            rv_date.setAdapter(ervDateAdapter);
            ervDateAdapter.notifyDataSetChanged();
        } else {
            rv_date.setVisibility(View.GONE);
            mTvNoDate.setVisibility(View.VISIBLE);
        }
    }

    private void apiCall() {
        try {
            mActivity.uiThreadHandler.sendEmptyMessage(Constant.SHOW_PROGRESS_DIALOG);
            SharedPreference mSharedPreference = new SharedPreference(mContext);
            String strApiKey = Constant.API_KEY;
            String strStateId = mSharedPreference.getUserStateId();
            Api api = ApiClients.getApiClients().create(Api.class);
            Log.d("danny","sid:"+strStateId+" dId:"+mDistrictId);
            Call<ModelDate> call = api.dateList(strApiKey, strStateId, mDistrictId);
            call.enqueue(new Callback<ModelDate>() {
                @Override
                public void onResponse(Call<ModelDate> call, Response<ModelDate> response) {
                    ModelDate modelDate = response.body();
                    assert modelDate != null;
                   // if (modelDate.getSTATUS() != null && modelDate.getSTATUS().equals(Constant.SUCCESS_CODE)){
                        ModelDateList.deleteAll(ModelDateList.class);
                        for (ModelDateList modelDateList : modelDate.getData()){
                            modelDateList.save();
                        }
                        callRecyclerView();
                   /* }else {
                        mTvNoDate.setVisibility(View.VISIBLE);
                        rv_date.setVisibility(View.GONE);
                    }*/
                    mActivity.uiThreadHandler.sendMessageDelayed(mActivity.uiThreadHandler.obtainMessage(Constant.HIDE_PROGRESS_DIALOG),Constant.HIDE_PROGRESS_DIALOG_DELAY);
                }

                @Override
                public void onFailure(Call<ModelDate> call, Throwable t) {
                    mActivity.uiThreadHandler.sendMessageDelayed(mActivity.uiThreadHandler.obtainMessage(Constant.HIDE_PROGRESS_DIALOG),Constant.HIDE_PROGRESS_DIALOG_DELAY);
                    mTvNoDate.setVisibility(View.VISIBLE);
                    rv_date.setVisibility(View.GONE);
                    Toast.makeText(mContext,"server error occur !",Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {
        //startHandler();
        mActivity = (DashboardActivity)getActivity();
        if (mActivity !=null){
            mActivity.setToolbarTitle("Date List");
        }
        if (mActivity.isNetworkAvailable()) {
            apiCall();
        } else {
            Toast.makeText(getContext(),"No Internet available",Toast.LENGTH_SHORT).show();
            mTvNoDate.setVisibility(View.VISIBLE);
            rv_date.setVisibility(View.GONE);
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
