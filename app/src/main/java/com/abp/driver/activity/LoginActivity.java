package com.abp.driver.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.abp.driver.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static String EMAIL = "EMAIL";
   // private static String PASS = "PASS";

    @BindView(R.id.et_email_login)
    EditText et_email;

    @BindView(R.id.et_password_login)
    EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.btn_login)
    public void submitBtn(View view){
        String email = et_email.getText().toString().trim();
        String pass = et_password.getText().toString().trim();
        if (email.isEmpty()){
            et_email.setError("Enter the user id");
        }else if (pass.isEmpty()){
            et_password.setError("Enter the password");
        }else {

            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra(EMAIL, email);
            intent.putExtra("type",1);
            startActivity(intent);
            finish();

        }
    }



}
