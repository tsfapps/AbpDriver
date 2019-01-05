package com.abp.driver.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abp.driver.R;

public class EvrAapter extends RecyclerView.Adapter<EvrAapter.EvrViewHolder> {

    @NonNull
    @Override
    public EvrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_evr_item, viewGroup, false);
        return new EvrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EvrViewHolder evrViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class EvrViewHolder extends RecyclerView.ViewHolder{

        public EvrViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
