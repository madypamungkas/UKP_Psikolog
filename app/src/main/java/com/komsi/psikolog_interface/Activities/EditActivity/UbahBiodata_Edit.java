package com.komsi.psikolog_interface.Activities.EditActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.komsi.psikolog_interface.Activities.MainActivity;
import com.komsi.psikolog_interface.Activities.Menu.UbahBiodata;
import com.komsi.psikolog_interface.Adapter.ChoosenLayananAdapter;
import com.komsi.psikolog_interface.Adapter.LayananAdapter;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.ArrayLayananModel;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.Models.LayananModel;
import com.komsi.psikolog_interface.Models.Psikolog;
import com.komsi.psikolog_interface.Models.Response_Details;
import com.komsi.psikolog_interface.Models.Response_Layanan;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahBiodata_Edit extends AppCompatActivity {
    private RecyclerView recyclerViewlayanan, recyclerViewChoosenLayanan;
    private LayananAdapter adapter;
    private ChoosenLayananAdapter adapterChoosen;
    private List<LayananModel> layananList;
    private ArrayList<ArrayLayananModel> arrayLayanan;
    private RecyclerView.LayoutManager mLayoutManager;
    User user  = SharedPrefManager.getInstance(this).getUser();
    String token = "Bearer "+ user.getToken();
    Details details = SharedPrefManager.getInstance(this).getDetails();
    Psikolog psikolog = SharedPrefManager.getInstance(this).getPsikolog();
    private String genderKlien;
    TextInputLayout biodata_nama, biodata_gelar, biodata_NIK, biodata_TL, biodata_sipp, biodata_alamat;
    RadioGroup rg_jk;
    RadioButton r_lakilaki, r_perempuan;
    Dialog myDialog;
    ImageView avatar;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    ProgressDialog loading;
    ArrayList<ArrayLayananModel>arrayLayananModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_biodata__edit);
        TextView back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UbahBiodata_Edit.super.onBackPressed();
            }
        });

        final Button simpan = (Button)findViewById(R.id.bt_simpanBiodata);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            simpan();
            }
        });
        avatar = (ImageView) findViewById(R.id.avatar);
        SharedPreferences sharedPrefFCM = getSharedPreferences("linkFoto", Context.MODE_PRIVATE);
        String link = sharedPrefFCM.getString("link", null);
        final String url = "http://psikologi.ridwan.info/images/" + details.getFoto();
        Picasso.with(this).load(url).error(R.drawable.menu_icon_user)
                .into(avatar);
        recyclerViewlayanan = findViewById(R.id.recyclerViewlayanan);
        recyclerViewlayanan.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences sharedPref = getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
        biodata_nama = findViewById(R.id.biodata_nama);
        biodata_nama.getEditText().setText(" "+details.getName());
        biodata_gelar = findViewById(R.id.biodata_gelar);
        biodata_gelar.getEditText().setText(" "+" "+sharedPref.getString("gelar", null));
        biodata_NIK = findViewById(R.id.biodata_NIK);
        biodata_NIK.getEditText().setText(" "+details.getNik());
        biodata_TL = findViewById(R.id.biodata_TL);
      //  biodata_TL.getEditText().setText(" "+details.getTanggal_lahir());
        biodata_sipp = findViewById(R.id.biodata_sipp);
        biodata_sipp.getEditText().setText(" "+sharedPref.getString("no_sipp", null));
        biodata_alamat = findViewById(R.id.biodata_alamat);
        biodata_alamat.getEditText().setText(" "+details.getAlamat());

        rg_jk = findViewById(R.id.rg_jk);
        r_lakilaki = findViewById(R.id.r_lakilaki);
        r_perempuan = findViewById(R.id.r_perempuan);


    }

    public void r_lakilaki(View view) {
        genderKlien = "Laki-laki";
    }

    public void r_perempuan(View view) {
        genderKlien = "Perempuan";
    }

    public void getListLayanan(){
        SharedPreferences sharedPref = getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);

        int idPsikolog = sharedPref.getInt("idPsikolog", -1);
        retrofit2.Call<Response_Layanan> call = RetrofitClient.getInstance().getApi().getLayanan(token);
        call.enqueue(new Callback<Response_Layanan>() {
            @Override
            public void onResponse(retrofit2.Call<Response_Layanan> call, Response<Response_Layanan> response) {
                layananList = response.body().getLayanan();
                adapter = new LayananAdapter( UbahBiodata_Edit.this, layananList);
                recyclerViewlayanan.setAdapter(adapter);
            }

            @Override
            public void onFailure(retrofit2.Call<Response_Layanan> call, Throwable t) {
                Toast.makeText(UbahBiodata_Edit.this, "Gagal", Toast.LENGTH_LONG).show();
            }
        });

        Button dateTTL = (Button) findViewById(R.id.dateTTL);
        dateTTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(UbahBiodata_Edit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        biodata_TL.getEditText().setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
                    }
                }, year, month, day);
                Calendar calMax = Calendar.getInstance();
                calMax.add(Calendar.YEAR, -3);
                long calMaxMillis = calMax.getTimeInMillis();
                datePickerDialog.getDatePicker().setMaxDate(calMaxMillis);
                datePickerDialog.show();
            }
        });

    }

    public void simpan(){

        String name = biodata_nama.getEditText().getText().toString().trim();
        String nik = biodata_NIK.getEditText().getText().toString().trim();
        String gelar = biodata_gelar.getEditText().getText().toString().trim();
        String alamat = biodata_alamat.getEditText().getText().toString().trim();
        String sipp = biodata_sipp.getEditText().getText().toString().trim();
        String ttl = biodata_TL.getEditText().getText().toString().trim();

        if (name.isEmpty()) {
            biodata_nama.setError("Nama Harus Diisi!");
            biodata_nama.requestFocus();
            return;
        }
        if (nik.isEmpty()) {
            biodata_NIK.setError("NIK Harus Diisi!");
            biodata_NIK.requestFocus();
            return;
        }
        if (ttl.isEmpty()) {
            biodata_TL.setError("Tanggal Harus Diisi!");
            biodata_TL.requestFocus();
            return;
        }
        if (alamat.isEmpty()) {
            biodata_alamat.setError("Alamat Harus Diisi!");
            biodata_alamat.requestFocus();
            return;
        }
        if (sipp.isEmpty()) {
            biodata_sipp.setError("Alamat Harus Diisi!");
            biodata_sipp.requestFocus();
            return;
        }
        if (gelar.isEmpty()) {
            biodata_gelar.setError("Alamat Harus Diisi!");
            biodata_gelar.requestFocus();
            return;
        }
        if (rg_jk.getCheckedRadioButtonId() == 0) {
            r_perempuan.setError("Jenis Kelamin Harus Diisi!");
            rg_jk.requestFocus();
            return;
        }
        loading = ProgressDialog.show(UbahBiodata_Edit.this, null, "Sedang Memproses...", true, false);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().updateBiodata(token,
                details.getUserId(), name, gelar,nik,alamat, sipp, ttl,genderKlien);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    loadDetails();
                    loading.dismiss();
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Terjadi Kesalahan Sistem", Toast.LENGTH_LONG)
                            .show();
                    loading.dismiss();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        "Periksa Kembali Koneksi Internet Anda", Toast.LENGTH_LONG)
                        .show();
                loading.dismiss();
            }
        });
    }
    public void getChoosen(){
        final ArrayList<Integer> mId = new ArrayList<>();
        final ArrayList<String> mNames = new ArrayList<>();
        recyclerViewChoosenLayanan = findViewById(R.id.recyclerViewChoosenLayanan);
        recyclerViewChoosenLayanan.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        adapterChoosen = new ChoosenLayananAdapter(mId, mNames );

        recyclerViewChoosenLayanan.setLayoutManager(mLayoutManager);
        recyclerViewChoosenLayanan.setAdapter(adapterChoosen);


    }
    public void loadChoosen(){
        SharedPreferences sharedPreferences = getSharedPreferences("Array Layanan", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("layanan list", null);
        Type type = new TypeToken<ArrayList<ArrayLayananModel>>(){}.getType();
        arrayLayananModels = gson.fromJson(json, type);
        if(arrayLayananModels == null){
            arrayLayananModels= new ArrayList<>();
        }
    }

    public void setGender() {
        if(details.getJenis_kelamin() == null){
            r_lakilaki.setChecked(false);
            r_perempuan.setChecked(false);
            return;
        }
        if (details.getJenis_kelamin().equals("Perempuan")) {
            r_lakilaki.setChecked(false);
            r_perempuan.setChecked(true);
            genderKlien = "Perempuan";
            return;
        }
        if (details.getJenis_kelamin().equals("Laki-laki")){
            r_lakilaki.setChecked(true);
            r_perempuan.setChecked(false);
            genderKlien = "Laki-laki";
            return;
        }
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
                    SharedPrefManager.getInstance(UbahBiodata_Edit.this).saveDetails(response.body().getDetails());
                    Intent simpan = new Intent(UbahBiodata_Edit.this, MainActivity.class);
                    Toast.makeText(getApplicationContext(), "Biodata Anda Telah Tersimpan", Toast.LENGTH_LONG)
                            .show();
                    simpan.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(simpan);
                } else {
                    Toast.makeText(UbahBiodata_Edit.this, "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Response_Details> call, Throwable t) {
                Toast.makeText(UbahBiodata_Edit.this, "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        getListLayanan();
//        loadChoosen();
//        getChoosen();
        setGender();
      //  loadDetails();
    }

}
