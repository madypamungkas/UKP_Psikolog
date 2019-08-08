package com.komsi.psikolog_interface.Activities.EditActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.MainActivity;
import com.komsi.psikolog_interface.Activities.Menu.CariKlien;
import com.komsi.psikolog_interface.Activities.Menu.KeahlianPsikolog;
import com.komsi.psikolog_interface.Adapter.ChoosenLayananAdapter;
import com.komsi.psikolog_interface.Adapter.LayananAdapter;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.ArrayLayananModel;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.Models.LayananModel;
import com.komsi.psikolog_interface.Models.Psikolog;
import com.komsi.psikolog_interface.Models.Response_Layanan;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetKeahlian extends AppCompatActivity {
    private RecyclerView recyclerViewlayanan, recyclerViewChoosenLayanan;
    private LayananAdapter adapter;
    private ChoosenLayananAdapter adapterChoosen;
    private List<LayananModel> layananList;
    private ArrayList<ArrayLayananModel> arrayLayanan;
    private RecyclerView.LayoutManager mLayoutManager;
    User user = SharedPrefManager.getInstance(this).getUser();
    String token = "Bearer " + user.getToken();
    Details details = SharedPrefManager.getInstance(this).getDetails();
    Psikolog psikolog = SharedPrefManager.getInstance(this).getPsikolog();
    ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_keahlian);
        TextView back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetKeahlian.super.onBackPressed();
            }
        });

        Button bt_daftarSaya = findViewById(R.id.bt_daftarSaya);
        bt_daftarSaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref2 = getSharedPreferences("Array Layanan", Context.MODE_PRIVATE);
                String lay = sharedPref2.getString("idLayanan", null)+" ";
                if (lay.length()>4) {
                    putLayanan();
                } else {
                    Toast.makeText(ResetKeahlian.this, "Pilih Layanan Terlebih Dahulu", Toast.LENGTH_LONG).show();
                }
            }
        });
        recyclerViewlayanan = findViewById(R.id.recyclerViewlayanan);
        recyclerViewlayanan.setLayoutManager(new LinearLayoutManager(this));

        getListLayanan();
    }

    public void getListLayanan() {
        SharedPreferences sharedPref = getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
        int idPsikolog = sharedPref.getInt("idPsikolog", -1);
        retrofit2.Call<Response_Layanan> call = RetrofitClient.getInstance().getApi().getLayanan(token);
        call.enqueue(new Callback<Response_Layanan>() {
            @Override
            public void onResponse(retrofit2.Call<Response_Layanan> call, Response<Response_Layanan> response) {
                layananList = response.body().getLayanan();
                adapter = new LayananAdapter(ResetKeahlian.this, layananList);
                recyclerViewlayanan.setAdapter(adapter);
            }

            @Override
            public void onFailure(retrofit2.Call<Response_Layanan> call, Throwable t) {
                Toast.makeText(ResetKeahlian.this, "Gagal", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void putLayanan() {
        SharedPreferences sharedPref2 = getSharedPreferences("Array Layanan", Context.MODE_PRIVATE);
        String lay = sharedPref2.getString("idLayanan", null);

        String lays[] = lay.split(",");

        String a = " ";
        if (lay.length() < 3) {
            a = lays[1];
        } else {
            a = lays[1];
        }
        String b = "";
        if (lay.length() > 6) {
            b = lays[2];
        } else {
            b = lays[1];
        }
        String c = "";
        if (lay.length() > 8) {
            c = lays[3];
        } else {
            c = lays[1];
        }
        String d = "";
        if (lay.length() > 11) {
            d = lays[4];
        } else {
            d = lays[1];
        }
        String e = "";
        if (lay.length() > 13) {
            e = lays[5];
        } else {
            e = lays[1];
        }
        String f = "";

        //Toast.makeText(ResetKeahlian.this, a + " " + b + " " + c + " " + d + " " + e, Toast.LENGTH_LONG).show();


        SharedPreferences sharedPref = getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
        int idPsikolog = sharedPref.getInt("idPsikolog", -1);
        int idUser = sharedPref.getInt("user_id", -1);
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().updateLayanan(token, idUser, a, b, c, d, e, 19);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ResetKeahlian.this, "Sukses", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ResetKeahlian.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    //loading.dismiss();
                }
                else {
                    Toast.makeText(ResetKeahlian.this, "Gagal", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ResetKeahlian.this, "Gagal", Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        getListLayanan();
        SharedPreferences sharedPref = getSharedPreferences("Array Layanan", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        // editor.putString("idLayanan", " ");
        editor.clear();
        editor.apply();
    }

}
