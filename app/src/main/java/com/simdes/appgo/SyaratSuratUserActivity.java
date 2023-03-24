package com.simdes.appgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.simdes.appgo.adapter.ApbDesaAdapter;
import com.simdes.appgo.adapter.SyaratPengajuanSuratAdapter;
import com.simdes.appgo.model.ApbDesaModel;
import com.simdes.appgo.model.SyaratPengajuanSuratModel;
import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.DataHelper;
import com.simdes.appgo.utils.GeneralHelper;
import com.simdes.appgo.utils.PopHelper;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SyaratSuratUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syarat_surat_user);
        ph = new PopHelper(this);
        gh = new GeneralHelper();
        _initView();
    }

    public PopHelper ph;
    public GeneralHelper gh;
    public ImageView btnBack;
    public RecyclerView rvApbDesa;
    public TextView txtNamaSurat;
    public CardView btnAjukanSurat;

    public void _initView() {
        rvApbDesa = findViewById(R.id.rvApbDesa);
        btnBack = findViewById(R.id.btnBack);
        txtNamaSurat = findViewById(R.id.txtNamaSurat);
        btnAjukanSurat = findViewById(R.id.btnAjukanSurat);

        txtNamaSurat.setText(getIntent().getStringExtra("nama_surat"));

        _actionView();
    }

    public void _actionView() {
        btnBack.setOnClickListener(view -> finish());
        btnAjukanSurat.setOnClickListener(view -> finish());
        updateView();
    }

    public void updateView() {
        ph.popLoading();
        reqSyaratPengajuanSurat();
    }


    @SuppressLint("CheckResult")
    public void reqSyaratPengajuanSurat() {
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqSyaratPengajuanSurat(
                        Integer.parseInt(DataHelper.data_pengajuan_surat.id)
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if (respData.response) {
                                initRVSyaratPengajuanSurat(respData.data);
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

    public void initRVSyaratPengajuanSurat(ArrayList<SyaratPengajuanSuratModel> respData) {
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RecyclerView rvView = findViewById(R.id.rvSyaratPengajuanSurat);
        rvView.setLayoutManager(layoutManager);

        SyaratPengajuanSuratAdapter viewAdapter = new SyaratPengajuanSuratAdapter(respData, getApplicationContext());
        rvView.setAdapter(viewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _actionView();
    }
}