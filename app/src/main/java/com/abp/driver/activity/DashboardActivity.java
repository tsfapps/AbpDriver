package com.abp.driver.activity;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abp.driver.R;
import com.abp.driver.fragment.DistrictManagerFragment;
import com.abp.driver.fragment.AttendanceFragment;

import com.abp.driver.fragment.ProfileFragment;
import com.abp.driver.fragment.StateManagerFragment;
import com.abp.driver.model.PunchInOut.ModelPunchInOutLocal;
import com.abp.driver.model.driver.DriverAttendanceList;
import com.abp.driver.model.login.ModelLoginList;
import com.abp.driver.receiver.NetworkStateChangeReceiver;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.CustomLog;
import com.abp.driver.utils.SharedPreference;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "DashboardActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    private List<ModelLoginList> mLoginList;
    private SharedPreference mSharedPreference;
    private ProgressDialog mDialog;
    private boolean isDataSyncStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        profileImage();
        init();


    }

    private void init() {
        mLoginList = ModelLoginList.listAll(ModelLoginList.class);
        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView tv_header_name = navigationView.getHeaderView(0).findViewById(R.id.tv_header_user_name);
        ImageView iv_header_img = navigationView.getHeaderView(0).findViewById(R.id.iv_header_user_image);
        iv_header_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new ProfileFragment()).addToBackStack(null).commit();
                DrawerLayout drawer =  findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        mSharedPreference = new SharedPreference(this);
        tv_header_name.setText(mSharedPreference.getUserName());
        Glide.with(this).load(mSharedPreference.getUserPic()).into(iv_header_img);

        navigationView.setNavigationItemSelectedListener(this);
        if (mLoginList.size() > 0) {
            startDashboardFragment(mLoginList.get(0).getLogintype());
        } else {
            String userType = getIntent().getExtras().getString("login_type");
            startDashboardFragment(userType);
        }
    }


    private void startDashboardFragment(String userType) {
        switch (userType){
            case Constant.LOGIN_TYPE_DRIVER:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new AttendanceFragment()).addToBackStack(null).commit();
                break;
            case Constant.LOGIN_TYPE_DISTRICT_MANAGER:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new DistrictManagerFragment()).addToBackStack(null).commit();
                break;
            case Constant.LOGIN_TYPE_STATE_MANAGER:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new StateManagerFragment()).addToBackStack(null).commit();
                break;

        }

    }

    public void setToolbarTitle(String title){
        mToolbarTitle.setText(title);
    }


    @Override
    public void onBackPressed() {
       onBackPressedCalled();

    }

    public void onBackPressedCalled(){
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            onBackPressedFragment();
        }
    }

    private void onBackPressedFragment(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }
        else {
           finish();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_attendance) {
          getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new AttendanceFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new ProfileFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "https://play.google.com/store/apps/details?id=" + this.getPackageName();
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download the App");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_rate) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + this.getPackageName())));
        } else if (id == R.id.nav_logout) {
            userLogoutCall();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void userLogoutCall() {
        /*List<ModelPunchInOutLocal> mList = ModelPunchInOutLocal.findWithQuery(ModelPunchInOutLocal.class,
                "SELECT * FROM MODEL_PUNCH_IN_OUT_LOCAL where phone_no = '" + mSharedPreference.getUserPhoneNo() + "' AND time_in != '"+Constant.EMPTY+"' AND time_out = '"+Constant.EMPTY+"' ORDER BY id ASC");
        if (mList.size() > 0) {
            Toast.makeText(this,"Punch out before logout !",Toast.LENGTH_SHORT).show();
            return;
        }*/
        List<ModelPunchInOutLocal> localDetails = ModelPunchInOutLocal.findWithQuery(ModelPunchInOutLocal.class,
                "SELECT * FROM MODEL_PUNCH_IN_OUT_LOCAL where phone_no = '" + mSharedPreference.getUserPhoneNo() + "' AND is_synced = 'N' ORDER BY id ASC");
        if (localDetails.size() > 0) {
            if (isNetworkAvailable()) {
                mDialog = new ProgressDialog(this);
                mDialog.setMessage("Please wait data sync in progress....");
                mDialog.show();
                startDataSyncHandler();
                if (!isDataSyncStarted) {
                    registerNetworkReceiver();
                }
            } else {
                Toast.makeText(this,"Please connect internet to Sync attendance data",Toast.LENGTH_SHORT).show();
            }
        } else {
            isDataSyncStarted = false;
            mSharedPreference.clearAllData();
            ModelLoginList.deleteAll(ModelLoginList.class);
            ModelPunchInOutLocal.deleteAll(ModelPunchInOutLocal.class);
            DriverAttendanceList.deleteAll(DriverAttendanceList.class);

            if (mDialog != null && mDialog.isShowing()){
                mDialog.dismiss();
            }
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void startDataSyncHandler() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               userLogoutCall();
            }
        }, 3000);
    }
    private void registerNetworkReceiver() {
        isDataSyncStarted = true;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkStateChangeReceiver(), intentFilter);
    }

    public void profileImage(){

    }

    public boolean isGpsEnable(){
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return statusOfGPS;
    }

    public void showDialogueGps(boolean cancelableValue) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(Constant.GPS_DIALOGUE_MESSAGE)
                    .setCancelable(cancelableValue)
                    .setPositiveButton(Constant.ENABLE, new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent,10);
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            CustomLog.e(TAG, e.toString());
        }
    }

    @SuppressWarnings("deprecation")
    public boolean isNetworkAvailable() {
        boolean condition = false;
        try {
            final ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                condition = false;
            } else {
                final NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            condition = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            CustomLog.e(TAG,e.toString());
        }
        return condition;
    }
}
