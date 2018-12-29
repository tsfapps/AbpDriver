package com.abp.driver.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.abp.driver.ApiClient.ApiClients;
import com.abp.driver.Interface.Api;
import com.abp.driver.R;
import com.abp.driver.model.driver.DriverAttendance;
import com.abp.driver.model.login.ModelLogin;
import com.abp.driver.model.login.ModelLoginList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.CustomLog;
import com.google.gson.Gson;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity{


    private static String NAME = "E_NAME";

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
    private ModelLoginList modelDriversList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);//butter knife binding
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Log In");
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        retrofitData();
    }

    public void retrofitData(){
        Api api = ApiClients.getApiClients().create(Api.class);

        String API_KEY = "abpn";
        String type = "driver";
        String username = "9005103632";
        String password = "MKS12322";


        Call<ModelLogin> call = api.loginUser(API_KEY, type, username, password);
        call.enqueue(new Callback<ModelLogin>() {
            @Override
            public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {
              ModelLogin modelLogin = response.body();
                if (modelLogin.getStatus().equals(Constant.SUCCESS_CODE)){
                    ModelLoginList.deleteAll(ModelLoginList.class);
                    for (ModelLoginList modelLoginList : modelLogin.getData()){
                        modelLoginList.save();
                    }

                }
            }

            @Override
            public void onFailure(Call<ModelLogin> call, Throwable t) {
                CustomLog.d("danny","flied..."+ call.toString());

            }
        });
    }

    @OnClick(R.id.btn_login)
    public void onButtonClick(View view) {
                submitBtn();
    }
    private void submitBtn(){
        int type = mSpinner.getSelectedItemPosition();
       // String name = modelDriver.getName();
      String  email_local = et_email.getText().toString().trim();
        pass = et_password.getText().toString().trim();

        if (email_local.isEmpty()){
            et_email.setError("Enter the user id");
        }else if (pass.isEmpty()){
            et_password.setError("Enter the password");
        }else {
            if (type > 0) {
                String API_KEY = "abpn";
                String phone = "9728265402";
                Api api = ApiClients.getApiClients().create(Api.class);
                Call<DriverAttendance> call = api.driverAttendance(API_KEY, phone);
                call.enqueue(new Callback<DriverAttendance>() {
                    @Override
                    public void onResponse(Call<DriverAttendance> call, Response<DriverAttendance> response) {
                        DriverAttendance driverAttendance = response.body();
                        String name = driverAttendance.getData().get(0).getPhoneNo();

                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        String EMAIL = "EMAIL";
                       // String TYPE = "TYPE";
                        intent.putExtra(EMAIL, name);
                       // intent.putExtra(TYPE,type);
                        startActivity(intent);


                        CustomLog.d("akram", "phone : "+name);
                    }

                    @Override
                    public void onFailure(Call<DriverAttendance> call, Throwable t) {

                    }
                });

              //  finish();
            } else {
                Toast.makeText(getApplicationContext(),"Select type !",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void driverAtt(){

    }

}
