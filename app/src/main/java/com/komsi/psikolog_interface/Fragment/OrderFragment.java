package com.komsi.psikolog_interface.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.komsi.psikolog_interface.Adapter.JadwalAdapter;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.JadwalModels;
import com.komsi.psikolog_interface.Models.JadwalResponse;
import com.komsi.psikolog_interface.Models.StatusModel;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Activities.bin.RiwayatKonsultasi;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment implements View.OnClickListener{


    public OrderFragment() {
        // Required empty public constructor
    }
    private String token;
    private int idUser, klienId;
    private RecyclerView recyclerViewJadwal;
    private JadwalAdapter adapter;
    private List<JadwalModels> jadwalModelsList;
    private StatusModel statusModel;
    ProgressDialog loading;
    ScrollView recyclerView;
    LinearLayout noData;
    SwipeRefreshLayout swiperefresh;
    private int refresh_count = 0;
    private ConnectivityManager connectivityManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_order, container, false);
        ImageView sem_konsul = (ImageView) fragmentView.findViewById(R.id.semua_konsul);
        sem_konsul.setOnClickListener(this);
        return fragmentView;

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);

        recyclerViewJadwal = view.findViewById(R.id.recyclerViewJadwal);
        recyclerViewJadwal.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView =view.findViewById(R.id.recyclerView);
        noData = view.findViewById(R.id.noData);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListJadwal();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.semua_konsul:
                Intent semua = new Intent(getActivity(), RiwayatKonsultasi.class);
                startActivity(semua);
                break;
        }

    }
    public void getListJadwal(){
        User user  = SharedPrefManager.getInstance(getActivity()).getUser();
        token = "Bearer "+ user.getToken();
      //  Klien klien= SharedPrefManager.getInstance(getActivity()).getKlienModel();
        loading = ProgressDialog.show(getActivity(), null, "Sedang Memuat...", true, false);



        SharedPreferences sharedPref = getActivity().getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
        final int idPsikolog= sharedPref.getInt("idPsikolog", 0);

        retrofit2.Call<JadwalResponse> call = RetrofitClient.getInstance().getApi().getRiwayat(token, idPsikolog);
        call.enqueue(new Callback<JadwalResponse>() {

            @Override
            public void onResponse(retrofit2.Call<JadwalResponse> call, Response<JadwalResponse> response) {
               loading.dismiss();
                refresh_count++;
                swiperefresh.setRefreshing(false);
                if (refresh_count > 3){
                    refresh_count = 0;
                    swiperefresh.setRefreshing(false);
                }

                if (response.body().getJadwal() != null) {
                    jadwalModelsList = response.body().getJadwal();
                    //   statusModel = (StatusModel) response.body().getJadwal().;
                    adapter = new JadwalAdapter(getActivity(), jadwalModelsList);//, statusModel);
                    recyclerViewJadwal.setAdapter(adapter);

                    if (jadwalModelsList.size() > 0){
                        recyclerView.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                    }
                    else{
                        recyclerView.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    swiperefresh.setRefreshing(false);
                    Toast.makeText(getActivity(), "Tidak Ada Data", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<JadwalResponse> call, Throwable t) {
              loading.dismiss();
                Toast.makeText(getActivity(), "Gagal", Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                swiperefresh.setRefreshing(false);

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        getListJadwal();
        cekConnectivity();
      /*  User user  = SharedPrefManager.getInstance(getActivity()).getUser();
        Klien klien= SharedPrefManager.getInstance(getActivity()).getKlienModel();

        idUser = user.getId();
        token = "Bearer "+ user.getToken();
        Call<KlienResponse> call = RetrofitClient.getInstance().getApi().getKlien(token,idUser);
        call.enqueue(new Callback<KlienResponse>() {
            @Override
            public void onResponse(Call<KlienResponse> call, Response<KlienResponse> response) {
                klienId = response.body().getKlien().getKlienId();
                //Toast.makeText(getActivity(), response.body().getKlien().getKlienId(), Toast.LENGTH_LONG).show();
                getListJadwal();
                loading.dismiss();
            }

            @Override
            public void onFailure(Call<KlienResponse> call, Throwable t) {
                loading.dismiss();
            }
        });
*/


    }

    private void cekConnectivity() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Snackbar.make(getActivity().findViewById(R.id.orderFragment), "Anda Terhubung", Snackbar.LENGTH_LONG);
        } else {
            Snackbar.make(getActivity().findViewById(R.id.orderFragment), "Koneksi Terputus, Anda Tidak Terhubung Dengan Layanan Internet", Snackbar.LENGTH_LONG).show();

        }
    }


}


