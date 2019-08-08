package com.komsi.psikolog_interface.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.Menu.JadwalSaya;
import com.komsi.psikolog_interface.Activities.Menu.UbahBiodata;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.Default_Response;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.Models.Psikolog;
import com.komsi.psikolog_interface.Models.Reponse_updateJadwal;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.Models.ValidasiJadwal_Response;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailKonsultasi_Klien extends AppCompatActivity {
    TextView detailNama, detailLayanan, detailHubungan, detailPsikolog, detailTangal, detailSesi, detailRuangan, detailStatus, keluhan;
    Psikolog psikolog = SharedPrefManager.getInstance(this).getPsikolog();
    Details details = SharedPrefManager.getInstance(this).getDetails();
    Dialog myDialog;
    int klienId;
    Button bt_konfirmasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_konsultasi__klien);
        Button close = (Button) findViewById(R.id.close_detail);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailKonsultasi_Klien.super.onBackPressed();
            }
        });
        TextView back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailKonsultasi_Klien.super.onBackPressed();
            }
        });
        detailNama = (TextView) findViewById(R.id.namaKlien);
        detailNama.setText(": " + getIntent().getStringExtra("namaKlien"));
        detailLayanan = (TextView) findViewById(R.id.layanan);
        detailLayanan.setText(": " + getIntent().getStringExtra("layanan"));
        detailHubungan = (TextView) findViewById(R.id.detailHubungan);
        detailHubungan.setText(": " + getIntent().getStringExtra("hubungan"));
        detailPsikolog = (TextView) findViewById(R.id.detailPsikolog);
        detailPsikolog.setText(": " + getIntent().getStringExtra("psikolog"));
        detailTangal = (TextView) findViewById(R.id.detailTanggal);
        detailTangal.setText(": " + getIntent().getStringExtra("tanggal"));
        detailSesi = (TextView) findViewById(R.id.detailSesi);
        detailSesi.setText(": " + getIntent().getStringExtra("sesi") + " (" + getIntent().getStringExtra("jam") + ")");
        detailRuangan = (TextView) findViewById(R.id.detailRuangan);
        detailRuangan.setText(": " + getIntent().getStringExtra("ruangan"));
        detailStatus = (TextView) findViewById(R.id.detailStatus);
        detailStatus.setText(": " + getIntent().getStringExtra("status"));
        keluhan = (TextView) findViewById(R.id.keluhan);
        keluhan.setText(": " + getIntent().getStringExtra("keluhan"));
        klienId = getIntent().getIntExtra("idklien", 1);

        ImageView avatarDetail = findViewById(R.id.avatarDetail);
        String url = "http://psikologi.ridwan.info/images/" + getIntent().getStringExtra("foto");
        Picasso.with(this).load(url).error(R.drawable.menu_icon_user).into(avatarDetail);


        bt_konfirmasi = (Button) findViewById(R.id.bt_konfirmasi);
        cekJadwal();
    }

    public void konfirmasi() {
        User user = SharedPrefManager.getInstance(this).getUser();
        int idJadwal = getIntent().getIntExtra("idJadwal", -1);
        String token = "Bearer " + user.getToken();
        SharedPreferences sharedPref = getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
        final int psikologId = sharedPref.getInt("idPsikolog", -1);

        Call<Reponse_updateJadwal> call = RetrofitClient.getInstance().getApi().updateJadwal7(token,
                idJadwal, psikologId, klienId, 7);
        call.enqueue(new Callback<Reponse_updateJadwal>() {
            @Override
            public void onResponse(Call<Reponse_updateJadwal> call, Response<Reponse_updateJadwal> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailKonsultasi_Klien.this, "Sukses Mengambil Klien",
                            Toast.LENGTH_LONG).show();
                    Intent sukses = new Intent(DetailKonsultasi_Klien.this, JadwalSaya.class);
                    startActivity(sukses);
                } else {
                    Toast.makeText(DetailKonsultasi_Klien.this, "Gagal Mengambil Klien",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Reponse_updateJadwal> call, Throwable t) {
                Toast.makeText(DetailKonsultasi_Klien.this, "Terjadi Kesalahan Data",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void cekJadwal() {
        User user = SharedPrefManager.getInstance(this).getUser();
        int idJadwal = getIntent().getIntExtra("idJadwal", -1);
        String token = "Bearer " + user.getToken();
        SharedPreferences sharedPref = getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
        final int psikologId = sharedPref.getInt("idPsikolog", -1);
        final String tanggalJadwal = getIntent().getStringExtra("tanggal");
        final String keluhanKlien = getIntent().getStringExtra("keluhan");
        final int layanan_id = getIntent().getIntExtra("idlayanan", -1);
        final int sesi_id = getIntent().getIntExtra("idsesi", -1);
        final int klien_id = getIntent().getIntExtra("idklien", -1);


        Call<ValidasiJadwal_Response> call = RetrofitClient.getInstance().getApi().validasiJadwal(token,
                tanggalJadwal,
                keluhanKlien + " .",
                sesi_id,
                layanan_id,
                psikologId,
                klien_id,
                6);
        call.enqueue(new Callback<ValidasiJadwal_Response>() {
            @Override
            public void onResponse(Call<ValidasiJadwal_Response> call, final Response<ValidasiJadwal_Response> response) {
                Toast.makeText(DetailKonsultasi_Klien.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                   // Toast.makeText(DetailKonsultasi_Klien.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    if (status.equals("failed")) {
                        // bt_konfirmasi.setEnabled(false);
                        Snackbar.make(findViewById(R.id.detail_layout),response.body().getMessage()+" ", Snackbar.LENGTH_LONG).show();

                        bt_konfirmasi.setText("Jadwal Tidak Tersedia");
                        bt_konfirmasi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(DetailKonsultasi_Klien.this, response.body().getMessage() +
                                        " ", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        //    konfirmasi();
                        bt_konfirmasi.setEnabled(true);
                        bt_konfirmasi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (details.getIsActive().equals("Aktif")) {
                                    //checJadwal();
                                    konfirmasi();
                                } else {
                                    Toast.makeText(DetailKonsultasi_Klien.this, "Status Anda Tidak Aktif", Toast.LENGTH_LONG).show();
                                    if (!details.getStatus().equals("Approved")) {
                                        myDialog = new Dialog(DetailKonsultasi_Klien.this);
                                        myDialog.setContentView(R.layout.popup_isi_biodata);
                                        Button isiBiodata = (Button) myDialog.findViewById(R.id.isiBiodata);
                                        isiBiodata.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent isiBiodata = new Intent(DetailKonsultasi_Klien.this, UbahBiodata.class);
                                                myDialog.dismiss();
                                                startActivity(isiBiodata);
                                            }
                                        });
                                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        myDialog.show();
                                    }
                                }
                            }
                        });
                    }

                } else {
                    Toast.makeText(DetailKonsultasi_Klien.this, "Gagal Memvalidasi Jadwal ",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ValidasiJadwal_Response> call, Throwable t) {
                Toast.makeText(DetailKonsultasi_Klien.this, "Gagal Validasi Jadwal", Toast.LENGTH_LONG).show();
            }
        });

    }


}
