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
import com.abp.driver.fragment.EvrFragment;
import com.abp.driver.model.police.ModelPoliceList;
import com.abp.driver.utils.CustomLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EvrPoliceAdapter extends RecyclerView.Adapter<EvrPoliceAdapter.EvrPoliceHolder> {

    private List<ModelPoliceList> modelPoliceLists = ModelPoliceList.listAll(ModelPoliceList.class);

    private Context mContext;
    private FragmentManager mFragmentManager;

    public EvrPoliceAdapter(Context mContext, FragmentManager mFragmentManager) {
        this.mContext = mContext;
        this.mFragmentManager = mFragmentManager;
    }

    @NonNull
    @Override
    public EvrPoliceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_evr_police_item, viewGroup, false);
        return new EvrPoliceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EvrPoliceHolder evrPoliceHolder, int i) {

        String policeStationName = modelPoliceLists.get(0).getPolicestationname();
        evrPoliceHolder.tv_policeList.setText(policeStationName);

        CustomLog.d("PoliceList", policeStationName);
        evrPoliceHolder.ll_policeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentManager.beginTransaction().replace(R.id.container_main, new EvrFragment()).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelPoliceLists.size();
    }

    public class EvrPoliceHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_evr_police_list)
        TextView tv_policeList;

        @BindView(R.id.ll_evr_police_list)
        LinearLayout ll_policeList;
        public EvrPoliceHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
