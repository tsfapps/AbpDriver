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
import android.widget.Toast;

import com.abp.driver.R;
import com.abp.driver.fragment.StatusFragment;
import com.abp.driver.model.status.StatusDistrictList;
import com.abp.driver.utils.CustomLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusDistrictAdapter extends RecyclerView.Adapter<StatusDistrictAdapter.DistrictListHolder> {

   private List<StatusDistrictList> statusDistrictLists = StatusDistrictList.listAll(StatusDistrictList.class);


  private Context mContext;
  private FragmentManager mFragmentManager;

    public StatusDistrictAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public StatusDistrictAdapter(Context mContext, FragmentManager mFragmentManager) {
        this.mContext = mContext;
        this.mFragmentManager = mFragmentManager;
    }

    @NonNull
    @Override
    public DistrictListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_status_district_item, viewGroup, false);
        return new DistrictListHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DistrictListHolder districtListHolder, int i) {
        if (statusDistrictLists.size()>0) {
            final String disList = statusDistrictLists.get(i).getDistrictName();
            final String disId = statusDistrictLists.get(i).getDistrictId();
            districtListHolder.tv_district_list.setText(disList);
            CustomLog.d("districtName", statusDistrictLists.get(0).getDistrictName());
            districtListHolder.ll_district_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFragmentManager.beginTransaction().replace(R.id.container_main, new StatusFragment()).addToBackStack(null).commit();


                    Toast.makeText(mContext, disId, Toast.LENGTH_LONG).show();
                }
            });
        }

    }
    @Override
    public int getItemCount() {
        return statusDistrictLists.size();
    }
    public class DistrictListHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_district_list)
        TextView tv_district_list;

        @BindView(R.id.ll_district_list)
        LinearLayout ll_district_list;

        public DistrictListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }
}
