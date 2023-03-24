package com.simdes.appgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.simdes.appgo.adapter.BeritaAdapter;
import com.simdes.appgo.adapter.LapakHargaAdapter;
import com.simdes.appgo.model.BeritaModel;
import com.simdes.appgo.model.LapakDesaModel;
import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.GeneralHelper;
import com.simdes.appgo.utils.PopHelper;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class BeritaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita);
        ph = new PopHelper(this);
        gh = new GeneralHelper();
        _initView();
    }

    public PopHelper ph;
    public GeneralHelper gh;
    public ImageView btnBack;
    public RecyclerView rvBerita;

    public void _initView(){
        rvBerita = findViewById(R.id.rvBerita);
        btnBack = findViewById(R.id.btnBack);

        _actionView();
    }

    public void _actionView(){
        btnBack.setOnClickListener(view -> finish());
        updateView();
    }

    public void updateView(){
        ph.popLoading();
        reqBerita();
    }



    @SuppressLint("CheckResult")
    public void reqBerita(){
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqBerita()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if(respData.response){
                                initRVBerita(respData.data);
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

    public void initRVBerita(ArrayList<BeritaModel> respData) {
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RecyclerView rvView = findViewById(R.id.rvBerita);
        rvView.setLayoutManager(layoutManager);

        BeritaAdapter viewAdapter = new BeritaAdapter(respData, getApplicationContext());
        rvView.setAdapter(viewAdapter);
    }
}