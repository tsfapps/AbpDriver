package com.abp.driver.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.abp.driver.R;
import com.abp.driver.model.driver.DriverAttendanceList;
import com.abp.driver.utils.CustomLog;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DriverAttDetAdapter extends RecyclerView.Adapter<DriverAttDetAdapter.DriverViewHolder>{
    private List<DriverAttendanceList> driverAttendanceLists = DriverAttendanceList.listAll(DriverAttendanceList.class);
    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_driver_attendance_detail_item, viewGroup, false);
        return new DriverViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder driverViewHolder, int i) {
        if (driverAttendanceLists.size()>0) {
            String chkIn = driverAttendanceLists.get(i).getTimeIn();
            String chkOut = driverAttendanceLists.get(i).getTimeOut();
            String date = driverAttendanceLists.get(i).getCheckInDate();
            String totalTime = driverAttendanceLists.get(i).getTotalTime();
            driverViewHolder.tv_dri_att_det_chk_in.setText(chkIn);
            driverViewHolder.tv_dri_att_det_chk_out.setText(chkOut);
            driverViewHolder.tv_dri_att_det_working_hrs.setText(totalTime);
            driverViewHolder.tv_dri_att_det_date.setText(date);
        }
    }
    @Override
    public int getItemCount() {
        return driverAttendanceLists.size();
    }
   public class DriverViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_chk_in_dri_att_det)
        TextView tv_dri_att_det_chk_in;
        @BindView(R.id.tv_chk_out_dri_att_det)
        TextView tv_dri_att_det_chk_out;
        @BindView(R.id.tv_working_hrs_dri_att_det)
        TextView tv_dri_att_det_working_hrs;
        @BindView(R.id.tv_date_dri_att_det)
        TextView tv_dri_att_det_date;
        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}