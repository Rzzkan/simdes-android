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
import com.simdes.appgo.utils.PopHelper;
import com.simdes.appgo.utils.UserHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    EditText inpEmail, inpName, inpPhone, inpPassword, inpPassconf;
    TextView btnRegister;
    PopHelper ph;
    String email = "", name = "", phone = "", password = "", passconf = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ph = new PopHelper(this);
        _initView();
    }

    public void _initView(){
        inpEmail = findViewById(R.id.inpEmail);
        inpName = findViewById(R.id.inpName);
        inpPhone = findViewById(R.id.inpPhone);
        inpPassword = findViewById(R.id.inpPassword);
        inpPassconf = findViewById(R.id.inpPassconf);

        btnRegister = findViewById(R.id.btnRegister);

        _actionView();
    }

    public void _actionView(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runRegister();
            }
        });
    }

    public void runRegister(){
        email = inpEmail.getText().toString().trim();
        name = inpName.getText().toString().trim();
        phone = inpPhone.getText().toString().trim();
        password = inpPassword.getText().toString().trim();
        passconf = inpPassconf.getText().toString().trim();
        reqRegister();
    }

    @SuppressLint("CheckResult")
    public void reqRegister(){
        ph.popLoading();
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);

        request.reqRegister(email, name, phone, password, passconf).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            UserHelper.user = respData.data;
                            ph.popDismiss();
                            finish();
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            throwable.printStackTrace();
                            ph.popDismiss();
                        }
                );
    }
}