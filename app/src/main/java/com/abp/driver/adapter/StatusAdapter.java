package com.abp.driver.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abp.driver.R;
import com.abp.driver.model.status.StatusDistrictList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder>{

    private List<StatusDistrictList> statusDistrictLists = StatusDistrictList.listAll(StatusDistrictList.class);
    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_status_item, viewGroup, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {

        String stateName = statusDistrictLists.get(i).getStateName();
        String districtName = statusDistrictLists.get(i).getDistrictName();
        String policeNumber = statusDistrictLists.get(i).getNoOfPolicestation().toString();

        statusViewHolder.tv_stateName.setText(stateName);
        statusViewHolder.tv_districtName.setText(districtName);
        statusViewHolder.tv_policeNumber.setText(policeNumber);

    }

    @Override
    public int getItemCount() {
        return statusDistrictLists.size();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_status_state_name)
        TextView tv_stateName;

        @BindView(R.id.tv_status_district_name)
        TextView tv_districtName;

        @BindView(R.id.tv_no_pol_station)
        TextView tv_policeNumber;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
