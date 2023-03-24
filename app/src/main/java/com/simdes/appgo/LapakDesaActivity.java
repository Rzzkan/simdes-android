package com.simdes.appgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.simdes.appgo.adapter.BeritaAdapter;
import com.simdes.appgo.adapter.LapakHargaAdapter;
import com.simdes.appgo.model.BeritaModel;
import com.simdes.appgo.model.LapakDesaModel;
import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.GeneralHelper;
import com.simdes.appgo.utils.PopHelper;
import com.simdes.appgo.utils.UserHelper;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LapakDesaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapak_desa);
        ph = new PopHelper(this);
        gh = new GeneralHelper();
        _initView();
    }

    public PopHelper ph;
    public GeneralHelper gh;
    public ImageView btnBack;
    public RecyclerView rvLapakDesa;

    public void _initView(){
        rvLapakDesa = findViewById(R.id.rvLapakDesa);
        btnBack = findViewById(R.id.btnBack);

        _actionView();
    }

    public void _actionView(){
        btnBack.setOnClickListener(view -> finish());
        updateView();
    }

    public void updateView(){
        ph.popLoading();
        reqLapakDesa();
    }



    @SuppressLint("CheckResult")
    public void reqLapakDesa(){
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqLapakDesa()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if(respData.response){
                                initRVLapakDesa(respData.data);
                            }else{
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

    public void initRVLapakDesa(ArrayList<LapakDesaModel> respData) {
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RecyclerView rvView = findViewById(R.id.rvLapakDesa);
        rvView.setLayoutManager(layoutManager);

        LapakHargaAdapter viewAdapter = new LapakHargaAdapter(respData, getApplicationContext());
        rvView.setAdapter(viewAdapter);
    }
}