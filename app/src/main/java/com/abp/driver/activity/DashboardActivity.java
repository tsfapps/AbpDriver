package com.abp.driver.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.abp.driver.R;
import com.abp.driver.fragment.DriverFragment;
import com.abp.driver.fragment.DistrictManagerFragment;
import com.abp.driver.fragment.StateManagerFragment;
import com.abp.driver.pojo.ModelProfile;
import com.bumptech.glide.Glide;

import java.util.List;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Toolbar toolbar;
    private TextView mToolbarTitle;
    private ModelProfile modelProfiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar =  findViewById(R.id.toolbar);
        mToolbarTitle =  findViewById(R.id.toolbar_title);
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
        int[] pro_img = {R.drawable.pro_img};
        ModelProfile modelProfile;
        modelProfile = new ModelProfile();
        modelProfile.setUser_name("Tousif Akram");
        modelProfile.setUser_image(pro_img[0]);


        String userEmail = getIntent().getExtras().getString("EMAIL");
        int userType = getIntent().getExtras().getInt("TYPE");
        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView tv_header_name = navigationView.getHeaderView(0).findViewById(R.id.tv_header_user_name);
        tv_header_name.setText(modelProfile.getUser_name());

        ImageView header_img = navigationView.getHeaderView(0).findViewById(R.id.iv_header_user_image);
        Glide.with(this).load(modelProfile.getUser_image()).into(header_img);
        navigationView.setNavigationItemSelectedListener(this);
        startDashboardFragment(userType);
    }

    private void startDashboardFragment(int userType) {
        switch (userType){
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new DriverFragment()).addToBackStack(null).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new DistrictManagerFragment()).addToBackStack(null).commit();

                break;
            case 3:
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
           finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            SharedPreferences SM = getSharedPreferences(LoginActivity.MyPreference, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = SM.edit();
            edit.clear();
            edit.apply();
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_attendance) {

        } else if (id == R.id.nav_status) {

        } else if (id == R.id.nav_evr) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void profileImage(){





    }
}
