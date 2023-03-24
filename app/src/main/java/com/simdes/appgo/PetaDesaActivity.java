package com.simdes.appgo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.GeneralHelper;
import com.simdes.appgo.utils.PopHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class PetaDesaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_desa);
        ph = new PopHelper(this);
        gh = new GeneralHelper();
        _initView();
    }

    public PopHelper ph;
    public GeneralHelper gh;
    public ImageView btnBack;
    public WebView webMaps;

    public void _initView(){
        btnBack = findViewById(R.id.btnBack);
        webMaps = findViewById(R.id.webMaps);

        _actionView();
    }

    public void _actionView(){
        btnBack.setOnClickListener(view -> finish());
        updateView();
    }

    public void updateView(){
        ph.popLoading();
        reqPetaDesa();
    }



    @SuppressLint("CheckResult")
    public void reqPetaDesa(){
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqPetaDesa()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if(respData.response){
                                viewMaps(respData.data.url_maps);
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

    public void viewMaps(String url){
        WebView webMaps = findViewById(R.id.webMaps);
        webMaps.getSettings().setJavaScriptEnabled(true);
        webMaps.loadUrl(url);
    }
}