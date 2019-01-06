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
import android.widget.Toast;

import com.abp.driver.ApiClient.ApiClients;
import com.abp.driver.Interface.Api;
import com.abp.driver.R;
import com.abp.driver.activity.DashboardActivity;
import com.abp.driver.adapter.DriverAttDetAdapter;
import com.abp.driver.model.driver.DriverAttendance;
import com.abp.driver.model.driver.DriverAttendanceList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.CustomLog;
import com.abp.driver.utils.SharedPreference;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverAttendanceDetailFragment extends Fragment {

    Unbinder ub_dri_att_det;
    private RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.rv_dri_att_det)
    RecyclerView mRecyclerView;
    private SharedPreference mSharePref;
    private List<DriverAttendanceList> mList;
    private DashboardActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        CustomLog.d("DriverAttDetFragment", "OnCreateCalled");
        View view = inflater.inflate(R.layout.fragment_driver_attendance_detail, container, false );

        ub_dri_att_det = ButterKnife.bind(this, view);
        init();
       return view;
    }
    private void init() {
        mActivity = (DashboardActivity) getActivity();
        if (mActivity != null) {
            mActivity.setToolbarTitle("Driver Attendance");
        }
        mSharePref = new SharedPreference(getContext());
        mList = DriverAttendanceList.listAll(DriverAttendanceList.class);
        if (mList.size() > 0) {
            callRecyclerView();
            if (mActivity.isNetworkAvailable())
            callAttendanceApi();
        } else {
            if (mActivity.isNetworkAvailable())
                callAttendanceApi();
            else
                Toast.makeText(getContext(),"No internet available ",Toast.LENGTH_SHORT).show();
        }
    }
    private void callAttendanceApi() {
        try {
            String api_key = Constant.API_KEY;
            String phone_no = mSharePref.getUserPhoneNo();
            Api api = ApiClients.getApiClients().create(Api.class);
            Call<DriverAttendance> listCall = api.driverAttendance(api_key, phone_no);
            listCall.enqueue(new Callback<DriverAttendance>() {
                @Override
                public void onResponse(Call<DriverAttendance> call, Response<DriverAttendance> response) {
                    DriverAttendance driverAttendance = response.body();
                    if (driverAttendance.getSTATUS().equals(Constant.SUCCESS_CODE)) {
                        DriverAttendanceList.deleteAll(DriverAttendanceList.class);
                        for (DriverAttendanceList driverAttendanceList : driverAttendance.getData()) {
                            driverAttendanceList.save();
                        }
                        callRecyclerView();
                        CustomLog.d("recyclerList", "Responding");
                    } else {

                    }

                }
                @Override
                public void onFailure(Call<DriverAttendance> call, Throwable t) {
                    CustomLog.d("recyclerList", "Not Responding");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callRecyclerView() {
        mList = DriverAttendanceList.listAll(DriverAttendanceList.class);
        if (mList.size() > 0) {
            layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);
            DriverAttDetAdapter driverAttDetAdapter = new DriverAttDetAdapter(getContext(),mList);
            mRecyclerView.setAdapter(driverAttDetAdapter);
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
