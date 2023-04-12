package com.simdes.appgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.github.barteksc.pdfviewer.PDFView;
import com.simdes.appgo.network.Config;
import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.DataHelper;
import com.simdes.appgo.utils.GeneralHelper;
import com.simdes.appgo.utils.PopHelper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class DetailApbDesaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_apb_desa);
        findViewById(R.id.btnBack).setOnClickListener(view -> finish());
        ph = new PopHelper(this);

        _initView();
    }

    public TextView txtJudul, txtTanggal;
    public PopHelper ph;
    public PDFView pdfView;
    public CardView btnUnduhApbDesa;

    public void _initView(){
        txtJudul = findViewById(R.id.txtJudul);
        txtTanggal = findViewById(R.id.txtTanggal);
        pdfView = findViewById(R.id.pdfView);
        btnUnduhApbDesa = findViewById(R.id.btnUnduhApbDesa);

        _actionView();
    }

    public void _actionView(){
        txtJudul.setText("" + DataHelper.data_apb_desa.judul);
        txtTanggal.setText("Oleh: " + DataHelper.data_apb_desa.user.name + " | " +
                GeneralHelper.formatDate(DataHelper.data_apb_desa.created_at));

        String berkas = Config.PATH_IMG + DataHelper.data_apb_desa.berkas.replace("//", "/");

        try {
            new RetrivePDFfromUrl().execute(berkas);
        }catch (Exception e){
            Toast.makeText(DetailApbDesaActivity.this, "Terjadi kesalahan \n" + e, Toast.LENGTH_SHORT).show();
        }

        btnUnduhApbDesa.setOnClickListener(view -> {
            try {
                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse("" + Config.PATH_IMG + DataHelper.data_apb_desa.berkas));
                httpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(httpIntent);
            }catch (Exception e){
                Toast.makeText(DetailApbDesaActivity.this, "Terjadi kesalahan \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .spacing(0)
                    .load();
        }
    }

}