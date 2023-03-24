package com.simdes.appgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.simdes.appgo.network.Config;
import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.DataHelper;
import com.simdes.appgo.utils.FileUtils;
import com.simdes.appgo.utils.PopHelper;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class UploadSyaratPengajuanSuratActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_syarat_pengajuan_surat);
        ph = new PopHelper(this);
        _initView();
    }

    public PopHelper ph;
    public ImageView imgFoto;
    public TextView txtSyarat;
    public CardView btnKirim;

    public void _initView(){
        imgFoto = findViewById(R.id.imgFoto);
        txtSyarat = findViewById(R.id.txtSyarat);
        btnKirim = findViewById(R.id.btnKirim);

        txtSyarat.setText(DataHelper.data_syarat_surat_user.syarat.nama);

        Glide.with(this)
                .load(Config.PATH_IMG + DataHelper.data_syarat_surat_user.berkas)
                .apply(new RequestOptions().placeholder(R.drawable.no_image_available).error(R.drawable.no_image_available))
                .into(imgFoto);

        displayDetail(DataHelper.data_syarat_surat_user.syarat.keterangan);

        _actionView();
    }

    public void _actionView(){
        findViewById(R.id.btnBack).setOnClickListener(view -> finish());
        imgFoto.setOnClickListener(view -> choosePhoto());
        btnKirim.setOnClickListener(view -> reqTambahPengaduan());
    }

    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private Uri uriFoto, uri;

    private void choosePhoto() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);

        }else{
            openGallery();
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if(data != null) {

                uriFoto = data.getData();
                uri = uriFoto;
                Glide.with(this)
                        .load(uriFoto)
                        .apply(new RequestOptions().placeholder(R.color.purple).error(R.color.black))
                        .into(imgFoto);

                try{
                    File file = FileUtils.getFile(UploadSyaratPengajuanSuratActivity.this, uri);
                }catch (Exception e){
                    Toast.makeText(UploadSyaratPengajuanSuratActivity.this, "File not found!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                }
                return;
            }
        }
    }

    private void reqTambahPengaduan() {
        ph.popLoading();
        File file = FileUtils.getFile(UploadSyaratPengajuanSuratActivity.this, uriFoto);

        RequestBody photoBody = null;
        MultipartBody.Part photoPart = null;

        if(file != null){
            photoBody = RequestBody.create(MediaType.parse("*/*"), file);
            photoPart = MultipartBody.Part.createFormData("berkas",
                    file.getName(), photoBody);
        }

        RequestBody id_syarat_user = RequestBody.create(MediaType.parse("text/plain"),
                DataHelper.data_syarat_surat_user.id);

        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqUploadSyaratPengajuanSurat(
                        id_syarat_user,
                        photoPart
                ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData  -> {
                            uriFoto = null;
                            Toast.makeText(getApplicationContext(), respData.message, Toast.LENGTH_SHORT).show();
                            ph.popDismiss();
                            finish();
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            throwable.printStackTrace();
                            ph.popDismiss();
                        }
                );
    }


    @SuppressLint("ClickableViewAccessibility")
    private void displayDetail(String detail) {
        String html_data = "<style>img{max-width:100%;height:auto;} iframe{width:100%;}</style> ";
        html_data += detail;
        WebView webDetail = findViewById(R.id.webDetail);
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