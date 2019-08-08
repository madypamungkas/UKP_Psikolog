package com.komsi.psikolog_interface.Activities.VerifEmail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.Loading;
import com.komsi.psikolog_interface.Activities.MainActivity;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.Models.SendEmailVerif_Model;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendEmailVerif extends AppCompatActivity {
    TextView emailTxt, resend, countdown;
    Button bt_verif, bt_sendCode;
    EditText code1, code2, code3, code4, code5, code6;
    LinearLayout sendVerif, sendCode, resendLayout;

    Details details = SharedPrefManager.getInstance(this).getDetails();
    User user = SharedPrefManager.getInstance(this).getUser();
    final String token = "Bearer " + user.getToken();

    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email_verif);
        emailTxt = findViewById(R.id.email);
        resend = findViewById(R.id.resend);
        countdown = findViewById(R.id.countdown);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerif.setVisibility(View.VISIBLE);
                sendCode.setVisibility(View.GONE);
            }
        });

        String email = details.getEmail();
        String[] emailsplit = email.split("@");
        String bodyemail = emailsplit[0].substring(0, 4);
        String emailfull = bodyemail + "****@" + emailsplit[1];
        emailTxt.setText(emailfull);

        resendLayout = findViewById(R.id.resendLayout);
        sendCode = findViewById(R.id.sendCode);
        sendVerif = findViewById(R.id.sendVerif);
        code1 = findViewById(R.id.code1);
        code2 = findViewById(R.id.code2);
        code3 = findViewById(R.id.code3);
        code4 = findViewById(R.id.code4);
        code5 = findViewById(R.id.code5);
        code6 = findViewById(R.id.code6);

        bt_verif = findViewById(R.id.bt_verif);
        bt_verif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerif();
            }
        });
        bt_sendCode = findViewById(R.id.bt_sendCode);
        bt_sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSendCode();
            }
        });

    }

    public void openWebsite() {
        String email = details.getEmail();
        String[] emailsplit = email.split("@");
        String url = emailsplit[1];
        Uri webpage = Uri.parse("http://" + url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(intent);

    }

        public void sendVerif() {
            loading = ProgressDialog.show(SendEmailVerif.this, null, "Sedang Memproses...", true, false);

           Call<SendEmailVerif_Model> call = RetrofitClient.getInstance().getApi().getSendVerif(token);
           call.enqueue(new Callback<SendEmailVerif_Model>() {
               @Override
               public void onResponse(Call<SendEmailVerif_Model> call, Response<SendEmailVerif_Model> response) {
                   loading.dismiss();
                   if (response.isSuccessful()) {

                       //openWebsite();
                       sendVerif.setVisibility(View.GONE);
                       sendCode.setVisibility(View.VISIBLE);
                       setCountdown();
                       Toast.makeText(SendEmailVerif.this, response.body().getStatus() + " ", Toast.LENGTH_LONG).show();
                   }
                   else{
                       Toast.makeText(SendEmailVerif.this,  "gagal ", Toast.LENGTH_LONG).show();

                   }

               }

               @Override
               public void onFailure(Call<SendEmailVerif_Model> call, Throwable t) {
                   loading.dismiss();
                   openWebsite();
                   sendVerif.setVisibility(View.GONE);
                   sendCode.setVisibility(View.VISIBLE);
                   setCountdown();
                   //Log.d("Failure Send", "onFailure: gagal kirim ");
                   Toast.makeText(SendEmailVerif.this,"Periksa Koneksi Internet Anda", Toast.LENGTH_LONG).show();

               }
           });
        }

        public void setCountdown() {
            CountDownTimer cd = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    countdown.setText("Kirim Ulang Dalam : 00:" + l / 1000);
                }

                @Override
                public void onFinish() {
                    resendLayout.setVisibility(View.VISIBLE);
                    countdown.setText("Kirim Ulang Sekarang");
                }
            }.start();
        }

        public void setSendCode() {
            String c1 = code1.getText().toString();
            String c2 = code2.getText().toString();
            String c3 = code3.getText().toString();
            String c4 = code4.getText().toString();
            String c5 = code5.getText().toString();
            String c6 = code6.getText().toString();
            if (c1.isEmpty()) {
                code1.setError("Kosong");
                code1.requestFocus();
                return;
            }
            if (c2.isEmpty()) {
                code2.setError("Kosong");
                code2.requestFocus();
                return;
            }
            if (c3.isEmpty()) {
                code3.setError("Kosong");
                code3.requestFocus();
                return;
            }
            if (c4.isEmpty()) {
                code4.setError("Kosong");
                code4.requestFocus();
                return;
            }
            if (c5.isEmpty()) {
                code5.setError("Kosong");
                code5.requestFocus();
                return;
            }
            if (c6.isEmpty()) {
                code6.setError("Kosong");
                code6.requestFocus();
                return;
            }

            String codefull = c1 + c2 + c3 + c4 + c5 + c6;
            if (codefull.length() > 6) {
                code1.setError("Kode Terdiri Dari 6 Digit");
                code1.requestFocus();
                return;
            }
            loading = ProgressDialog.show(SendEmailVerif.this, null, "Sedang Memproses...", true, false);


            Call<SendEmailVerif_Model> call = RetrofitClient.getInstance().getApi().postCodeVerif(token, codefull);
            call.enqueue(new Callback<SendEmailVerif_Model>() {
                @Override
                public void onResponse(Call<SendEmailVerif_Model> call, Response<SendEmailVerif_Model> response) {
                    loading.dismiss();
                    //Toast.makeText(SendEmailVerif.this, response.body().getStatus()+"Verifikasi Berhasil", Toast.LENGTH_LONG).show();
                    String status = response.body().getStatus();
                    if (status.equals("success")) {
                        Toast.makeText(SendEmailVerif.this, "Verifikasi Berhasil", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SendEmailVerif.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SendEmailVerif.this, "Kode Yang Anda Masukkan Tidak Tepat", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<SendEmailVerif_Model> call, Throwable t) {
                    loading.dismiss();
                    Toast.makeText(SendEmailVerif.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_LONG).show();
                }
            });

        }

}
