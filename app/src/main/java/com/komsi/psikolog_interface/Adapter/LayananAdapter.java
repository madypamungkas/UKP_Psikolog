package com.komsi.psikolog_interface.Adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.komsi.psikolog_interface.AlarmTask.TimerJadwalTask;
import com.komsi.psikolog_interface.Models.ArrayLayananModel;
import com.komsi.psikolog_interface.Models.LayananModel;
import com.komsi.psikolog_interface.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class LayananAdapter extends RecyclerView.Adapter<LayananAdapter.LayananViewHolder> {
    private Context mCtx;
    private List<LayananModel> layananList;
    ArrayList<ArrayLayananModel> arrayLayananModels;


    public LayananAdapter(Context mCtx, List<LayananModel> layananList) {
        this.mCtx = mCtx;
        this.layananList = layananList;
    }

    @Override
    public LayananViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.layanan_list_layout, parent, false);
        return new LayananAdapter.LayananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LayananViewHolder holder, int position) {
        final LayananModel layananModel = layananList.get(position);
        holder.namaLayanan.setText(layananModel.getNama());
        Calendar calnow = Calendar.getInstance();
        final long timenow = calnow.getTimeInMillis();

        holder.checkLayanan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                SharedPreferences sharedPref = mCtx.getSharedPreferences("Array Layanan", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                String a = sharedPref.getString("idLayanan", " ");
                String nama =sharedPref.getString("namaLayanan", null);

                editor.putString("idLayanan", a + "," + String.valueOf(layananModel.getId()));
                editor.putString("namaLayanan", nama + "," + layananModel.getNama());
                editor.apply();



                Log.d("layanan", String.valueOf(a));
                Log.d("layanan", String.valueOf(nama));

//                AlarmManager am = (AlarmManager) mCtx.getSystemService(mCtx.ALARM_SERVICE);
//                Intent intent = new Intent(mCtx, TimerJadwalTask.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // coba di set flag
//
//                // put extra
//                intent.putExtra("idLayanan", layananModel.getId());
//                PendingIntent pi = PendingIntent.getBroadcast(mCtx, layananModel.getId(), intent, 0);
//
//                am.set(AlarmManager.RTC, timenow + 1000, pi);
//                Toast.makeText(mCtx, "saving Set ", Toast.LENGTH_SHORT).show();
//
            }
        });

    }


    @Override
    public int getItemCount() {

        return layananList.size();
    }

    class LayananViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkLayanan;
        TextView namaLayanan, choosen;

        public LayananViewHolder(View itemView) {
            super(itemView);
            checkLayanan = itemView.findViewById(R.id.checkLayanan);
            namaLayanan = itemView.findViewById(R.id.namaLayanan);
            choosen = itemView.findViewById(R.id.choosen);


        }
    }


//            // insert into array
//            arrayLayananModels.add(new ArrayLayananModel(layananModel.getId()));
//
//
//            // save data
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            Gson gson = new Gson();
//            String json = gson.toJson(arrayLayananModels);
//            editor.putString("layanan list", json);
//            editor.apply();
//            // load data


  /* Gson gson = new Gson();
        String json = sharedPreferences.getString("layanan list", null);

        Type type = new TypeToken<ArrayList<ArrayLayananModel>>() {
        }.getType();
        arrayLayananModels = gson.fromJson(json, type);
        for (ArrayLayananModel arrayLayananModel : arrayLayananModels) {
            holder.choosen.setText((CharSequence) arrayLayananModel);


        }*/
}
