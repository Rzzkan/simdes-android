package com.simdes.appgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.simdes.appgo.adapter.ApbDesaAdapter;
import com.simdes.appgo.adapter.PengaduanAdapter;
import com.simdes.appgo.model.ApbDesaModel;
import com.simdes.appgo.model.PengaduanModel;
import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.GeneralHelper;
import com.simdes.appgo.utils.PopHelper;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class PengaduanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaduan);
        ph = new PopHelper(this);
        gh = new GeneralHelper();
        _initView();
    }

    public PopHelper ph;
    public GeneralHelper gh;
    public ImageView btnBack;
    public CardView btnTambahPengaduan;

    public void _initView() {
        btnBack = findViewById(R.id.btnBack);
        btnTambahPengaduan = findViewById(R.id.btnTambahPengaduan);

        _actionView();
    }

    public void _actionView() {
        btnBack.setOnClickListener(view -> finish());
        btnTambahPengaduan.setOnClickListener(view ->
                startActivity(new Intent(PengaduanActivity.this, TambahPengaduanActivity.class)));
        updateView();
    }

    public void updateView() {
        ph.popLoading();
        reqPengaduan();
    }


    @SuppressLint("CheckResult")
    public void reqPengaduan() {
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqPengaduan()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if (respData.response) {
                                initRVPengaduan(respData.data);
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

    public void initRVPengaduan(ArrayList<PengaduanModel> respData) {
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RecyclerView rvView = findViewById(R.id.rvPengaduan);
        rvView.setLayoutManager(layoutManager);

        PengaduanAdapter viewAdapter = new PengaduanAdapter(respData, getApplicationContext());
        rvView.setAdapter(viewAdapter);
    }

    @Override
    protected void onResume() {
        updateView();
        super.onResume();
    }
}