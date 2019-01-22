package com.abp.driver.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abp.driver.R;
import com.abp.driver.model.evr.ModelErvFinalList;
import com.abp.driver.utils.CustomLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErvFinalAdapter extends RecyclerView.Adapter<ErvFinalAdapter.ErvFinalHolder>{

    private List<ModelErvFinalList> modelErvFinalLists;

    public ErvFinalAdapter(List<ModelErvFinalList> modelErvFinalLists) {
        this.modelErvFinalLists = modelErvFinalLists;
    }

    @NonNull
    @Override
    public ErvFinalHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_erv_final_item, viewGroup, false);
        CustomLog.d("tsfapps", String.valueOf(modelErvFinalLists.size()));
        return new ErvFinalHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ErvFinalHolder ervFinalHolder, int i) {
        String firstDriverStatus = modelErvFinalLists.get(i).getDriverstatus1();
        String secDriverStatus = modelErvFinalLists.get(i).getDriverstatus2();
        String thirdDriverStatus = modelErvFinalLists.get(i).getDriverstatus3();
        String vehicleNumber = modelErvFinalLists.get(i).getRegistrationNo();
        ervFinalHolder.tvVehicleNumber.setText(vehicleNumber);
        int no = i+1;
        ervFinalHolder.mTvSno.setText(""+no);
        if (firstDriverStatus.equals("delay")){
            ervFinalHolder.llFirstDriver.setBackgroundColor(Color.parseColor("#fa0202"));
        }else if (firstDriverStatus.equals("normal")){
            ervFinalHolder.llFirstDriver.setBackgroundColor(Color.parseColor("#059603"));
        } else if (firstDriverStatus.equals("overtime")) {
            ervFinalHolder.llFirstDriver.setBackgroundColor(Color.parseColor("#fca103"));
        } else {
            ervFinalHolder.llFirstDriver.setBackgroundColor(Color.parseColor("#03c2df"));
        }
        if (secDriverStatus.equals("delay")){
            ervFinalHolder.llSecDriver.setBackgroundColor(Color.parseColor("#fa0202"));
        }else if (secDriverStatus.equals("normal")){
            ervFinalHolder.llSecDriver.setBackgroundColor(Color.parseColor("#059603"));
        }else if (secDriverStatus.equals("overtime")) {
            ervFinalHolder.llSecDriver.setBackgroundColor(Color.parseColor("#fca103"));
        } else {
            ervFinalHolder.llSecDriver.setBackgroundColor(Color.parseColor("#03c2df"));
        }

        if (thirdDriverStatus.equals("delay")){
            ervFinalHolder.llThirdDriver.setBackgroundColor(Color.parseColor("#fa0202"));
        }else if (thirdDriverStatus.equals("normal")){
            ervFinalHolder.llThirdDriver.setBackgroundColor(Color.parseColor("#059603"));
        }else if (thirdDriverStatus.equals("overtime")) {
            ervFinalHolder.llThirdDriver.setBackgroundColor(Color.parseColor("#fca103"));
        } else {
            ervFinalHolder.llThirdDriver.setBackgroundColor(Color.parseColor("#000000"));
        }

    }

    @Override
    public int getItemCount() {
        return modelErvFinalLists.size();
    }

    public class ErvFinalHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_erv_vehicle_number)
        protected TextView tvVehicleNumber;

        @BindView(R.id.tv_erv_M_driver_status)
        protected TextView llFirstDriver;
        @BindView(R.id.tv_erv_D_driver_status)
        protected TextView llSecDriver;
        @BindView(R.id.tv_erv_N_driver_status)
        protected TextView llThirdDriver;
        @BindView(R.id.tv_srl_no_evr)
        public TextView mTvSno;

        public ErvFinalHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
