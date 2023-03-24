package com.simdes.appgo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.simdes.appgo.network.Config;
import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.GeneralHelper;
import com.simdes.appgo.utils.PopHelper;
import com.simdes.appgo.utils.PreferencesHelper;
import com.simdes.appgo.utils.UserHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    public EditText inpEmail, inpPassword;
    public TextView btnLogin, btnRegister, btnForgotPassword;
    public PopHelper ph;
    public String email = "", password = "";
    public RelativeLayout laySplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ph = new PopHelper(this);
        _initView();
    }

    public void _initView(){
        inpEmail = findViewById(R.id.inpEmail);
        inpPassword = findViewById(R.id.inpPassword);

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        laySplash = findViewById(R.id.laySplash);

        _actionView();
    }

    public void _actionView(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runLogin();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GeneralHelper().intentAct(com.simdes.appgo.LoginActivity.this, RegisterActivity.class);
            }
        });

        btnForgotPassword.setOnClickListener(view -> {
            new GeneralHelper().intentAct(this, ForgotPasswordActivity.class);
        });
    }

    public void runLogin(){
        email = inpEmail.getText().toString().trim();
        password = inpPassword.getText().toString().trim();
        reqLogin();
    }

    @SuppressLint("CheckResult")
    public void reqLogin(){
        ph.popLoading();
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);

        request.reqLogin(email, password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if(respData.response){
                                UserHelper.user = respData.data;
                                ph.popDismiss();
                                UserHelper.pass = password;
                                PreferencesHelper.setRegisteredUser(getApplicationContext(),email);
                                PreferencesHelper.setRegisteredPass(getApplicationContext(),password);
                                UserHelper.api_token = respData.data.user.api_token;
                                new GeneralHelper().intentAct(com.simdes.appgo.LoginActivity.this, MainActivity.class);
                                finish();
                            }else{
                                laySplash.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), respData.message, Toast.LENGTH_SHORT).show();
                                ph.popDismiss();
                            }
                        },
                        throwable -> {
                            laySplash.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            throwable.printStackTrace();
                            ph.popDismiss();
                        }
                );
    }

    @Override
    public void onResume(){
        if(PreferencesHelper.getRegisteredUser(this) != null){
            if (!PreferencesHelper.getRegisteredUser(this).equals("")) {
                inpEmail.setText(PreferencesHelper.getRegisteredUser(this));
                inpPassword.setText(PreferencesHelper.getRegisteredPass(this));
                laySplash.setVisibility(View.VISIBLE);
                runLogin();
            }else {
                laySplash.setVisibility(View.GONE);
            }
        }else {
            laySplash.setVisibility(View.GONE);
        }

        super.onResume();
    }
}