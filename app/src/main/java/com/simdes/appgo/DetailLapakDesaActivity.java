package com.simdes.appgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.simdes.appgo.network.Config;
import com.simdes.appgo.utils.DataHelper;
import com.simdes.appgo.utils.GeneralHelper;
import com.simdes.appgo.utils.PopHelper;

public class DetailLapakDesaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lapak_desa);
        findViewById(R.id.btnBack).setOnClickListener(view -> finish());
        ph = new PopHelper(this);

        _initView();
    }

    public PopHelper ph;

    public TextView txtJudul, txtTanggal;
    public ImageView imgGambar;
    public CardView btnHubungiPenjual;
    public WebView webDetail;

    public void _initView(){
        txtJudul = findViewById(R.id.txtJudul);
        txtTanggal = findViewById(R.id.txtTanggal);
        btnHubungiPenjual = findViewById(R.id.btnHubungiPenjual);
        imgGambar = findViewById(R.id.imgGambar);
        webDetail = findViewById(R.id.webDetail);

        _actionView();
    }

    public void _actionView(){
        txtJudul.setText("" + DataHelper.data_lapak_desa.judul + "\n"
                + GeneralHelper.formatBalance(DataHelper.data_lapak_desa.harga));
        txtTanggal.setText("Oleh: " + DataHelper.data_lapak_desa.user.name + " | "
                + GeneralHelper.formatDate(DataHelper.data_lapak_desa.created_at));
        displayDetail(DataHelper.data_lapak_desa.deskripsi);
        if(!DataHelper.data_lapak_desa.gambar.equals("") && DataHelper.data_lapak_desa.gambar != null){
            Glide.with(this)
                    .load(Config.PATH_IMG + DataHelper.data_lapak_desa.gambar)
                    .apply(new RequestOptions().placeholder(R.drawable.logo_simdes).error(R.drawable.logo_simdes))
                    .into(imgGambar);
        }else {
            imgGambar.setVisibility(View.GONE);
        }
        btnHubungiPenjual.setOnClickListener(view -> sendWa());
    }

    public void sendWa(){
        String phoneNumber = "" + DataHelper.data_lapak_desa.user.no_hp;
        String message = "Saya ingin membeli produk ini, \n" +
                DataHelper.data_lapak_desa.judul + "\n" +
                GeneralHelper.formatBalance(DataHelper.data_lapak_desa.harga) + "\n" +
                Config.PATH_IMG + DataHelper.data_lapak_desa.gambar + "\n\n";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + message));
        startActivity(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void displayDetail(String detail) {
        String html_data = "<style>img{max-width:100%;height:auto;} iframe{width:100%;}</style> ";
        html_data += detail;
        webDetail.getSettings().setJavaScriptEnabled(true);
        webDetail.getSettings();
        webDetail.getSettings().setBuiltInZoomControls(true);
        webDetail.setBackgroundColor(Color.TRANSPARENT);
        webDetail.setWebChromeClient(new WebChromeClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            webDetail.loadDataWithBaseURL(null, html_data, "text/html; charset=UTF-8", "utf-8", null);
        } else {
            webDetail.loadData(html_data, "text/html; charset=UTF-8", null);
        }
        // disable scroll on touch
        webDetail.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        // override url direct
        webDetail.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("" + url));
                startActivity(browserIntent);
                return true;
            }
        });
    }
}