/*
 * Create by Brian Dwi Murdianto
 * Email : dwibrian120@gmail.com
 */

package com.simdes.appgo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.simdes.appgo.network.Config;
import com.simdes.appgo.network.RequestInterface;
import com.simdes.appgo.network.RetrofitClient;
import com.simdes.appgo.utils.FileUtils;
import com.simdes.appgo.utils.GeneralHelper;
import com.simdes.appgo.utils.PopHelper;
import com.simdes.appgo.utils.PreferencesHelper;
import com.simdes.appgo.utils.UserHelper;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.http.Part;

public class ProfileActivity extends AppCompatActivity {

    public PopHelper ph;
    public ImageView myImage, imgProfile, imgIdCard;
    public EditText inpName, inpEmail, inpPhone, inpPassword, inpUlangiPassword;
    public TextView btnUpdate, btnSaveIdCard, btnUpdatePassword, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViewById(R.id.btnBack).setOnClickListener(view -> finish());
        ph = new PopHelper(this);
        _initView();
    }

    public void _initView(){
        myImage = findViewById(R.id.imgQR);
        inpName = findViewById(R.id.inpName);
        inpEmail = findViewById(R.id.inpEmail);
        inpPhone = findViewById(R.id.inpPhone);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnLogout = findViewById(R.id.btnLogout);
        inpPassword = findViewById(R.id.inpPassword);
        inpUlangiPassword = findViewById(R.id.inpUlangiPassword);

        btnSaveIdCard = findViewById(R.id.btnSaveIdCard);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        imgProfile = findViewById(R.id.imgProfile);
        imgIdCard = findViewById(R.id.imgIdCard);

        _actionView();
    }

    public void _actionView(){

        inpName.setText(UserHelper.user.user.name);
        inpEmail.setText(UserHelper.user.user.email);
        inpPhone.setText(UserHelper.user.user.no_hp);

        Glide.with(this)
                .load(Config.PATH_IMG + UserHelper.user.user.avatar)
                .apply(new RequestOptions().circleCrop())
                .apply(new RequestOptions().placeholder(R.color.white).error(R.color.white))
                .into(imgProfile);

        imgProfile.setOnClickListener(view -> {
            newImgParam = imgProfile;
            choosePhoto();
        });

        imgIdCard.setOnClickListener(view -> {
            newImgParam = imgIdCard;
            choosePhoto();
        });

        btnUpdate.setOnClickListener(view -> {
            reqUpdateProfil();
        });

        btnSaveIdCard.setOnClickListener(view -> {
            if(uriIdCard == null){
                GeneralHelper.ToastNew(this, "Choose Image first!");
            }else {
                reqUpdateIdCard();
            }
        });

        btnUpdatePassword.setOnClickListener(view -> {
            new GeneralHelper().intentAct(this, NewPasswordActivity.class);
        });

        btnLogout.setOnClickListener(view -> {
            PreferencesHelper.clearLoggedInUser(ProfileActivity.this);
            UserHelper.user = null;
            UserHelper.pass = "";
            UserHelper.api_token = "";
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private Uri uriProfil, uriIdCard, uri;
    public ImageView newImgParam;

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

                if(newImgParam == imgProfile){
                    uriProfil = data.getData();
                    uri = uriProfil;
                    Glide.with(this)
                            .load(uriProfil)
                            .apply(new RequestOptions().circleCrop())
                            .apply(new RequestOptions().placeholder(R.color.purple).error(R.color.black))
                            .into(imgProfile);
                }else {
                    uriIdCard = data.getData();
                    uri = uriIdCard;
                    Glide.with(this)
                            .load(uriIdCard)
                            .apply(new RequestOptions().placeholder(R.color.purple).error(R.color.black))
                            .into(imgIdCard);
                }

                try{
                    File file = FileUtils.getFile(com.simdes.appgo.ProfileActivity.this, uri);
                }catch (Exception e){
                    uri = null;
                    if(newImgParam == imgProfile){
                        uriProfil = null;
                    }else {
                        uriIdCard = null;
                    }
                    Toast.makeText(com.simdes.appgo.ProfileActivity.this, "File not found!", Toast.LENGTH_SHORT).show();
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

    private void reqUpdateProfil() {
        ph.popLoading();
        File file = FileUtils.getFile(com.simdes.appgo.ProfileActivity.this, uriProfil);

        RequestBody photoBody = null;
        MultipartBody.Part photoPart = null;

        if(file != null){
            photoBody = RequestBody.create(MediaType.parse("*/*"), file);
            photoPart = MultipartBody.Part.createFormData("avatar",
                    file.getName(), photoBody);
        }

        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), inpEmail.getText().toString().trim());
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), inpName.getText().toString().trim());
        RequestBody no_hp = RequestBody.create(MediaType.parse("text/plain"), inpPhone.getText().toString().trim());
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), inpPassword.getText().toString().trim());
        RequestBody password_confirmation = RequestBody.create(MediaType.parse("text/plain"), inpUlangiPassword.getText().toString().trim());

        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqUpdateProfil(
                email,
                name,
                no_hp,
                password,
                password_confirmation,
                photoPart
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData  -> {
                            UserHelper.user.user = respData.data;
                            uriProfil = null;
                            Toast.makeText(getApplicationContext(), respData.message, Toast.LENGTH_SHORT).show();
                            ph.popDismiss();
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            throwable.printStackTrace();
                            ph.popDismiss();
                        }
                );
    }


    private void reqUpdateIdCard() {
        ph.popLoading();
        File file = FileUtils.getFile(com.simdes.appgo.ProfileActivity.this, uriIdCard);

        RequestBody photoBody = null;
        MultipartBody.Part photoPart = null;

        if(file != null){
            photoBody = RequestBody.create(MediaType.parse("*/*"), file);
            photoPart = MultipartBody.Part.createFormData("id_card",
                    file.getName(), photoBody);
        }

        RequestBody codeApp = RequestBody.create(MediaType.parse("text/plain"), Config.CODE_APP);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), UserHelper.user.user.id);
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), UserHelper.user.user.api_token);

        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqUpdateIdCard(
                codeApp,
                id,
                token,
                photoPart
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData  -> {
                            UserHelper.user = respData.data;
                            uriIdCard = null;
                            Toast.makeText(getApplicationContext(), respData.message, Toast.LENGTH_SHORT).show();
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
    public void reqUpdateProfile(){
        ph.popLoading();
        Retrofit retrofit = RetrofitClient.getClient(this);
        RequestInterface request = retrofit.create(RequestInterface.class);
        request.reqUpdateProfile(
                inpName.getText().toString().trim() + "",
                UserHelper.user.user.api_token + "",
                UserHelper.user.user.id + "",
                inpPhone.getText().toString().trim() + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        respData -> {
                            if(respData.response){
                                UserHelper.user = respData.data;
                                Toast.makeText(getApplicationContext(), respData.message, Toast.LENGTH_SHORT).show();
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
}