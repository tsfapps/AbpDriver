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
import com.abp.driver.adapter.EarAdapter;
import com.abp.driver.model.evr.ModelEvr;
import com.abp.driver.model.evr.ModelEvrList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.SharedPreference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EvrFragment extends Fragment {

    @BindView(R.id.rv_evr)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.tv_no_data)
    protected TextView mNoDataText;
    private RecyclerView.LayoutManager mLayoutManager;
    private String mDistrictId;
    private String dateCreated;
    private DashboardActivity mActivity;
    private SharedPreference mSharePref;
    private Context mContext;
    private String mStateId;
    private List<ModelEvrList> mList;
    private FragmentManager mFragmentManager;


    public static EvrFragment newInstance(String districtId, String dateCreated, String stateId) {
        EvrFragment fragment = new EvrFragment();
        fragment.mDistrictId = districtId;
        fragment.dateCreated = dateCreated;
        fragment.mStateId = stateId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evr, container, false);

       ButterKnife.bind(this, view);
       mContext = getContext();
       mFragmentManager = getFragmentManager();
        init();
        return view;
    }

    private void callApi() {
        String strApiKey = Constant.API_KEY;
        String strStateId = mStateId;
        String strDistrictId = mDistrictId;
        String strDateCreated = dateCreated;
        Api api = ApiClients.getApiClients().create(Api.class);
        Call<ModelEvr> call = api.ervDetail(strApiKey, strStateId, strDistrictId, strDateCreated);
        call.enqueue(new Callback<ModelEvr>() {
            @Override
            public void onResponse(Call<ModelEvr> call, Response<ModelEvr> response) {
                ModelEvr modelEvr = response.body();
                if (modelEvr.getSTATUS().equals(Constant.SUCCESS_CODE)) {
                    ModelEvrList.deleteAll(ModelEvrList.class);
                    for (ModelEvrList modelEvrList : modelEvr.getData()) {
                        modelEvrList.save();
                    }
                    callRecyclerView();
                } else {
                    callRecyclerView();
                }
            }
            @Override
            public void onFailure(Call<ModelEvr> call, Throwable t) {
                callRecyclerView();
                Toast.makeText(getContext(),"Server error coming !",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        mActivity = (DashboardActivity) getActivity();
        if (mActivity != null) {
            mActivity.setToolbarTitle("ERV");
        }
        mSharePref = new SharedPreference(getContext());
        mList = ModelEvrList.listAll(ModelEvrList.class);
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
        mList = ModelEvrList.listAll(ModelEvrList.class);
        if (mList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoDataText.setVisibility(View.GONE);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            EarAdapter evrAapter = new EarAdapter(mContext, mFragmentManager, mList);
            mRecyclerView.setAdapter(evrAapter);
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
