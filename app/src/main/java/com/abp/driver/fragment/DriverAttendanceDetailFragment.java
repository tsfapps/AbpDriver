package com.abp.driver.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abp.driver.ApiClient.ApiClients;
import com.abp.driver.Interface.Api;
import com.abp.driver.R;
import com.abp.driver.adapter.DriverAttDetAdapter;
import com.abp.driver.model.driver.DriverAttendance;
import com.abp.driver.model.driver.DriverAttendanceList;
import com.abp.driver.utils.CustomLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverAttendanceDetailFragment extends Fragment {

    Unbinder ub_dri_att_det;

    private List<DriverAttendance> driverAttendances;
    private RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.rv_dri_att_det)
    RecyclerView mRecyclerView;

   // private DriverAttendanceList driverAttendanceLists;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        CustomLog.d("DriverAttDetFragment", "OnCreateCalled");
        View view = inflater.inflate(R.layout.fragment_driver_attendance_detail, container, false );

        ub_dri_att_det = ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        DriverAttDetAdapter driverAttDetAdapter = new DriverAttDetAdapter();
        mRecyclerView.setAdapter(driverAttDetAdapter);

        callAttendanceApi();

       return view;
    }


    private void callAttendanceApi() {
       // final  Context ctx;
        String api_key = "abpn";
        String phone_no = "9005103632";
        Api api = ApiClients.getApiClients().create(Api.class);
        Call<DriverAttendance> listCall = api.driverAttendance(api_key, phone_no);
        final Context finalCtx;
        listCall.enqueue(new Callback<DriverAttendance>() {
            @Override
            public void onResponse(Call<DriverAttendance> call, Response<DriverAttendance> response) {

                DriverAttendance driverAttendance = response.body();
                DriverAttendanceList.deleteAll(DriverAttendanceList.class);
                for (DriverAttendanceList driverAttendanceList : driverAttendance.getData()){
                    driverAttendanceList.save();
                }


            }

            @Override
            public void onFailure(Call<DriverAttendance> call, Throwable t) {
                CustomLog.d("recyclerList", "Not Responding");

            }
        });
    }
}
