package com.komsi.psikolog_interface.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.komsi.psikolog_interface.Activities.Menu.JadwalSaya;
import com.komsi.psikolog_interface.R;

public class Notifikasi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);
        RelativeLayout notif = (RelativeLayout)findViewById(R.id.notif_jadwal);
        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notif  = new Intent(Notifikasi.this, JadwalSaya.class);
                startActivity(notif);
            }
        });
    }
}
