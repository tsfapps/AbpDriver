package com.abp.driver.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abp.driver.R;
import com.abp.driver.model.evr.ModelEvrList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErvAdapter extends RecyclerView.Adapter<ErvAdapter.EvrViewHolder> {

    private final Context mContext;
    private final FragmentManager mFragmentManger;
    private final List<ModelEvrList> mList;

    public ErvAdapter(Context mContext, FragmentManager mFragmentManager, List<ModelEvrList> mList) {
        this.mContext = mContext;
        this.mFragmentManger = mFragmentManager;
        this.mList = mList;
    }
    @NonNull
    @Override
    public EvrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_erv_final_item, viewGroup, false);



        return new EvrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EvrViewHolder evrViewHolder, int i) {
//        String strStateName = mList.get(i).getStateName();
//        String strDistrictName = mList.get(i).getDistrictName();
//        String strPoliceStationName = mList.get(i).getPolicestationname();
//        String strVehicleNumber = mList.get(i).getVehicleNumber();
//        String driverOne = mList.get(i).getDriver1Name();
//        String driverTwo = mList.get(i).getDriver2Name();
//        String druverThree = (String) mList.get(i).getDriver3Name();
//        evrViewHolder.evrStateName.setText(strStateName);
//        evrViewHolder.evrDistrictName.setText(strDistrictName);
//        evrViewHolder.evrPoliceStationName.setText(strPoliceStationName);
//        evrViewHolder.evrVehicleNumber.setText(strVehicleNumber);
//        evrViewHolder.firstDriver.setText(driverOne);
//        evrViewHolder.secondDriver.setText(driverTwo);
//        evrViewHolder.thirdDriver.setText(druverThree);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class EvrViewHolder extends RecyclerView.ViewHolder{

//        @BindView(R.id.tv_evr_state_name)
//        TextView evrStateName;
//        @BindView(R.id.tv_evr_district_name)
//        TextView evrDistrictName;
//        @BindView(R.id.tv_evr_police_station_name)
//        TextView evrPoliceStationName;
//        @BindView(R.id.tv_evr_vehicle_number)
//        TextView evrVehicleNumber;
//        @BindView(R.id.tv_evr_first_driver)
//        TextView firstDriver;
//        @BindView(R.id.tv_evr_second_driver)
//        TextView secondDriver;
//        @BindView(R.id.tv_evr_third_driver)
//        TextView thirdDriver;
        public EvrViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
