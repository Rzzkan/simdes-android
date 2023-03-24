/*
 * Create by Brian Dwi Murdianto
 * Email : dwibrian120@gmail.com
 */

package com.simdes.appgo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    public PopHelper ph;
    public Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ph = new PopHelper(this);
        dialog = new Dialog(this);
        _initView();
    }

    public EditText inpEmail;
    public TextView btnReset;

    public void _initView(){
        inpEmail = findViewById(R.id.inpEmail);
        btnReset = findViewById(R.id.btnReset);

        _actionView();
    }

    public void _actionView(){
        btnReset.setOnClickListener(view -> {
            reqForgot();
        });
    }

    @SuppressLint("CheckResult")
    public void reqForgot(){
        ph.popLoading();
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);

        request.reqForgot(inpEmail.getText().toString().trim()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if(respData.response){
                                UserHelper.forgot = respData.data;
                                ph.popDismiss();
                                popCheckPass();
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

    public void popCheckPass(){
        dialog.setContentView(R.layout.pop_code_reset);
        dialog.setCancelable(false);
        EditText inpCode = dialog.findViewById(R.id.inpCode);
        TextView btnCheckCode = dialog.findViewById(R.id.btnCheckCode);

        btnCheckCode.setOnClickListener(view -> {
            if(inpCode.getText().toString().trim().equals(UserHelper.forgot.code)){
                Intent o = new Intent(this, NewPasswordActivity.class);
                o.putExtra("pos", 1);
                startActivity(o);
                dialog.dismiss();
                finish();
            }else {
                GeneralHelper.ToastNew(this, "Wrong Code!");
            }
        });

        dialog.show();
    }
}