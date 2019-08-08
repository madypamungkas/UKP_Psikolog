package com.komsi.psikolog_interface.Activities.bin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.MainActivity;
import com.komsi.psikolog_interface.R;

public class KonfirmasiKonsultasi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_konsultasi);
        Button close = (Button)findViewById(R.id.close_detail);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               KonfirmasiKonsultasi.super.onBackPressed();
            }
        });
        Button ambil = (Button)findViewById(R.id.ambilKlien);
        ambil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ambil = new Intent(KonfirmasiKonsultasi.this, MainActivity.class);
                Toast.makeText(getApplicationContext(), "Konsultasi Telah Ditambahkan Ke Jadwal Anda", Toast.LENGTH_LONG)
                        .show();
                startActivity(ambil);
            }
        });

    }
}
