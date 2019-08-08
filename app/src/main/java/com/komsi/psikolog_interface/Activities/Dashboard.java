package com.komsi.psikolog_interface.Activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.komsi.psikolog_interface.Activities.Daftar.DaftarActivity;
import com.komsi.psikolog_interface.Activities.VerifEmail.SendEmailVerif;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;

public class Dashboard extends AppCompatActivity {
    Button bt_login;
    Button bt_daftar;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(Dashboard.this, LoginActivity.class);
                startActivity(login);

            }
        });

        bt_daftar = (Button) findViewById(R.id.bt_daftar);
        bt_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent daftar = new Intent(Dashboard.this, DaftarActivity.class);
                startActivity(daftar);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        cekConnectivity();
        Details details = SharedPrefManager.getInstance(this).getDetails();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            if (details.getEmail_verified_at() == null) {
                intentVerif();

            } else {
                intentMain();
            }
        }
    }

    public void intentMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void intentVerif() {
        Intent intent = new Intent(this, SendEmailVerif.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void cekConnectivity() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Snackbar.make(findViewById(R.id.dashboardLayout), "Anda Terhubung", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById(R.id.dashboardLayout), "Koneksi Terputus, Anda Tidak Terhubung Dengan Layanan Internet", Snackbar.LENGTH_LONG).show();

        }
    }

    public void about(View view) {
        Intent intent = new Intent(Dashboard.this, About.class);
        startActivity(intent);
    }
}
