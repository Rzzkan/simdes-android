package com.simdes.appgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.simdes.appgo.adapter.BeritaAdapter;
import com.simdes.appgo.adapter.PengumumanAdapter;
import com.simdes.appgo.model.BeritaModel;
import com.simdes.appgo.model.PengumumanModel;
import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.GeneralHelper;
import com.simdes.appgo.utils.PopHelper;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class PengumumanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengumuman);
        ph = new PopHelper(this);
        gh = new GeneralHelper();
        _initView();
    }

    public PopHelper ph;
    public GeneralHelper gh;
    public ImageView btnBack;

    public void _initView(){
        btnBack = findViewById(R.id.btnBack);

        _actionView();
    }

    public void _actionView(){
        btnBack.setOnClickListener(view -> finish());
        updateView();
    }

    public void updateView(){
        ph.popLoading();
        reqPengumuman();
    }



    @SuppressLint("CheckResult")
    public void reqPengumuman(){
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqPengumuman()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if(respData.response){
                                initRVPengumuman(respData.data);
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

    public void initRVPengumuman(ArrayList<PengumumanModel> respData) {
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RecyclerView rvView = findViewById(R.id.rvPengumuman);
        rvView.setLayoutManager(layoutManager);

        PengumumanAdapter viewAdapter = new PengumumanAdapter(respData, getApplicationContext());
        rvView.setAdapter(viewAdapter);
    }
}