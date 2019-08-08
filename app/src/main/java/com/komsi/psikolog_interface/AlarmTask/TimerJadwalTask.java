package com.komsi.psikolog_interface.AlarmTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.Menu.PermintaanKlien;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.Reponse_updateJadwal;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerJadwalTask extends BroadcastReceiver {
    private Context mCtx;


    @Override
    public void onReceive(final Context context, Intent intent) {
        User user = SharedPrefManager.getInstance(mCtx).getUser();
        String token = "Bearer " + user.getToken();
        final int idJadwal = intent.getIntExtra("idJadwal", -1);
        int psikologId = intent.getIntExtra("PsikologId", -1);
        int klienId = intent.getIntExtra("KlienId", -1);
        Log.d("alarm manager ", "onBindViewHolder: alarm fired" + idJadwal + psikologId);
        Call<Reponse_updateJadwal> call = RetrofitClient.getInstance().getApi().updateJadwal12(token,
                idJadwal, psikologId, klienId, 12);
        call.enqueue(new Callback<Reponse_updateJadwal>() {
            @Override
            public void onResponse(Call<Reponse_updateJadwal> call, Response<Reponse_updateJadwal> response) {
                Log.d("alarm manager ", "onBindViewHolder: alarm on respon");
                if (response.isSuccessful()) {
                    Log.d("alarm manager ", "onBindViewHolder: alarm succes");
                    Intent intent = new Intent(context, PermintaanKlien.class);
                    context.startActivity(intent);
                } else {
                    Log.d("alarm manager ", "onBindViewHolder: alarm gagal");
                }
            }

            @Override
            public void onFailure(Call<Reponse_updateJadwal> call, Throwable t) {
                Log.d("alarm manager ", "onBindViewHolder: alarm failure 2");
            }
        });
    }

    private void reupload() {


       /* Call<Reponse_updateJadwal> backupCall = RetrofitClient.getInstance().getApi().updateJadwal(token, idJadwal, 12, 12);
        backupCall.enqueue(new Callback<Reponse_updateJadwal>() {
            @Override
            public void onResponse(Call<Reponse_updateJadwal> call, Response<Reponse_updateJadwal> response) {
                if (response.isSuccessful()) {
                    Log.d("alarm manager ", "onBindViewHolder: alarm succes 2");

                    //   Toast.makeText(mCtx, "Permintaan Klien Kedaluarsa", Toast.LENGTH_LONG).show();
//                                Intent intent= new Intent(mCtx, PermintaanKlien.class);
//                                mCtx.startActivity(intent);
                } else {
                    //     Toast.makeText(mCtx, "Gagal Memperbaharui data 2", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Reponse_updateJadwal> call, Throwable t) {
                // Toast.makeText(mCtx, "Terjadi Kesalahan Data 1", Toast.LENGTH_LONG).show();
                Log.d("alarm manager ", "onBindViewHolder: alarm failure 2");


            }
        });
*/
    }
}
