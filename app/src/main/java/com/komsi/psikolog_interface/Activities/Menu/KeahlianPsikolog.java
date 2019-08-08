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
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.EditActivity.EditAkun;
import com.komsi.psikolog_interface.Activities.EditActivity.ResetKeahlian;
import com.komsi.psikolog_interface.Activities.EditActivity.UbahBiodata_Edit;
import com.komsi.psikolog_interface.Activities.MainActivity;
import com.komsi.psikolog_interface.Adapter.LayananPsikologAdapter;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.LayananModel;
import com.komsi.psikolog_interface.Models.Psikolog;
import com.komsi.psikolog_interface.Models.Response_LayananPsikolog;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class KeahlianPsikolog extends AppCompatActivity {
    private RecyclerView recyclerViewlayanan;
    private LayananPsikologAdapter adapter;
    private List<LayananModel> layananList;
    User user = SharedPrefManager.getInstance(this).getUser();
    String token = "Bearer " + user.getToken();
    Psikolog psikolog = SharedPrefManager.getInstance(this).getPsikolog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keahlian_psikolog);
        TextView back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(KeahlianPsikolog.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        recyclerViewlayanan = findViewById(R.id.recyclerViewlayanan);
        recyclerViewlayanan.setLayoutManager(new LinearLayoutManager(this));
        getListLayanan();
        Button edit = (Button) findViewById(R.id.bt_daftarSaya);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(KeahlianPsikolog.this, ResetKeahlian.class);
                startActivity(edit);
            }
        });
    }

    public void getListLayanan() {
        SharedPreferences sharedPref = getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
        int idPsikolog = sharedPref.getInt("idPsikolog", -1);
        retrofit2.Call<Response_LayananPsikolog> call = RetrofitClient.getInstance().getApi()
                .getLayananPsikolog(token, idPsikolog);
        call.enqueue(new Callback<Response_LayananPsikolog>() {
            @Override
            public void onResponse(retrofit2.Call<Response_LayananPsikolog> call,
                                   Response<Response_LayananPsikolog> response) {
                layananList = response.body().getLayanan();
                adapter = new LayananPsikologAdapter(KeahlianPsikolog.this, layananList);
                recyclerViewlayanan.setAdapter(adapter);
            }

            @Override
            public void onFailure(retrofit2.Call<Response_LayananPsikolog> call, Throwable t) {
                Toast.makeText(KeahlianPsikolog.this, "Gagal", Toast.LENGTH_LONG).show();
            }
        });

    }
}
