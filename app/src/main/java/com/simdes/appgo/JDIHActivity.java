package com.simdes.appgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.simdes.appgo.adapter.ApbDesaAdapter;
import com.simdes.appgo.adapter.JdihAdapter;
import com.simdes.appgo.model.ApbDesaModel;
import com.simdes.appgo.model.JdihModel;
import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.GeneralHelper;
import com.simdes.appgo.utils.PopHelper;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class JDIHActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jdihactivity);
        ph = new PopHelper(this);
        gh = new GeneralHelper();
        _initView();
    }

    public PopHelper ph;
    public GeneralHelper gh;
    public ImageView btnBack;

    public void _initView() {
        btnBack = findViewById(R.id.btnBack);

        _actionView();
    }

    public void _actionView() {
        btnBack.setOnClickListener(view -> finish());
        updateView();
    }

    public void updateView() {
        ph.popLoading();
        reqApbDesa();
    }


    @SuppressLint("CheckResult")
    public void reqApbDesa() {
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqJdih()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if (respData.response) {
                                initRVJdih(respData.data);
                            } else {
                                Toast.makeText(getApplicationContext(), respData.message, Toast.LENGTH_SHORT).show();
                            }
                            ph.popDismiss();
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            throwable.printStackTrace();
                            ph.popDismiss();
                        }
                );
    }

    public void initRVJdih(ArrayList<ApbDesaModel> respData) {
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RecyclerView rvView = findViewById(R.id.rvJdih);
        rvView.setLayoutManager(layoutManager);

        JdihAdapter viewAdapter = new JdihAdapter(respData, getApplicationContext());
        rvView.setAdapter(viewAdapter);
    }
}