package com.abp.driver.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abp.driver.R;

public class DriverAttDetAdapter extends RecyclerView.Adapter<DriverAttDetAdapter.DriverViewHolder>{


    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_driver_attendance_detail_item, viewGroup, false);

        return new DriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder driverViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DriverViewHolder extends RecyclerView.ViewHolder{



        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
