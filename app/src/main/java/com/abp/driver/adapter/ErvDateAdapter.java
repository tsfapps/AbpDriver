package com.abp.driver.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abp.driver.R;
import com.abp.driver.fragment.ErvFinalFragment;
import com.abp.driver.fragment.StatusFinalFragment;
import com.abp.driver.model.date.ModelDateList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErvDateAdapter extends RecyclerView.Adapter<ErvDateAdapter.DateViewHolder>  {

   private List<ModelDateList> modelDateLists;
   private Context mContext;
   private FragmentManager mFragmentManager;

    public ErvDateAdapter(List<ModelDateList> modelDateLists, Context mContext, FragmentManager mFragmentManager) {
        this.modelDateLists = modelDateLists;
        this.mContext = mContext;
        this.mFragmentManager = mFragmentManager;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_erv_date_item, viewGroup, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder dateViewHolder, final int i) {
        dateViewHolder.textViewDate.setText(modelDateLists.get(i).getCreateDate());
        dateViewHolder.linearLayoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentManager.beginTransaction().replace(R.id.container_main, StatusFinalFragment.newInstance(modelDateLists.get(i).getDistrictid(),modelDateLists.get(i).getCreateDate(),modelDateLists.get(i).getStateid())).addToBackStack(null).commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        return modelDateLists.size();
    }

    public class DateViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ll_evr_date)
        protected LinearLayout linearLayoutDate;
        @BindView(R.id.tv_evr_date)
        protected TextView textViewDate;
        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
