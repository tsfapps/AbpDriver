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
import com.abp.driver.adapter.ErvFinalAdapter;
import com.abp.driver.model.evr.ModelErvFinal;
import com.abp.driver.model.evr.ModelErvFinalList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.CustomLog;
import com.abp.driver.utils.SharedPreference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ErvFinalFragment extends Fragment {

    @BindView(R.id.rv_erv_final)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.tv_no_data_erv_final)
    protected TextView mNoDataText;
    private RecyclerView.LayoutManager mLayoutManager;
    private String mDistrictId;
    private String mDate;
    private DashboardActivity mActivity;
    private SharedPreference mSharePref;
    private Context mContext;
    private String mStateId;
    private List<ModelErvFinalList> mList;
    private FragmentManager mFragmentManager;


    public static ErvFinalFragment newInstance(String districtId, String mDate, String stateId) {
        ErvFinalFragment fragment = new ErvFinalFragment();
        fragment.mDistrictId = districtId;
        fragment.mDate = mDate;
        fragment.mStateId = stateId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_erv_final, container, false);

       ButterKnife.bind(this, view);
       mContext = getContext();
       mFragmentManager = getFragmentManager();
       init();
       return view;
    }

    private void callApi() {
        String apiKey = Constant.API_KEY;
        final String stateId = mStateId;
        String districtId = mDistrictId;
        String dateCreated = mDate;
        Api api = ApiClients.getApiClients().create(Api.class);
        Call<ModelErvFinal> call = api.finalErv(apiKey, stateId, districtId, dateCreated);
        call.enqueue(new Callback<ModelErvFinal>() {
            @Override
            public void onResponse(Call<ModelErvFinal> call, Response<ModelErvFinal> response) {
                ModelErvFinal modelErvFinal = response.body();
                if (modelErvFinal.getSTATUS().equals(Constant.SUCCESS_CODE)){
                    ModelErvFinalList.deleteAll(ModelErvFinalList.class);
                    for (ModelErvFinalList modelErvFinalList : modelErvFinal.getData()){
                        modelErvFinalList.save();
                    }

                    callRecyclerView();
                }else {
                    callRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<ModelErvFinal> call, Throwable t) {
                Toast.makeText(getContext(),"Server error coming !",Toast.LENGTH_SHORT).show();
                callRecyclerView();
            }
        });

    }

    private void init() {
        mActivity = (DashboardActivity) getActivity();
        if (mActivity != null) {
            mActivity.setToolbarTitle("ERV");
        }
        mSharePref = new SharedPreference(getContext());
        mList = ModelErvFinalList.listAll(ModelErvFinalList.class);
        if (mList.size() > 0) {
            callRecyclerView();
            callApi();
        } else {
            mNoDataText.setVisibility(View.VISIBLE);
            if (mActivity.isNetworkAvailable()) {
                callApi();
            } else {
                Toast.makeText(getContext(),"No internet available",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void callRecyclerView(){
        mList = ModelErvFinalList.listAll(ModelErvFinalList.class);
        if (mList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoDataText.setVisibility(View.GONE);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            ErvFinalAdapter ervFinalAdapter = new ErvFinalAdapter(mList);
            mRecyclerView.setAdapter(ervFinalAdapter);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mNoDataText.setVisibility(View.VISIBLE);
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
