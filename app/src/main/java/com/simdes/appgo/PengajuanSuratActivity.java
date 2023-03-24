package com.simdes.appgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.simdes.appgo.adapter.JenisSuratAdapter;
import com.simdes.appgo.adapter.PengaduanAdapter;
import com.simdes.appgo.adapter.PengajuanSuratAdapter;
import com.simdes.appgo.model.JenisSuratModel;
import com.simdes.appgo.model.PengaduanModel;
import com.simdes.appgo.model.PengajuanSuratModel;
import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.DataHelper;
import com.simdes.appgo.utils.GeneralHelper;
import com.simdes.appgo.utils.PopHelper;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class PengajuanSuratActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuan_surat);
        ph = new PopHelper(this);
        gh = new GeneralHelper();
        _initView();
    }

    public PopHelper ph;
    public GeneralHelper gh;
    public ImageView btnBack;
    public CardView btnTambahPengajuanSurat;

    public void _initView() {
        btnBack = findViewById(R.id.btnBack);
        btnTambahPengajuanSurat = findViewById(R.id.btnTambahPengajuanSurat);

        _actionView();
    }

    public void _actionView() {
        btnBack.setOnClickListener(view -> finish());
        btnTambahPengajuanSurat.setOnClickListener(view ->
                popJenisSurat());
        updateView();
    }

    public void updateView() {
        ph.popLoading();
        reqPengajuanSurat();
    }

    public Dialog dialog;
    public RecyclerView rvJenisSurat;
    public TextView btnPilih;

    public void popJenisSurat(){
        dialog = new Dialog(PengajuanSuratActivity.this);
        dialog.setContentView(R.layout.pop_pilih_jenis_pengajuan_surat);

        rvJenisSurat = dialog.findViewById(R.id.rvJenisSurat);
        btnPilih = dialog.findViewById(R.id.btnPilih);
        reqJenisSurat();

        btnPilih.setOnClickListener(view -> {
                reqCreatePengajuanSurat();
                DataHelper.id_jenis_surat = 0;
                dialog.dismiss();
                ph.popLoading();
        });

        dialog.show();
    }

    @SuppressLint("CheckResult")
    public void reqCreatePengajuanSurat() {
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqCreatePengajuanSurat(
                        DataHelper.id_jenis_surat
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if (respData.response) {
                                reqPengajuanSurat();
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

    @SuppressLint("CheckResult")
    public void reqJenisSurat() {
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqJenisSurat()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if (respData.response) {
                                initRVJenisSurat(respData.data);
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

    public void initRVJenisSurat(ArrayList<JenisSuratModel> respData) {
        GridLayoutManager layoutManager = new GridLayoutManager(PengajuanSuratActivity.this, 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvJenisSurat.setLayoutManager(layoutManager);
        JenisSuratAdapter viewAdapter = new JenisSuratAdapter(respData, PengajuanSuratActivity.this);
        rvJenisSurat.setAdapter(viewAdapter);
    }

    @SuppressLint("CheckResult")
    public void reqPengajuanSurat() {
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqPengajuanSurat()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if (respData.response) {
                                initRVPengajuanSurat(respData.data);
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

    public void initRVPengajuanSurat(ArrayList<PengajuanSuratModel> respData) {
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RecyclerView rvView = findViewById(R.id.rvPengajuanSurat);
        rvView.setLayoutManager(layoutManager);

        PengajuanSuratAdapter viewAdapter = new PengajuanSuratAdapter(respData, getApplicationContext());
        rvView.setAdapter(viewAdapter);
    }
}