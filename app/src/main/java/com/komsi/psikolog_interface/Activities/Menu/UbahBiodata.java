package com.komsi.psikolog_interface.Activities.Menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.MainActivity;
import com.komsi.psikolog_interface.Adapter.LayananPsikologAdapter;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.Models.LayananModel;
import com.komsi.psikolog_interface.Models.Psikolog;
import com.komsi.psikolog_interface.Models.Response_Details;
import com.komsi.psikolog_interface.Models.Response_LayananPsikolog;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Activities.EditActivity.UbahBiodata_Edit;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class UbahBiodata extends AppCompatActivity {
    TextView nama, nik, jk,ttl,alamat,noHp, noSipp;
    ImageView avatar;
    private RecyclerView recyclerViewlayanan;
    private LayananPsikologAdapter adapter;
    private List<LayananModel> layananList;
    User user  = SharedPrefManager.getInstance(this).getUser();
    String token = "Bearer "+ user.getToken();
    Psikolog psikolog = SharedPrefManager.getInstance(this).getPsikolog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_biodata);
        Details details = SharedPrefManager.getInstance(this).getDetails();
       // Klien klien = SharedPrefManager.getInstance(this).getKlienModel();
        SharedPreferences sharedPrefFCM = getSharedPreferences("linkFoto", Context.MODE_PRIVATE);
        String link = sharedPrefFCM.getString("link", null);
        String url = "http://psikologi.ridwan.info/images/" + details.getFoto();
        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        Picasso.with(this).load(url).error(R.drawable.menu_icon_user).into(avatar);
        recyclerViewlayanan = findViewById(R.id.recyclerViewlayanan);
        recyclerViewlayanan.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences sharedPref = getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);

        nama = findViewById(R.id.bioName);
        nama.setText(" "+ details.getName() + " " + sharedPref.getString("gelar", null));
        nik = findViewById(R.id.bioNik);
        nik.setText(" "+ details.getNik());
        jk = findViewById(R.id.bioJk);
        jk.setText(" "+ details.getJenis_kelamin());
        ttl = findViewById(R.id.bioTTL);
        ttl.setText(" "+ details.getTanggal_lahir());
        alamat = findViewById(R.id.bioAlamat);
        alamat.setText(" "+ details.getAlamat());
        noHp = findViewById(R.id.bioNohp);
        noHp.setText(" "+ details.getNo_telepon());
        noSipp = findViewById(R.id.bioNoSipp);
        noSipp.setText(" " + sharedPref.getString("no_sipp", null));

        TextView back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UbahBiodata.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button edit = (Button)findViewById(R.id.bt_daftarSaya);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(UbahBiodata.this, UbahBiodata_Edit.class);
                startActivity(edit);
            }
        });
    }
    public void getListLayanan(){
        SharedPreferences sharedPref = getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
        int idPsikolog = sharedPref.getInt("idPsikolog", -1);
        retrofit2.Call<Response_LayananPsikolog> call = RetrofitClient.getInstance().getApi().getLayananPsikolog(token, idPsikolog);
        call.enqueue(new Callback<Response_LayananPsikolog>() {
            @Override
            public void onResponse(retrofit2.Call<Response_LayananPsikolog> call, Response<Response_LayananPsikolog> response) {
                layananList = response.body().getLayanan();
                adapter = new LayananPsikologAdapter( UbahBiodata.this, layananList);
                recyclerViewlayanan.setAdapter(adapter);
            }

            @Override
            public void onFailure(retrofit2.Call<Response_LayananPsikolog> call, Throwable t) {
                Toast.makeText(UbahBiodata.this, "Gagal", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void loadDetails(){
        retrofit2.Call<Response_Details> call = RetrofitClient
                .getInstance()
                .getApi()
                .getDetails(token);
        call.enqueue(new retrofit2.Callback<Response_Details>() {
            @Override
            public void onResponse(retrofit2.Call<Response_Details> call, retrofit2.Response<Response_Details> response) {
                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(UbahBiodata.this).saveDetails(response.body().getDetails());
                } else {
                    Toast.makeText(UbahBiodata.this, "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Response_Details> call, Throwable t) {
                Toast.makeText(UbahBiodata.this, "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getListLayanan();
        loadDetails();
    }
}
