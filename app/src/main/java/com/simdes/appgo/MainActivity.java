package com.simdes.appgo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.simdes.appgo.adapter.BeritaAdapter;
import com.simdes.appgo.adapter.BeritaTopAdapter;
import com.simdes.appgo.adapter.PengumumanAdapter;
import com.simdes.appgo.adapter.PengumumanTopAdapter;
import com.simdes.appgo.model.BeritaModel;
import com.simdes.appgo.model.PengajuanSuratModel;
import com.simdes.appgo.model.PengumumanModel;
import com.simdes.appgo.network.Config;
import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.DataHelper;
import com.simdes.appgo.utils.FlagHelper;
import com.simdes.appgo.utils.GeneralHelper;
import com.simdes.appgo.utils.PopHelper;
import com.simdes.appgo.utils.UserHelper;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    public PopHelper ph;
    public GeneralHelper gh;
    public ImageView imgProfile;
    public TextView txtNama, btnLihatBerita, btnLihatPengumuman;
    public LinearLayout btnBottomHome, btnBottomProfile,
            btnLapakDesa, btnPetaDesa, btnApbDesa, btnPengaduan, btnLayananDesa;
    public CardView btnKontak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ph = new PopHelper(this);
        gh = new GeneralHelper();
        _initView();
    }

    public void _initView(){
        btnBottomHome = findViewById(R.id.btnBottomHome);
        btnBottomProfile = findViewById(R.id.btnBottomProfile);
        btnLapakDesa = findViewById(R.id.btnLapakDesa);
        btnPetaDesa = findViewById(R.id.btnPetaDesa);
        btnApbDesa = findViewById(R.id.btnApbDesa);
        btnPengaduan = findViewById(R.id.btnPengaduan);
        btnLayananDesa = findViewById(R.id.btnLayananDesa);
        imgProfile = findViewById(R.id.imgProfile);
        txtNama = findViewById(R.id.txtNama);
        btnLihatBerita = findViewById(R.id.btnLihatBerita);
        btnLihatPengumuman = findViewById(R.id.btnLihatPengumuman);
        btnKontak = findViewById(R.id.btnKontak);

        _actionView();
    }

    public void _actionView(){

        Glide.with(this)
                .load(Config.PATH_IMG + UserHelper.user.user.avatar)
                .apply(new RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24).error(R.drawable.ic_baseline_account_circle_24).circleCrop())
                .apply(new RequestOptions().circleCrop())
                .into(imgProfile);

        txtNama.setText("Hai, " + UserHelper.user.user.name);

        btnBottomProfile.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, ProfileActivity.class)));
        btnLapakDesa.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, LapakDesaActivity.class)));
        btnPetaDesa.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, PetaDesaActivity.class)));
        btnApbDesa.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, ApbDesaActivity.class)));
        btnPengaduan.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, PengaduanActivity.class)));
        btnLayananDesa.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, PengajuanSuratActivity.class)));

        btnLihatBerita.setOnClickListener(view -> startActivity(
                new Intent(MainActivity.this, BeritaActivity.class)));
        btnLihatPengumuman.setOnClickListener(view -> startActivity(
                new Intent(MainActivity.this, PengumumanActivity.class)));

        btnKontak.setOnClickListener(view -> sendWa());

        updateView();

    }

    public void sendWa(){
        String phoneNumber = "" + DataHelper.data_konfigurasi.kontak;
        String message = "Saya ingin bertanya. \n\n";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + message));
        startActivity(intent);
    }

    public void updateView(){
        ph.popLoading();
        reqBeritaTop();
        reqPengumumanTop();
        reqKonfigurasi();
    }

    @SuppressLint("CheckResult")
    public void reqKonfigurasi(){
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqKonfigurasi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if(respData.response){
                                DataHelper.data_konfigurasi = respData.data;
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

    @SuppressLint("CheckResult")
    public void reqBeritaTop(){
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqBeritaTop()
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
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        RecyclerView rvView = findViewById(R.id.rvBerita);
        rvView.setLayoutManager(layoutManager);

        BeritaTopAdapter viewAdapter = new BeritaTopAdapter(respData, getApplicationContext());
        rvView.setAdapter(viewAdapter);
    }

    @SuppressLint("CheckResult")
    public void reqPengumumanTop(){
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqPengumumanTop()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if(respData.response){
                                initRVPrngumuman(respData.data);
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

    public void initRVPrngumuman(ArrayList<PengumumanModel> respData) {
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        RecyclerView rvView = findViewById(R.id.rvPengumuman);
        rvView.setLayoutManager(layoutManager);

        PengumumanTopAdapter viewAdapter = new PengumumanTopAdapter(respData, getApplicationContext());
        rvView.setAdapter(viewAdapter);
    }

    @Override
    public void onResume(){
        _initView();
        super.onResume();
    }
}