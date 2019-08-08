package com.komsi.psikolog_interface.Activities.Password;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.MainActivity;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.Models.Response_GantiPassword;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GantiPassword extends AppCompatActivity {
    TextInputLayout passwordLama, passwordBaru, passwordKonfirm;
    private String token;
    private int userId;
    Context mCtx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);
        TextView back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GantiPassword.super.onBackPressed();
            }
        });


        Button gPass = (Button) findViewById(R.id.bt_ubahPassword);
        gPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Details details = SharedPrefManager.getInstance(mCtx).getDetails();
                //Klien klien = SharedPrefManager.getInstance(this).getKlienModel();
                User user = SharedPrefManager.getInstance(mCtx).getUser();
                SharedPreferences sharedPref = getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
                int iduser= sharedPref.getInt("user_id", 0);
                token ="Bearer "+ user.getToken();
                userId = iduser;


                passwordLama = (TextInputLayout)findViewById(R.id.passwordLama);
                passwordBaru = (TextInputLayout)findViewById(R.id.passwordBaru);
                passwordKonfirm = (TextInputLayout)findViewById(R.id.passwordKonfirm);

                final String passwordLamaUser = passwordLama.getEditText().getText().toString();
                final String passwordBaruUser =  passwordBaru.getEditText().getText().toString();
                final String passwordKonfirmUser =  passwordKonfirm.getEditText().getText().toString();

                if(passwordLamaUser.equals(passwordBaruUser)){
                    passwordBaru.setError(" Password Tidak Boleh Sama Dengan Password Lama");
                    passwordBaru.requestFocus();
                    return;
                }
                if(!passwordBaruUser.equals(passwordKonfirmUser) ){
                    passwordKonfirm.setError(" Password Yang Anda Isikan Tidak Cocok");
                    passwordKonfirm.requestFocus();
                    return;
                }if(passwordBaruUser.equals(passwordKonfirmUser) ){
                    passwordKonfirm.setErrorEnabled(false);
                }
                if(passwordLamaUser.isEmpty())
                {
                    passwordLama.setError("Password Lama Tidak Boleh Kosong");
                    passwordLama.requestFocus();
                    return;
                }
                if(passwordBaruUser.isEmpty())
                {
                    passwordBaru.setError("Password Baru Tidak Boleh Kosong");
                    passwordBaru.requestFocus();
                    return;
                }if(passwordBaruUser.length() < 6)
                {
                    passwordBaru.setError("Password Baru Minimum Terdiri Dari 6 Karakter");
                    passwordBaru.requestFocus();
                    return;
                }
                if(passwordBaruUser.contains(" "))
                {
                    passwordBaru.setError("Password Terdiri Dari Satu Kata!");
                    passwordBaru.requestFocus();
                    return;
                }

                if(passwordKonfirmUser.isEmpty())
                {
                    passwordKonfirm.setError("Konfirmasi Password Tidak Boleh Kosong");
                    passwordKonfirm.requestFocus();
                    return;
                }



                retrofit2.Call<Response_GantiPassword> call = RetrofitClient.getInstance().getApi().updatePass(token, userId, passwordLamaUser, passwordBaruUser, passwordKonfirmUser);
                call.enqueue(new Callback<Response_GantiPassword>() {
                    @Override
                    public void onResponse(Call<Response_GantiPassword> call, Response<Response_GantiPassword> response) {
                        SharedPrefManager.getInstance(GantiPassword.this).saveStatusGantiPassword(response.body().getPassword());
                        String statusResponse = SharedPrefManager.getInstance(GantiPassword.this).getGantiPass().getStatus();
                       // Toast.makeText(GantiPassword.this, userId, Toast.LENGTH_LONG).show();


                        if (statusResponse.equals("sukses")){
                            Toast.makeText(GantiPassword.this, statusResponse, Toast.LENGTH_LONG).show();
                            Intent sukses = new Intent(GantiPassword.this, MainActivity.class);
                            startActivity(sukses);
                        }
                        else{
                            Toast.makeText(GantiPassword.this, statusResponse, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Response_GantiPassword> call, Throwable t) {
                        Toast.makeText(GantiPassword.this, "Periksa Kembali Koneksi Anda ", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });
    }
}
