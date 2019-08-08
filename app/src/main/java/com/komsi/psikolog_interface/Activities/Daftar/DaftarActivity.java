package com.komsi.psikolog_interface.Activities.Daftar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.LoginActivity;
import com.komsi.psikolog_interface.Activities.MainActivity;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.Models.JsonDefault;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout textName, textEmail, textPassword, textPassword_confirm, textUsername, textTelepon, textGelar, textNo_sipp;
    ProgressDialog loading;
    private ConnectivityManager connectivityManager;
    CheckBox termsCb;
    TextView termsText;
    Button daftar;

    NotificationCompat.Builder notification;
    public static final int uniqeID = 458714;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        termsCb = findViewById(R.id.termsCb);
        termsText = findViewById(R.id.termsText);

        textName = (TextInputLayout) findViewById(R.id.textInputNama);
        textEmail = (TextInputLayout) findViewById(R.id.textInputEmail);
        textPassword = (TextInputLayout) findViewById(R.id.textInputPassword);
        textPassword_confirm = (TextInputLayout) findViewById(R.id.textConfirmPassword);
        textUsername = (TextInputLayout) findViewById(R.id.textInputUsername);
        textTelepon = (TextInputLayout) findViewById(R.id.textInputnoTelepon);
        textGelar = (TextInputLayout) findViewById(R.id.textInputGelar);
        textNo_sipp = (TextInputLayout) findViewById(R.id.textInputSipp);
        //textKeahlian = (TextInputLayout) findViewById(R.id.textInputKeahlian);
        daftar = (Button) findViewById(R.id.bt_register);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignUp();
                //  cekConnectivity();
            }
        });


    }


    private void userSignUp() {
        String name = textName.getEditText().getText().toString().trim();
        String email = textEmail.getEditText().getText().toString().trim();
        String password = textPassword.getEditText().getText().toString().trim();
        String password_confirm = textPassword_confirm.getEditText().getText().toString().trim();
        String username = textUsername.getEditText().getText().toString().trim();
        String no_telepon = textTelepon.getEditText().getText().toString().trim();
        String gelar = textGelar.getEditText().getText().toString().trim();
        String no_sipp = textNo_sipp.getEditText().getText().toString().trim();
        // String keahlian = textKeahlian.getEditText().getText().toString().trim();

        if (name.isEmpty()) {
            textName.setError("Nama Harus Diisi!");
            textName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            textEmail.setError("Email Harus Diisi!");
            textEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textEmail.setError("Isikan Email yang Benar!");
            textEmail.requestFocus();
            return;
        }
        if (username.contains(" ")) {
            textUsername.setError("Username Terdiri Dari Satu Kata!");
            textUsername.requestFocus();
            return;
        }
        if (username.length() < 6) {
            textUsername.setError("Username Minimum Terdiri Dari 6 Karakter!");
            textUsername.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            textPassword.setError("Password Harus Diisi!");
            textPassword.requestFocus();
            return;
        }
        if (username.contains(" ")) {
            textUsername.setError("Username Terdiri Dari Satu Kata!");
            textUsername.requestFocus();
            return;
        }
        if (password.length() < 6) {
            textPassword.setError("Password Minimum Terdiri Dari 6 Karakter!");
            textPassword.requestFocus();
            return;
        }

        if (no_telepon.isEmpty()) {
            textTelepon.setError("No Telepon Harus Diisi!");
            textTelepon.requestFocus();
            return;
        }

        if (no_sipp.isEmpty()) {
            textNo_sipp.setError("No SIPP Harus Diisi!");
            textNo_sipp.requestFocus();
            return;
        }
        if (gelar.isEmpty()) {
            textGelar.setError("Gelar Harus Diisi!");
            textGelar.requestFocus();
            return;
        }
         if (!termsCb.isChecked()){
            termsCb.setError("Pilih Dan Setujui");
            termsCb.requestFocus();

            return;
        }


        loading = ProgressDialog.show(DaftarActivity.this, null, "Sedang Memuat...", true, false);

        Call<JsonDefault> call = RetrofitClient
                .getInstance()
                .getApi()
                .psikolog(name, email, password, username, no_telepon, gelar, no_sipp);
        call.enqueue(new Callback<JsonDefault>() {
            @Override
            public void onResponse(Call<JsonDefault> call, Response<JsonDefault> response) {
                loading.dismiss();
                JsonDefault signUpResponse = response.body();
                if (response.isSuccessful()){
                    if (signUpResponse.getStatus().equals("success")) {
                        Log.i("debug", "onResponse: SUCCESS");
                        loading.dismiss();
                        Toast.makeText(DaftarActivity.this, "Registration Success!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(DaftarActivity.this, LoginActivity.class));
                    }
                } else {
                    loading.dismiss();
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody.trim());
                        jsonObject = jsonObject.getJSONObject("error");
                        Iterator<String> keys = jsonObject.keys();
                        StringBuilder errors = new StringBuilder();
                        String separator = "";
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONArray arr = jsonObject.getJSONArray(key);
                            for (int i = 0; i < arr.length(); i++) {
                                errors.append(separator).append(key).append(" : ").append(arr.getString(i));
                                separator = "\n";
                            }
                        }
                        Toast.makeText(DaftarActivity.this, errors.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(DaftarActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonDefault> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(DaftarActivity.this, "Periksa Kembali Koneksi Internet Anda", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_register:
                userSignUp();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        cekConnectivity();
    }
    public void notifikasi() {
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);


        notification.setSmallIcon(R.drawable.nav_icon_orders);
        notification.setTicker("ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Pendaftaran Sukses");
        // notification.setLargeIcon(R.drawable.nav_icon_orders);
        notification.setContentText("Pendaftaran Akun Psikolog Sukses, Silahkan Log In " );

        Intent intent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqeID, notification.build());
    }


    private void cekConnectivity() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Snackbar.make(findViewById(R.id.daftarLayout), "Anda Terhubung", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById(R.id.daftarLayout), "Koneksi Terputus, Anda Tidak Terhubung Dengan Layanan Internet", Snackbar.LENGTH_LONG).show();

        }
    }

}
