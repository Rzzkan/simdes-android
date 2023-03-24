package com.simdes.appgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.simdes.appgo.network.Config;
import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.FileUtils;
import com.simdes.appgo.utils.PopHelper;
import com.simdes.appgo.utils.UserHelper;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class TambahPengaduanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pengaduan);
        ph = new PopHelper(this);
        _initView();
    }

    public PopHelper ph;
    public ImageView imgFoto;
    public EditText inpLokasi, inpDeskripsi;
    public CardView btnKirim;

    public void _initView(){
        inpLokasi = findViewById(R.id.inpLokasi);
        inpDeskripsi = findViewById(R.id.inpDeskripsi);
        imgFoto = findViewById(R.id.imgFoto);
        btnKirim = findViewById(R.id.btnKirim);

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
                    File file = FileUtils.getFile(TambahPengaduanActivity.this, uri);
                }catch (Exception e){
                    Toast.makeText(TambahPengaduanActivity.this, "File not found!", Toast.LENGTH_SHORT).show();
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
        File file = FileUtils.getFile(TambahPengaduanActivity.this, uriFoto);

        RequestBody photoBody = null;
        MultipartBody.Part photoPart = null;

        if(file != null){
            photoBody = RequestBody.create(MediaType.parse("*/*"), file);
            photoPart = MultipartBody.Part.createFormData("gambar",
                    file.getName(), photoBody);
        }

        RequestBody deskripsi = RequestBody.create(MediaType.parse("text/plain"),
                inpLokasi.getText().toString().trim() + "\n" +
                        inpDeskripsi.getText().toString().trim());

        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqTambahPengaduan(
                        deskripsi,
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
}