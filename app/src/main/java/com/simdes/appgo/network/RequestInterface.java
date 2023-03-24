package com.simdes.appgo.network;

import com.simdes.appgo.model.ApbDesaModel;
import com.simdes.appgo.model.BeritaModel;
import com.simdes.appgo.model.DataListModel;
import com.simdes.appgo.model.DataModel;
import com.simdes.appgo.model.ForgotPasswordModel;
import com.simdes.appgo.model.JenisSuratModel;
import com.simdes.appgo.model.KonfigurasiModel;
import com.simdes.appgo.model.LapakDesaModel;
import com.simdes.appgo.model.LoginModel;
import com.simdes.appgo.model.PengaduanModel;
import com.simdes.appgo.model.PengajuanSuratModel;
import com.simdes.appgo.model.PengumumanModel;
import com.simdes.appgo.model.PetaDesaModel;
import com.simdes.appgo.model.SyaratPengajuanSuratModel;
import com.simdes.appgo.model.UserModel;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RequestInterface {

    @Multipart
    @POST("pengajuan_surat/create_syarat_surat")
    Observable<DataModel<SyaratPengajuanSuratModel>> reqUploadSyaratPengajuanSurat(
            @Part("id_syarat_user") RequestBody id_syarat_user,
            @Part MultipartBody.Part berkas);

    @FormUrlEncoded
    @POST("pengajuan_surat/syarat_surat")
    Observable<DataListModel<SyaratPengajuanSuratModel>> reqSyaratPengajuanSurat(
            @Field("id_pengajuan_surat") int id_pengajuan_surat
    );

    @FormUrlEncoded
    @POST("pengajuan_surat/create_pengajuan_surat")
    Observable<DataModel<PengajuanSuratModel>> reqCreatePengajuanSurat(
            @Field("id_jenis_surat") int id_jenis_surat
    );

    @GET("pengajuan_surat/jenis_surat")
    Observable<DataListModel<JenisSuratModel>> reqJenisSurat();

    @GET("pengajuan_surat")
    Observable<DataListModel<PengajuanSuratModel>> reqPengajuanSurat();

    @Multipart
    @POST("pengaduan")
    Observable<DataModel<PengaduanModel>> reqTambahPengaduan(
            @Part("deskripsi") RequestBody deskripsi,
            @Part MultipartBody.Part gambar);

    @GET("pengaduan")
    Observable<DataListModel<PengaduanModel>> reqPengaduan();

    @GET("apb_desa")
    Observable<DataListModel<ApbDesaModel>> reqApbDesa();

    @GET("peta_desa")
    Observable<DataModel<PetaDesaModel>> reqPetaDesa();

    @GET("lapak_desa")
    Observable<DataListModel<LapakDesaModel>> reqLapakDesa();

    @GET("berita")
    Observable<DataListModel<BeritaModel>> reqBerita();

    @GET("banner")
    Observable<DataListModel<BeritaModel>> reqBeritaTop();

    @GET("pengumuman")
    Observable<DataListModel<PengumumanModel>> reqPengumuman();

    @GET("top_pengumuman")
    Observable<DataListModel<PengumumanModel>> reqPengumumanTop();

    @GET("konfigurasi")
    Observable<DataModel<KonfigurasiModel>> reqKonfigurasi();

    @FormUrlEncoded
    @POST("login")
    Observable<DataModel<LoginModel>> reqLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register")
    Observable<DataModel<LoginModel>> reqRegister(
            @Field("email") String email,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("passconf") String passconf
    );

    @FormUrlEncoded
    @POST("req_code_reset_password")
    Observable<DataModel<ForgotPasswordModel>> reqForgot(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("reset_password")
    Observable<DataModel<LoginModel>> reqResetPassword(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("update_profile")
    Observable<DataModel<LoginModel>> reqUpdateProfile(
            @Field("email") String email,
            @Field("name") String name,
            @Field("no_hp") String no_hp,
            @Field("phone") String phone);

    @Multipart
    @POST("update_profil")
    Observable<DataModel<UserModel>> reqUpdateProfil(
            @Part("email") RequestBody email,
            @Part("name") RequestBody name,
            @Part("no_hp") RequestBody no_hp,
            @Part("password") RequestBody password,
            @Part("password_confirmation") RequestBody password_confirmation,
            @Part MultipartBody.Part avatar);

    @Multipart
    @POST("update_id_card")
    Observable<DataModel<LoginModel>> reqUpdateIdCard(
            @Part("codeApp") RequestBody codeApp,
            @Part("id") RequestBody id,
            @Part("token") RequestBody token,
            @Part MultipartBody.Part id_card);
}
