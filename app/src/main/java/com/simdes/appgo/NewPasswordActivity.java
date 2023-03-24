/*
 * Create by Brian Dwi Murdianto
 * Email : dwibrian120@gmail.com
 */

package com.simdes.appgo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.simdes.appgo.network.Config;
import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.GeneralHelper;
import com.simdes.appgo.utils.PopHelper;
import com.simdes.appgo.utils.UserHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class NewPasswordActivity extends AppCompatActivity {

    public PopHelper ph;
    public int pos;
    public String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        ph = new PopHelper(this);

        pos = getIntent().getIntExtra("pos", 0);

        _initView();
    }

    public EditText inpPasswordOld, inpPassword, inpPassconf;
    public TextView btnReset, txtPasswordOld;

    public void _initView(){
        txtPasswordOld = findViewById(R.id.txtPasswordOld);
        inpPasswordOld = findViewById(R.id.inpPasswordOld);
        inpPassword = findViewById(R.id.inpPassword);
        inpPassconf = findViewById(R.id.inpPassconf);
        btnReset = findViewById(R.id.btnReset);

        _actionView();
    }

    public boolean checkPassOld = true;

    public void _actionView(){
        if(pos == 1){
            txtPasswordOld.setVisibility(View.GONE);
            inpPasswordOld.setVisibility(View.GONE);
            email = UserHelper.forgot.email;
        }else {
            txtPasswordOld.setVisibility(View.VISIBLE);
            inpPasswordOld.setVisibility(View.VISIBLE);
            email = UserHelper.user.user.email;
        }
        btnReset.setOnClickListener(view -> {
            if(pos != 1){
                if(!inpPasswordOld.getText().toString().trim().equals(UserHelper.pass)){
                    checkPassOld = false;
                }
            }

            if(checkPassOld){
                if(inpPassword.getText().toString().trim().equals(inpPassconf.getText().toString().trim())){
                    reqResetPassword();
                }else {
                    GeneralHelper.ToastNew(this, "Incorrect New password");
                }
            }else {
                GeneralHelper.ToastNew(this, "Incorrect Old Password");
            }
        });
    }

    @SuppressLint("CheckResult")
    public void reqResetPassword(){
        ph.popLoading();
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);

        request.reqResetPassword(email, inpPassword.getText().toString().trim()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if(respData.response){
                                UserHelper.user = respData.data;
                                ph.popDismiss();
                                UserHelper.pass = inpPassword.getText().toString().trim();
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), respData.message, Toast.LENGTH_SHORT).show();
                                ph.popDismiss();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            throwable.printStackTrace();
                            ph.popDismiss();
                        }
                );
    }
}