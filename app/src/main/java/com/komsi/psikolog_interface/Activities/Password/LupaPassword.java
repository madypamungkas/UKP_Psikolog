package com.komsi.psikolog_interface.Activities.Password;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.Dashboard;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.Response_ForgotPassword;
import com.komsi.psikolog_interface.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LupaPassword extends AppCompatActivity {
    EditText email_forgotPassword;
    Button bt_kirim;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);
        TextView back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LupaPassword.super.onBackPressed();
            }
        });
        email_forgotPassword = (EditText)findViewById(R.id.email_forgotPassword);

        bt_kirim = (Button)findViewById(R.id.bt_kirim);
        bt_kirim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(LupaPassword.this, null, "Sedang Memuat...", true, false);

                String email = email_forgotPassword.getText().toString();
                if(email.isEmpty()){
                    email_forgotPassword.setError("Email Harus Diisi");
                    email_forgotPassword.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    email_forgotPassword.setError("Isikan Email dengan Benar");
                    email_forgotPassword.requestFocus();
                    return;
                }

                Call<Response_ForgotPassword> call = RetrofitClient.getInstance().getApi().forgotPassword(email);
                call.enqueue(new Callback<Response_ForgotPassword>() {
                    @Override
                    public void onResponse(Call<Response_ForgotPassword> call, Response<Response_ForgotPassword> response) {
                        loading.dismiss();

                        if( response.isSuccessful()){

                            if(response.body().isStatus() == true){
                                //Toast.makeText(LupaPassword.this, "Sukses. "+ response.body().getMessage().getMessage() ,Toast.LENGTH_LONG).show();
                                Toast.makeText(LupaPassword.this, "Sukses. "+response.body().getMessage() ,Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(LupaPassword.this, Dashboard.class);
                                startActivity(intent);
                            }
                            else {
                                loading.dismiss();
                                Toast.makeText(LupaPassword.this, "Gagal. "+response.body().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(LupaPassword.this, "Gagal Mengirim Reset Link, Periksa Kembali Alamat Email Anda",Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Response_ForgotPassword> call, Throwable t) {
                        loading.dismiss();
                        Toast.makeText(LupaPassword.this, "Periksa Kembali Jaringan Anda" ,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });



    }
}
