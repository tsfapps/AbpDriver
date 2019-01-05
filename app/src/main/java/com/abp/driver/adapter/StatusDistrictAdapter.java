package com.abp.driver.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abp.driver.R;

public class StatusDistrictAdapter extends RecyclerView.Adapter<StatusDistrictAdapter.DistrictListHolder> {

    @NonNull
    @Override
    public DistrictListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_status_district_item, viewGroup, false);
        return new DistrictListHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DistrictListHolder districtListHolder, int i) {
    }
    @Override
    public int getItemCount() {
        return 30;
    }
    public class DistrictListHolder extends RecyclerView.ViewHolder{

        public DistrictListHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
