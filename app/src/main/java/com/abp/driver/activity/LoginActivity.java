package com.abp.driver.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.abp.driver.ApiClient.ApiClients;
import com.abp.driver.Interface.Api;
import com.abp.driver.R;
import com.abp.driver.model.login.ModelLogin;
import com.abp.driver.model.login.ModelLoginList;
import com.abp.driver.utils.Constant;
import com.abp.driver.utils.CustomLog;
import com.abp.driver.utils.SharedPreference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity{



    public static final String MyPreference = "MyPref";

    @BindView(R.id.et_phone_login )
    protected EditText et_email;
    @BindView(R.id.et_password_login)
    protected EditText et_password;
    @BindView(R.id.sp_login)
    protected AppCompatSpinner mSpinner;
    private SharedPreference mSharedPreference;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);//butter knife binding
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle("Log In");
//        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        mSharedPreference = new SharedPreference(this);
    }

    public void callLoginApi(final String type, String phoneNo,String pass){
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please wait login in progress....");
        mDialog.show();
        String API_KEY = Constant.API_KEY;
        String username = phoneNo;
        String password = pass;
        String mType = null;
        if (type.equals(Constant.LOGIN_TYPE_DRIVER)) {
            mType = Constant.LOGIN_TYPE_DRIVER;
        } else if (type.equals(Constant.LOGIN_SPINNER_TYPE_STATE_MANAGER)) {
            mType = "statemanager";
        } else if (type.equals(Constant.LOGIN_SPINNER_TYPE_DISTRICT_MANAGER)) {
            mType = "districtmanager";
        }

        new CallApiAsyncTask(this,API_KEY, mType, username, password).execute();
    }

    private void startDashboardAcivity(String type) {
        List<ModelLoginList> mList = ModelLoginList.listAll(ModelLoginList.class);
        if (mList.size() > 0) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("login_type", type);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            ModelLoginList.deleteAll(ModelLoginList.class);
            mSharedPreference.clearAllData();
            Toast.makeText(getApplicationContext(),"Error occur please retry !",Toast.LENGTH_SHORT).show();
        }
    }

    private void startApiHandler(final String type) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDialog.isShowing()) {
                    mDialog.cancel();
                }
                startDashboardAcivity(type);
            }
        }, 500);
    }

    @OnClick(R.id.btn_login)
    public void onButtonClick(View view) {
        submitBtn();
    }
    private void submitBtn(){
        int type = mSpinner.getSelectedItemPosition();
        String mPhoneNo = et_email.getText().toString().trim();
        String mPass = et_password.getText().toString().trim();

        if (mPhoneNo.isEmpty()){
            et_email.setError("Enter phone no");
        }else if (mPass.isEmpty()){
            et_password.setError("Enter the password");
        }else {
            if (type > 0) {
                callLoginApi(mSpinner.getSelectedItem().toString().toLowerCase(),mPhoneNo,mPass);
            } else {
                Toast.makeText(getApplicationContext(), "Select type !", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class CallApiAsyncTask extends AsyncTask<Void, Void, Void> {
        private final String mApiKey;
        private final String mType;
        private final String mUserName;
        private final String mPassword;

        public CallApiAsyncTask(LoginActivity loginActivity, String api_key, String type, String username, String password) {
            this.mApiKey = api_key;
            this.mType = type;
            this.mUserName = username;
            this.mPassword = password;
        }

        @Override
        protected void onPreExecute() {
        }

        protected Void doInBackground(Void... args) {
            Api api = ApiClients.getApiClients().create(Api.class);
            Call<ModelLogin> call = api.loginUser(mApiKey, mType, mUserName, mPassword);
            call.enqueue(new Callback<ModelLogin>() {
                @Override
                public void onResponse(@NonNull Call<ModelLogin> call, @NonNull Response<ModelLogin> response) {
                    ModelLogin modelLogin = response.body();
                    CustomLog.d("danny","LoginActivity onResponse");
                    ModelLoginList.deleteAll(ModelLoginList.class);
                   if (modelLogin != null && modelLogin.getData() != null) {
                       for (ModelLoginList modelLoginList : modelLogin.getData()) {
                           modelLoginList.save();
                       }
                       List<ModelLoginList> mList = ModelLoginList.listAll(ModelLoginList.class);
                       if (mList.size() > 0) {
                           CustomLog.d("danny", "LoginActivity onResponse...data added success.. list size :" + mList.size());
                           mSharedPreference.setUserData(mList.get(0).getName(), mList.get(0).getPhoneno(), mList.get(0).getProfilePic(),
                                   mList.get(0).getStateId(), mList.get(0).getDistrictId(), mList.get(0).getLogintype(),
                                   mList.get(0).getEvrId(), mList.get(0).getErvNo());
                           startApiHandler(mType);
                       } else {
                           Toast.makeText(LoginActivity.this, ""+modelLogin.getMessage(), Toast.LENGTH_SHORT).show();
                           if (mDialog.isShowing()) {
                               mDialog.dismiss();
                           }

                       }
                   } else {
                       assert response.body() != null;
                       if (response.body().getMessage() != null) {
                           Toast.makeText(LoginActivity.this,""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                       } else {
                           Toast.makeText(LoginActivity.this, "No data found please retry !", Toast.LENGTH_SHORT).show();
                       }
                       if (mDialog.isShowing()) {
                           mDialog.dismiss();
                       }
                   }

                }

                @Override
                public void onFailure(Call<ModelLogin> call, Throwable t) {
                    CustomLog.d("danny","onFailure..."+ call.toString());
                    Toast.makeText(LoginActivity.this,"Server Error ! Please check internet connection",Toast.LENGTH_SHORT).show();
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }

                }
            });
            return null;
        }

        protected void onPostExecute(Void result) {
            // do UI work here
        }
    }

}
