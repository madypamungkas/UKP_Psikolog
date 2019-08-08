package com.komsi.psikolog_interface.Api;

import com.komsi.psikolog_interface.Activities.VerifEmail.SendEmailVerif;
import com.komsi.psikolog_interface.Models.Default_Response;
import com.komsi.psikolog_interface.Models.JadwalResponse;
import com.komsi.psikolog_interface.Models.JsonDefault;
import com.komsi.psikolog_interface.Models.KonsultasiResponse;
import com.komsi.psikolog_interface.Models.LoginResponse;
import com.komsi.psikolog_interface.Models.Reponse_updateJadwal;
import com.komsi.psikolog_interface.Models.Response_ForgotPassword;
import com.komsi.psikolog_interface.Models.Response_GantiPassword;
import com.komsi.psikolog_interface.Models.Response_Layanan;
import com.komsi.psikolog_interface.Models.Response_LayananPsikolog;
import com.komsi.psikolog_interface.Models.Response_Psikolog;
import com.komsi.psikolog_interface.Models.Response_Details;
import com.komsi.psikolog_interface.Models.SendEmailVerif_Model;
import com.komsi.psikolog_interface.Models.ValidasiJadwal_Response;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @POST("register/psikolog")
    Call<JsonDefault> psikolog(@Field("name") String name,
                               @Field("email") String email,
                               @Field("password") String password,
                               //@Field("c_password") String c_password,
                               @Field("username") String username,
                               @Field("no_telepon") String no_telepon,
                               @Field("gelar") String gelar,
                               @Field("no_sipp") String no_sipp);

    @FormUrlEncoded
    @POST("login/psikolog")
    Call<LoginResponse> login(@Field("username") String username,
                              @Field("password") String password,
                              @Field("level") String level
    );


    @GET("details")
    Call<Response_Details> getDetails(@Header("Authorization") String token);

    @GET("psikolog/show/{id}")
    Call<Response_Psikolog> getPsikolog(@Header("Authorization") String token,
                                     @Path("id") int id);

    @FormUrlEncoded
    @PUT("ganti_password/{id}")
    Call<Response_GantiPassword> updatePass(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Field("password_lama") String password_lama,
            @Field("password_baru") String password_baru,
            @Field("konfirmasi") String konfirmasi
    );
    @FormUrlEncoded
    @PUT("psikolog/update_user/{id}")
    Call<JsonDefault> updateUser(
            @Header("Accept") String accept,
            @Header("Authorization") String token,
            @Path("id") int id,
            @Field("username") String username,
            @Field("email") String email,
            @Field("email_verified_at") String email_verified_at,
            @Field("no_telepon") String phone
    );


    @FormUrlEncoded
    @PUT("jadwal/updatePsikolog/{id}")
    Call<ResponseBody> updateJadwal(
            @Header("Authorization") String token,
            @Path("id") int idJadwal,
            @Field("psikolog_id") int psikolog_id,
            @Field("status_id") int status_id
    );

    @FormUrlEncoded
    @PUT("jadwal/updatePsikolog5/{id}")
    Call<ResponseBody> updateJadwal5(
            @Header("Authorization") String token,
            @Path("id") int idJadwal,
            @Field("psikolog_id") int psikolog_id,
            @Field("status_id") int status_id
    );

    @FormUrlEncoded
    @PUT("jadwal/updatePsikolog7/{id}")
    Call<Reponse_updateJadwal> updateJadwal7(
            @Header("Authorization") String token,
            @Path("id") int idJadwal,
            @Field("psikolog_id") int psikolog_id,
            @Field("klien_id") int klien_id,
            @Field("status_id") int status_id
    );

    @FormUrlEncoded
    @PUT("jadwal/updatePsikolog10/{id}")
    Call<Reponse_updateJadwal> updateJadwal10(
            @Header("Authorization") String token,
            @Path("id") int idJadwal,
            @Field("psikolog_id") int psikolog_id,
            @Field("klien_id") int klien_id,
            @Field("status_id") int status_id
    );

    @FormUrlEncoded
    @PUT("jadwal/updatePsikolog12/{id}")
    Call<Reponse_updateJadwal> updateJadwal12(
            @Header("Authorization") String token,
            @Path("id") int idJadwal,
            @Field("psikolog_id") int psikolog_id,
            @Field("klien_id") int klien_id,
            @Field("status_id") int status_id
    );
    @FormUrlEncoded
    @PUT("jadwal/updatePsikolog13/{id}")
    Call<ResponseBody> updateJadwal13(
            @Header("Authorization") String token,
            @Path("id") int idJadwal,
            @Field("psikolog_id") int psikolog_id,
            @Field("klien_id") int klien_id,
            @Field("status_id") int status_id
    );

    @GET("psikolog/jadwal")
    Call<JadwalResponse> getJadwal(@Header("Authorization") String token,
                                    @Query("psikolog_id") int psikolog_id);

    @GET("psikolog/riwayat")
    Call<JadwalResponse> getRiwayat(@Header("Authorization") String token,
                                    @Query("psikolog_id") int psikolog_id);

    @GET("psikolog/permintaan_klien")
    Call<JadwalResponse> getPermintaan(@Header("Authorization") String token,
                                        @Query("psikolog_id") int psikolog_id);

    @GET("jadwal/show/{id}")
    Call<KonsultasiResponse> getCariKlien(@Header("Authorization") String token,
                                              @Path("id") int idJadwal);
    @FormUrlEncoded
    @POST("forgot/password")
    Call<Response_ForgotPassword> forgotPassword(
            @Field("email") String email
    );
    @FormUrlEncoded
    @PUT("update_token/{id}")
    Call<ResponseBody> updateFCM(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Field("fcm_token") String fcm_token
    );
    @FormUrlEncoded
    @PUT("psikolog/update/{id}")
    Call<ResponseBody> uploadImg(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Field("foto") String foto
    );
    @FormUrlEncoded
    @PUT("updateStatusAktif/{id}")
    Call<ResponseBody> updateStatusAktif(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Field("isActive") String isActive
    );

    @GET("detail_psikolog/{id}")
    Call<Response_LayananPsikolog> getLayananPsikolog(@Header("Authorization") String token,
                                                      @Path("id") int idPsikolog);
    @GET("layanan")
    Call<Response_Layanan> getLayanan(@Header("Authorization") String token);

    @FormUrlEncoded
    @PUT("psikolog/update_biodata/{id}")
    Call<ResponseBody> updateBiodata(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Field("name") String nama,
            @Field("gelar") String gelar,
            @Field("nik") String nik,
            @Field("alamat") String alamat,
            @Field("no_sipp") String no_sipp,
            @Field("tanggal_lahir") String ttl,
            @Field("jenis_kelamin") String jk
    );
    @GET("email/resend")
    Call<SendEmailVerif_Model> getSendVerif(@Header("Authorization") String token);

    @GET("email/verify")
    Call<SendEmailVerif_Model> getStatusVerif(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("email/verify")
    Call<SendEmailVerif_Model> postCodeVerif(@Header("Authorization") String token,
                                     @Field("verification_code") String code
    );
    @FormUrlEncoded
    @POST("jadwal/getSesi")
    Call<ValidasiJadwal_Response> validasiJadwal(@Header("Authorization") String token,
                                                 @Field("tanggal") String tanggalJadwal,
                                                 @Field("keluhan") String keluhanKlien,
                                                 @Field("sesi_id") int sesi_id,
                                                 @Field("layanan_id") int layanan_id,
                                                 @Field("psikolog_id") int psikolog_id,
                                                 @Field("klien_id") int klien_id,
                                                 @Field("status_id") int status_id

    );

    @FormUrlEncoded
    @PUT("psikolog/update_layanan/{id}")
    Call<ResponseBody> updateLayanan(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Query("layanan_id[0]") String layanan,
            @Query("layanan_id[1]") String layanan1,
            @Query("layanan_id[2]") String layanan2,
            @Query("layanan_id[3]") String layanan3,
            @Query("layanan_id[4]") String layanan4,
            @Field("layanan") int layanan5
            );
}

