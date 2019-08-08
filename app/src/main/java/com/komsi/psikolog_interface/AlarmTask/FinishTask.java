package com.komsi.psikolog_interface.AlarmTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.komsi.psikolog_interface.Activities.Menu.PermintaanKlien;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.Reponse_updateJadwal;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinishTask extends BroadcastReceiver {
    private Context mCtx;

    @Override
    public void onReceive(final Context context, Intent intent) {
        User user = SharedPrefManager.getInstance(mCtx).getUser();
        String token = "Bearer " + user.getToken();
        final int idJadwal = intent.getIntExtra("idJadwal", -1);
        int psikologId = intent.getIntExtra("PsikologId", -1);
        int klienId = intent.getIntExtra("KlienId", -1);
        Log.d("alarm manager ", "onBindViewHolder: alarm fired" + idJadwal + psikologId);
        Call<Reponse_updateJadwal> call = RetrofitClient.getInstance().getApi().updateJadwal10(token,
                idJadwal, psikologId, klienId, 10);
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
}
