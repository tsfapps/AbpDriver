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
import com.abp.driver.fragment.EvrPoliceListFragment;
import com.abp.driver.model.district.ModelDistrictList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EvrDistrictAdapter extends RecyclerView.Adapter<EvrDistrictAdapter.EvrDistrictHolder> {

    private List<ModelDistrictList> modelDistrictLists;

    private Context mContext;
    private FragmentManager mFragmentManager;

    public EvrDistrictAdapter(Context mContext, FragmentManager mFragmentManager, List<ModelDistrictList> list) {
        this.mContext = mContext;
        this.mFragmentManager = mFragmentManager;
        this.modelDistrictLists = list;
    }

    @NonNull
    @Override
    public EvrDistrictHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_evr_district_item, viewGroup, false);

        return new EvrDistrictHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EvrDistrictHolder evrDistrictHolder, final int i) {
        String strDistrictName = modelDistrictLists.get(i).getDistrictName();
        evrDistrictHolder.tv_evrDistrictList.setText(strDistrictName);

        evrDistrictHolder.ll_EvrDistrictList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentManager.beginTransaction().replace(R.id.container_main, EvrPoliceListFragment.newInstance(modelDistrictLists.get(i).getDistrictId())).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelDistrictLists.size();
    }

    public class EvrDistrictHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ll_evr_district_list)
        LinearLayout ll_EvrDistrictList;

        @BindView(R.id.tv_evr_district_list)
        TextView tv_evrDistrictList;
        public EvrDistrictHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
