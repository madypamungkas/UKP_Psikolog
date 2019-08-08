package com.komsi.psikolog_interface.Activities.Menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.psikolog_interface.Adapter.KonsultasiAdapter;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.JadwalModels;
import com.komsi.psikolog_interface.Models.KonsultasiResponse;
import com.komsi.psikolog_interface.Models.StatusModel;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class CariKlien extends AppCompatActivity {
    private String token;
    private int idUser, klienId;
    private RecyclerView recyclerViewKonsultasi;
    private KonsultasiAdapter adapter;
    private List<JadwalModels> jadwalModelsList;
    private StatusModel statusModel;
    ProgressDialog loading;
    ScrollView recyclerView;
    LinearLayout noData;
    SwipeRefreshLayout swiperefresh;
    private int refresh_count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_klien);
        TextView back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CariKlien.super.onBackPressed();
            }
        });
        recyclerViewKonsultasi = findViewById(R.id.recyclerViewKonsultasi);
        recyclerViewKonsultasi.setLayoutManager(new LinearLayoutManager(this));
        recyclerView =findViewById(R.id.recyclerView);
        noData = findViewById(R.id.noData);
        swiperefresh = findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListJadwal();
            }
        });

    }
    public void  getListJadwal(){
        loading = ProgressDialog.show(CariKlien.this, null, "Sedang Memuat...", true, false);
        User user  = SharedPrefManager.getInstance(this).getUser();
        SharedPreferences sharedPref = getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
        final int idPsikolog= sharedPref.getInt("idPsikolog", 0);


        token = "Bearer "+ user.getToken();
        retrofit2.Call<KonsultasiResponse> call = RetrofitClient.getInstance().getApi().getCariKlien(token, 5);
        call.enqueue(new Callback<KonsultasiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<KonsultasiResponse> call, Response<KonsultasiResponse> response) {
                loading.dismiss();
                jadwalModelsList = response.body().getJadwal();
                refresh_count++;
                swiperefresh.setRefreshing(false);
                if (refresh_count > 3){
                    refresh_count = 0;
                    swiperefresh.setRefreshing(false);
                }
                // statusModel = (StatusModel) response.body().getJadwal();
                adapter = new KonsultasiAdapter(CariKlien.this, jadwalModelsList);//, statusModel);
                recyclerViewKonsultasi.setAdapter(adapter);
                if (jadwalModelsList.size() > 0){
                    recyclerView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                }
                else{
                    recyclerView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(retrofit2.Call<KonsultasiResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(CariKlien.this, "Gagal", Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                refresh_count++;
                swiperefresh.setRefreshing(false);
                if (refresh_count > 3){
                    refresh_count = 0;
                    swiperefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getListJadwal();
    }
}
