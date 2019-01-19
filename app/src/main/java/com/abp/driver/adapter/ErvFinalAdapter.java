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
        String firstDriver = modelErvFinalLists.get(i).getDriver1();
        String firstDriverStatus = modelErvFinalLists.get(i).getDriverstatus1();
        String secDriver = modelErvFinalLists.get(i).getDriver2();
        String secDriverStatus = modelErvFinalLists.get(i).getDriverstatus2();
        String thirdDriver = modelErvFinalLists.get(i).getDriver3();
        String thirdDriverStatus = modelErvFinalLists.get(i).getDriverstatus3();
        String vehicleNumber = modelErvFinalLists.get(i).getRegistrationNo();

        ervFinalHolder.tvFirstDriver.setText(firstDriver);
        ervFinalHolder.tvFirstDriverStatus.setText(firstDriverStatus);
        ervFinalHolder.tvSecDriver.setText(secDriver);
        ervFinalHolder.tvSecDriverStatus.setText(secDriverStatus);
        ervFinalHolder.tvThirdDriver.setText(thirdDriver);
        ervFinalHolder.tvThirdDriverStatus.setText(thirdDriverStatus);
        ervFinalHolder.tvVehicleNumber.setText(vehicleNumber);

        if (firstDriverStatus.equals("delay")){
            ervFinalHolder.llFirstDriver.setBackgroundColor(Color.parseColor("#fa0202"));
        }else if (firstDriverStatus.equals("normal")){
            ervFinalHolder.llFirstDriver.setBackgroundColor(Color.parseColor("#059603"));
        }else ervFinalHolder.llFirstDriver.setBackgroundColor(Color.parseColor("#fca103"));
 if (secDriverStatus.equals("delay")){
            ervFinalHolder.llSecDriver.setBackgroundColor(Color.parseColor("#fa0202"));
        }else if (secDriverStatus.equals("normal")){
            ervFinalHolder.llSecDriver.setBackgroundColor(Color.parseColor("#059603"));
        }else ervFinalHolder.llSecDriver.setBackgroundColor(Color.parseColor("#fca103"));
 if (thirdDriverStatus.equals("delay")){
            ervFinalHolder.llThirdDriver.setBackgroundColor(Color.parseColor("#fa0202"));
        }else if (thirdDriverStatus.equals("normal")){
            ervFinalHolder.llThirdDriver.setBackgroundColor(Color.parseColor("#059603"));
        }else ervFinalHolder.llThirdDriver.setBackgroundColor(Color.parseColor("#fca103"));


    }

    @Override
    public int getItemCount() {
        return modelErvFinalLists.size();
    }

    public class ErvFinalHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_erv_first_driver)
        protected TextView tvFirstDriver;

        @BindView(R.id.tv_erv_first_driver_status)
        protected TextView tvFirstDriverStatus;

        @BindView(R.id.tv_erv_second_driver)
        protected TextView tvSecDriver;

        @BindView(R.id.tv_erv_second_driver_status)
        protected TextView tvSecDriverStatus;

        @BindView(R.id.tv_erv_third_driver)
        protected TextView tvThirdDriver;

        @BindView(R.id.tv_erv_third_driver_status)
        protected TextView tvThirdDriverStatus;

        @BindView(R.id.tv_erv_vehicle_number)
        protected TextView tvVehicleNumber;

        @BindView(R.id.ll_erv_first_driver_status)
        protected LinearLayout llFirstDriver;
        @BindView(R.id.ll_erv_second_driver_status)
        protected LinearLayout llSecDriver;
        @BindView(R.id.ll_erv_third_driver_status)
        protected LinearLayout llThirdDriver;

        public ErvFinalHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
