package com.komsi.psikolog_interface.Activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.Menu.UbahBiodata;
import com.komsi.psikolog_interface.Activities.VerifEmail.SendEmailVerif;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.Models.Response_Details;
import com.komsi.psikolog_interface.Models.Response_Psikolog;
import com.komsi.psikolog_interface.Models.SendEmailVerif_Model;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Loading extends AppCompatActivity {
    LinearLayout loading, refresh;
    NotificationCompat.Builder notification;
    public static final int uniqeID = 458714;
    Dialog myDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

    }

    User user = SharedPrefManager.getInstance(this).getUser();
    private String token = "Bearer " + user.getToken();

    @Override
    protected void onStart() {
        super.onStart();

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        Details details = SharedPrefManager.getInstance(this).getDetails();

        if (details.getUsername() == null) {
            loadDetails();
        } else {
            loading = (LinearLayout) findViewById(R.id.loadingLayout);
            refresh = (LinearLayout) findViewById(R.id.refreshLoading);
            loading.setVisibility(View.GONE);
            refresh.setVisibility(View.VISIBLE);
        }
         // checkVerif();
        if (details.getNik() == null) {
            myDialog = new Dialog(this);
            myDialog.setContentView(R.layout.popup_isi_biodata);
            Button isiBiodata = (Button) myDialog.findViewById(R.id.isiBiodata);
            isiBiodata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent isiBiodata = new Intent(Loading.this, UbahBiodata.class);
                    myDialog.dismiss();
                    startActivity(isiBiodata);
                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        }

    }

    public void loadDetails() {
        retrofit2.Call<Response_Details> call = RetrofitClient
                .getInstance()
                .getApi()
                .getDetails(token);
        call.enqueue(new retrofit2.Callback<Response_Details>() {
            @Override
            public void onResponse(retrofit2.Call<Response_Details> call, retrofit2.Response<Response_Details> response) {
                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(Loading.this).saveDetails(response.body().getDetails());
                    loadPsikolog();

                } else {
                    Toast.makeText(Loading.this, "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Response_Details> call, Throwable t) {
                Toast.makeText(Loading.this, "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loadPsikolog() {
        final Details details = SharedPrefManager.getInstance(Loading.this).getDetails();
        final int idUser = details.getUserId();

        Call<Response_Psikolog> callKlien = RetrofitClient.getInstance().getApi().getPsikolog(token, idUser);
        callKlien.enqueue(new Callback<Response_Psikolog>() {
            @Override
            public void onResponse(Call<Response_Psikolog> call, Response<Response_Psikolog> response) {
                SharedPrefManager.getInstance(Loading.this).savePsikolog(response.body().getPsikolog());
                SharedPreferences sharedPref = getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("idPsikolog", response.body().getPsikolog().getId());
                editor.putString("gelar", response.body().getPsikolog().getGelar());
                editor.putString("no_sipp", response.body().getPsikolog().getNo_sipp());
                //editor.putInt("keahlian_id", response.body().getPsikolog().getKeahlian_id());
                editor.putInt("user_id", response.body().getPsikolog().getUser_id());
                editor.apply();

                SharedPreferences sharedPrefFCM = getSharedPreferences("TokenFCM", Context.MODE_PRIVATE);
                String TokenFCM = sharedPrefFCM.getString("token", null);

                Call<ResponseBody> putoken = RetrofitClient.getInstance().getApi().updateFCM(token, idUser, TokenFCM);
                putoken.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (details.getEmail_verified_at() == null) {
                            Intent intent = new Intent(Loading.this, SendEmailVerif.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //  Toast.makeText(Loading.this, details.getEmail_verified_at(), Toast.LENGTH_LONG).show();

                            startActivity(intent);
                        } else {
                            notifikasi();
                            linkFoto();
                            Intent intent = new Intent(Loading.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(Loading.this, "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
                    }
                });
                //end of put token


            }

            @Override
            public void onFailure(Call<Response_Psikolog> call, Throwable t) {
                Toast.makeText(Loading.this, "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void checkVerif() {
        retrofit2.Call<SendEmailVerif_Model> call = RetrofitClient.getInstance().getApi().getStatusVerif(token);
        call.enqueue(new Callback<SendEmailVerif_Model>() {
            @Override
            public void onResponse(retrofit2.Call<SendEmailVerif_Model> call, Response<SendEmailVerif_Model> response) {
                if (response.body().getMessage() == "Not verified") {

                    Intent intent = new Intent(Loading.this, SendEmailVerif.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    notifikasi();
                    linkFoto();
                    Intent intent = new Intent(Loading.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<SendEmailVerif_Model> call, Throwable t) {
                Log.d("Cek Verif", "onFailure: gagal");
            }
        });

    }

    public void notifikasi() {
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        Details details = SharedPrefManager.getInstance(this).getDetails();


        notification.setSmallIcon(R.drawable.nav_icon_orders);
        notification.setTicker("ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Log In Sukses");
        // notification.setLargeIcon(R.drawable.nav_icon_orders);
        notification.setContentText("Selamat Datang " + details.getName());

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqeID, notification.build());
    }


    public void linkFoto() {
        String link = "http://psikologi.ridwan.info/images/";
        //     String link = "http://10.0.2.2/psikolog-api/public/images/";
        SharedPreferences sharedPref = getSharedPreferences("linkFoto", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("link", link);
        editor.apply();

    }
}
