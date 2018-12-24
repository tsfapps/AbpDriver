package com.abp.driver.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;
import android.widget.Toast;

import com.abp.driver.R;
import com.abp.driver.service.LocationService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity{


    public static final String MyPreference = "MyPref";
    public boolean aBoolean = false;
    private SharedPreferences SM;
    // private static String PASS = "PASS";

    @BindView(R.id.et_phone_login )
    protected EditText et_email;
    @BindView(R.id.et_password_login)
    protected EditText et_password;
    @BindView(R.id.sp_login)
    protected AppCompatSpinner mSpinner;
    private Toolbar toolbar;
    private String email;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Log In");
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
    }
    @OnClick(R.id.btn_login)

    public void onButtonClick(View view) {
                submitBtn();
    }
    private void submitBtn(){
        int type = mSpinner.getSelectedItemPosition();
        email = et_email.getText().toString().trim();
        pass = et_password.getText().toString().trim();
        if (email.isEmpty()){
            et_email.setError("Enter the user id");
        }else if (pass.isEmpty()){
            et_password.setError("Enter the password");
        }else {
            if (type > 0) {
                Intent intent = new Intent(this, DashboardActivity.class);
                String EMAIL = "EMAIL";
                String TYPE = "TYPE";
                intent.putExtra(EMAIL, email);
                intent.putExtra(TYPE,type);
                startActivity(intent);
              //  finish();
            } else {
                Toast.makeText(getApplicationContext(),"Select type !",Toast.LENGTH_SHORT).show();
            }

        }
    }

}
