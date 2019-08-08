package com.komsi.psikolog_interface.Adapter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.DetailKonsultasi;
import com.komsi.psikolog_interface.Activities.MainActivity;
import com.komsi.psikolog_interface.AlarmTask.FinishTask;
import com.komsi.psikolog_interface.AlarmTask.NotifTask;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.JadwalModels;
import com.komsi.psikolog_interface.Models.Reponse_updateJadwal;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;
import retrofit2.Response;

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder> {

    private Context mCtx;
    private List<JadwalModels> jadwalList;
    User user = SharedPrefManager.getInstance(mCtx).getUser();
    String token = "Bearer "+user.getToken();


    public JadwalAdapter(Context mCtx, List<JadwalModels> jadwalList) {
        this.mCtx = mCtx;
        this.jadwalList = jadwalList;

    }


    @Override
    public JadwalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.riwayat_konsul_list_layout, parent, false);
        return new JadwalViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final JadwalViewHolder holder, final int position) {
        final JadwalModels jadwal = jadwalList.get(position);
        holder.jenisLayanan.setText(jadwal.getLayanan().getNama());
        holder.namaKlien.setText(jadwal.getKlien().getUser().getName());
        holder.tanggal.setText(jadwal.getTanggal());
        SharedPreferences sharedPrefFCM = mCtx.getSharedPreferences("linkFoto", Context.MODE_PRIVATE);
        String link = sharedPrefFCM.getString("link", null);
        String url = "http://psikologi.ridwan.info/images/" + jadwal.getKlien().getUser().getFoto();
        Picasso.with(mCtx).load(url).error(R.drawable.menu_icon_user).into(holder.avatarKlien);




        String tesTanggal = jadwal.getTanggal();
        final String[] separated = tesTanggal.split(" ");
        final String[] separated2 = separated[1].split("");
        final int year = Integer.parseInt(separated[2]);
        final int day = Integer.parseInt(separated[0]);
        int month1 = Integer.parseInt(separated[1]);
        int month = 0;
        if (month1 < 10) {
            String m = String.valueOf(month1);
            String[] months = m.split("");
            month = Integer.parseInt(months[1]);
        } else {
            month = month1;
        }

        if (month1==1){
            holder.warna.setBackgroundResource(R.color.yellow);
        }
        if (month1==2){
            holder.warna.setBackgroundResource(R.color.yellow2);
        }
        if (month1==3){
            holder.warna.setBackgroundResource(R.color.green);
        }
        if (month1==4){
            holder.warna.setBackgroundResource(R.color.green2);
        }
        if (month1==5){
            holder.warna.setBackgroundResource(R.color.purple);
        }
        if (month1==6){
            holder.warna.setBackgroundResource(R.color.purple2);
        }
        if (month1==7){
            holder.warna.setBackgroundResource(R.color.pink);
        }
        if (month1==8){
            holder.warna.setBackgroundResource(R.color.pink2);
        }
        if (month1==9){
            holder.warna.setBackgroundResource(R.color.gradStop);
        }
        if (month1==10){
            holder.warna.setBackgroundResource(R.color.black);
        }
        if (month1==11){
            holder.warna.setBackgroundResource(R.color.gradStart2);
        }
        if (month1==12){
            holder.warna.setBackgroundResource(R.color.gradStart);
        }

        // set Alarm

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 10);

        Date jadwalDate = calendar.getTime();

        Calendar calNow = Calendar.getInstance();
        calNow.getTime();
        Date nowDate = calNow.getTime();

        long calenderNow = calNow.getTimeInMillis();

        if(jadwal.getStatus().getNama().equals("Terjadwal")){
         //  setAlarm(calendar.getTimeInMillis());
           if(nowDate.after(jadwalDate)){
               holder.finishLayout.setVisibility(View.VISIBLE);

           }
           else{
               holder.finishLayout.setVisibility(View.GONE);

           }
       }
       else {
           holder.finishLayout.setVisibility(View.GONE);
       }

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, year);
        calendar2.set(Calendar.MONTH, month-1);
        calendar2.set(Calendar.DAY_OF_MONTH, day);
        calendar2.set(Calendar.HOUR_OF_DAY, 16);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 10);
        Date finishDate = calendar2.getTime();
        long finishtime= calendar2.getTimeInMillis();

        if(jadwal.getStatus().getNama().equals("Terjadwal")){
            //  setAlarm(calendar.getTimeInMillis());
            if(nowDate.after(finishDate)){

                AlarmManager am = (AlarmManager) mCtx.getSystemService(mCtx.ALARM_SERVICE);
                Intent intent = new Intent(mCtx, FinishTask.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // coba di set flag

                // put extra
                intent.putExtra("idJadwal", jadwal.getId());
                SharedPreferences sharedPref = mCtx.getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
                intent.putExtra("PsikologId", jadwal.getPsikolog_id());
                intent.putExtra("KlienId", jadwal.getKlien_id());
                PendingIntent pi = PendingIntent.getBroadcast(mCtx, jadwal.getId(), intent, 0);
                am.set(AlarmManager.RTC, finishtime, pi);

            }
        }


        holder.bt_selesai.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               SharedPreferences sharedPref = mCtx.getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
               final int psikologId = sharedPref.getInt("idPsikolog", -1);
               holder.loading = ProgressDialog.show(mCtx, null, "Sedang Memuat...", true, false);

               retrofit2.Call<Reponse_updateJadwal> backupCall = RetrofitClient.getInstance().getApi().updateJadwal10(token, jadwal.getId(), psikologId, jadwal.getKlien_id(), 10);
               backupCall.enqueue(new Callback<Reponse_updateJadwal>() {
                   @Override
                   public void onResponse(retrofit2.Call<Reponse_updateJadwal> call, Response<Reponse_updateJadwal> response) {
                       if (response.isSuccessful()){
                           holder.loading.dismiss();
                           Toast.makeText(mCtx, "Pelayanan Konsultasi Telah Selesai", Toast.LENGTH_LONG).show();
                           Intent intent = new Intent(mCtx, MainActivity.class);
                           mCtx.startActivity(intent);
                       }
                       else {
                           Toast.makeText(mCtx, "Gagal Memperbaharui data", Toast.LENGTH_LONG).show();
                           holder.loading.dismiss();

                       }
                   }
                   @Override
                   public void onFailure(retrofit2.Call<Reponse_updateJadwal> call, Throwable t) {
                       Toast.makeText(mCtx, "Terjadi Kesalahan Data", Toast.LENGTH_LONG).show();
                       holder.loading.dismiss();

                   }
               });
           }
       });


        holder.status.setText(jadwal.getStatus().getNama());
        holder.bt_detail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent detail =new  Intent(mCtx, DetailKonsultasi.class);
                if(jadwal.getKlien().getParent_id() == 0){
                    holder.hubungan = "Pendaftar Diri Sendiri";
                }
                else {
                    holder.hubungan = "Didaftarkan Pemilik Akun";
                }
                detail.putExtra("namaKlien", jadwal.getKlien().getUser().getName());
                detail.putExtra("layanan", jadwal.getLayanan().getNama());
                detail.putExtra("psikolog", user.getName());
                detail.putExtra("tanggal", jadwal.getTanggal());
                detail.putExtra("status", jadwal.getStatus().getNama());
                detail.putExtra("sesi", jadwal.getSesi().getNama());
                detail.putExtra("jam", jadwal.getSesi().getJam());
                detail.putExtra("keluhan", jadwal.getKeluhan());
                detail.putExtra("hubungan",holder.hubungan);
                detail.putExtra("foto",jadwal.getKlien().getUser().getFoto());
                detail.putExtra("idJadwal", jadwal.getId());
                detail.putExtra("idsesi", jadwal.getSesi_id());
                detail.putExtra("idklien", jadwal.getKlien_id());
                detail.putExtra("idlayanan", jadwal.getLayanan_id());

                if (jadwal.getRuangan_id() == 0){
                    detail.putExtra("ruangan", "Menunggu Konfirmasi Admin");
                }
                else {

                    detail.putExtra("ruangan", jadwal.getRuangan().getNama());
                }


                detail.putExtra("idSesi", jadwal.getSesi_id());
                detail.putExtra("idKlien", jadwal.getKlien_id());
                detail.putExtra("idLayanan", jadwal.getLayanan_id());


                mCtx.startActivity(detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jadwalList.size();
    }

    class JadwalViewHolder extends RecyclerView.ViewHolder{
        TextView jenisLayanan, namaKlien, tanggal, status;
        Button bt_detail1, bt_selesai;
        String hubungan;
        CircleImageView avatarKlien;
        LinearLayout finishLayout;
        ProgressDialog loading;
        android.support.v7.widget.CardView warna;

        public JadwalViewHolder(View itemView) {
            super(itemView);
            jenisLayanan = itemView.findViewById(R.id.namakonsul);
            namaKlien = itemView.findViewById(R.id.namaklien);
            status = itemView.findViewById(R.id.status);
            tanggal = itemView.findViewById(R.id.tanggalKonsul);
            bt_detail1 = itemView.findViewById(R.id.bt_detail1);
            avatarKlien = itemView.findViewById(R.id.avatarKlien);
            bt_selesai = itemView.findViewById(R.id.bt_selesai);
            finishLayout = itemView.findViewById(R.id.finishLayout);
            warna = itemView.findViewById(R.id.warna);

        }
    }
    private void setAlarm(long time){
        AlarmManager am =(AlarmManager)mCtx.getSystemService(mCtx.ALARM_SERVICE);
        Intent intent = new Intent(mCtx, NotifTask.class);
        PendingIntent pi = PendingIntent.getBroadcast(mCtx,0, intent, 0);
     //   am.set(AlarmManager.RTC, time, pi);


    }
}
