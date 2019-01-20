package com.abp.driver.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abp.driver.R;
import com.abp.driver.model.district.ModelDistrictList;
import com.abp.driver.model.status.ModelStatusList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusFinalAdapter extends RecyclerView.Adapter<StatusFinalAdapter.StatusViewHolder>{
    private List<ModelStatusList> modelStatusLists;

    public StatusFinalAdapter(List<ModelStatusList> modelStatusLists) {
        this.modelStatusLists = modelStatusLists;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_status_final_item, viewGroup, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {

        String strShifting = modelStatusLists.get(i).getShiftingTime();
        String strStatus = modelStatusLists.get(i).getStatusOnOff();
        String strPoliceStation = modelStatusLists.get(i).getPoliceStationName();
        String strDriverName = modelStatusLists.get(i).getDriverName();
        String strWorkingHrs = modelStatusLists.get(i).getTotalWorkingHour();
        String strErvNumber = modelStatusLists.get(i).getErv();

        statusViewHolder.tv_shifting.setText(strShifting);
        statusViewHolder.tv_status.setText(strStatus);
        statusViewHolder.tv_driverName.setText(strDriverName);
        statusViewHolder.tv_policeStation.setText(strPoliceStation);
        statusViewHolder.tv_workHrs.setText(strWorkingHrs);
        statusViewHolder.tv_ervNumber.setText(strErvNumber);


    }

    @Override
    public int getItemCount() {
        return modelStatusLists.size();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_status_shifting)
        TextView tv_shifting;

        @BindView(R.id.tv_status_status)
        TextView tv_status;

        @BindView(R.id.tv_status_driver_name)
        TextView tv_driverName;

 @BindView(R.id.tv_status_police_station_name)
        TextView tv_policeStation;

 @BindView(R.id.tv_status_work_hr)
        TextView tv_workHrs;

 @BindView(R.id.tv_status_erv_number)
        TextView tv_ervNumber;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
