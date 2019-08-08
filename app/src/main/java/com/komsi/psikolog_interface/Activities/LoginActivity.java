package com.komsi.psikolog_interface.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.Daftar.DaftarActivity;
import com.komsi.psikolog_interface.Activities.Password.LupaPassword;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.LoginResponse;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    ProgressDialog loading;
    TextView bt_lupaPassword;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);


        Button daftar = (Button) findViewById(R.id.bt_daftarSkrg);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent daftar = new Intent(LoginActivity.this, DaftarActivity.class);
                startActivity(daftar);
            }
        });
        bt_lupaPassword = (TextView) findViewById(R.id.bt_lupaPassword);
        bt_lupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lupaPassword = new Intent(LoginActivity.this, LupaPassword.class);
                startActivity(lupaPassword);
            }
        });

        textInputUsername = findViewById(R.id.textInputUsername);
        textInputPassword = findViewById(R.id.textInputPassword);
        final Button login = (Button) findViewById(R.id.bt_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = textInputPassword.getEditText().getText().toString().trim();
                String username = textInputUsername.getEditText().getText().toString().trim();
                if (username.isEmpty()) {
                    textInputUsername.setError("Password Harus Diisi!");
                    textInputUsername.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    textInputPassword.setError("Password Harus Diisi!");
                    textInputPassword.requestFocus();
                    return;
                }
                if (username.isEmpty()) {
                    textInputUsername.setError("Username Harus Diisi!");
                    textInputUsername.requestFocus();
                    return;
                }

                loading = ProgressDialog.show(LoginActivity.this, null, "Sedang Memuat...", true, false);

                Call<LoginResponse> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .login(username, password, "psikolog");
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        //  LoginResponse loginResponse =response.body();
                        //  loading.dismiss();
                        if (response.isSuccessful()) {

                            SharedPrefManager.getInstance(LoginActivity.this)
                                    .saveUser(response.body().getUser());
                            Toast.makeText(LoginActivity.this, "Selamat Datang " + response.body().getUser().getUsername_id(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, Loading.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            loading.dismiss();
                        } else {
                            loading.dismiss();
                            Toast.makeText(LoginActivity.this, "Error, Periksa Kembali Username dan Password Anda", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        loading.dismiss();
                        Toast.makeText(LoginActivity.this, "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
                        // loading.dismiss();
                    }
                });
            }
        });
    }

    public void showResultDialogue() {
        SharedPreferences sharedPrefFCM = getSharedPreferences("TokenFCM", Context.MODE_PRIVATE);
        final String TokenFCM = sharedPrefFCM.getString("token", null);
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Token FCM")
                .setMessage("Token FCM " + TokenFCM)
                .setPositiveButton("Copy Token", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Hasil Scanning", TokenFCM);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(LoginActivity.this, "Hasil Scan Berhasil Ter-Copy", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
        Log.d("meetmy", "onStart: " + TokenFCM);
    }

    @Override
    protected void onStart() {
        super.onStart();
       // showResultDialogue();
        cekConnectivity();

    }

    private void cekConnectivity() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Snackbar.make(findViewById(R.id.loginLayout), "Anda Terhubung", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById(R.id.loginLayout), "Koneksi Terputus, Anda Tidak Terhubung Dengan Layanan Internet", Snackbar.LENGTH_LONG).show();

        }
    }


}

