package com.komsi.psikolog_interface.Adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.DetailKonsultasi_Klien;
import com.komsi.psikolog_interface.Activities.Menu.PermintaanKlien;
import com.komsi.psikolog_interface.AlarmTask.NotifTask;
import com.komsi.psikolog_interface.AlarmTask.TimerJadwalTask;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.Models.JadwalModels;
import com.komsi.psikolog_interface.Models.Psikolog;
import com.komsi.psikolog_interface.Models.Reponse_updateJadwal;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PermintaanKlienAdapter extends RecyclerView.Adapter<PermintaanKlienAdapter.PermintaanKlienViewHolder> {
    private Context mCtx;
    private List<JadwalModels> jadwalList;
    User user = SharedPrefManager.getInstance(mCtx).getUser();
    String token = "Bearer " + user.getToken();
    Details details = SharedPrefManager.getInstance(mCtx).getDetails();
    Psikolog psikolog = SharedPrefManager.getInstance(mCtx).getPsikolog();
    Runnable runnable;
    Handler handler = new Handler();

    public PermintaanKlienAdapter(Context mCtx, List<JadwalModels> jadwalList) {
        this.mCtx = mCtx;
        this.jadwalList = jadwalList;
    }

    @Override
    public PermintaanKlienViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.permintaan_klien_list_layout, parent, false);
        return new PermintaanKlienAdapter.PermintaanKlienViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final PermintaanKlienViewHolder holder, int position) {
        final JadwalModels jadwal = jadwalList.get(position);
        holder.jenisLayanan.setText(jadwal.getLayanan().getNama());
        holder.namaKlien.setText("Nama Klien : " + jadwal.getKlien().getUser().getName());
        holder.tanggal.setText(jadwal.getTanggal());
        SharedPreferences sharedPrefFCM = mCtx.getSharedPreferences("linkFoto", Context.MODE_PRIVATE);
        String link = sharedPrefFCM.getString("link", null);
        String url = "http://psikologi.ridwan.info/images/" + jadwal.getKlien().getUser().getFoto();
        Picasso.with(mCtx).load(url).error(R.drawable.menu_icon_user).into(holder.avatarKlien);


        String[] waktu = jadwal.getUpdated_at().split(" ");
        String[] separateJam = waktu[1].split(":");
        final int jam = Integer.parseInt(separateJam[0]);
        final int menit = Integer.parseInt(separateJam[1]);
        final int detik = Integer.parseInt(separateJam[2]);

        Calendar cal = Calendar.getInstance();
        cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, jam);
        cal.set(Calendar.MINUTE, menit);
        cal.set(Calendar.SECOND, detik);

        cal.add(Calendar.MINUTE, 9);
        cal.add(Calendar.SECOND, 20);
        Date datelimit = cal.getTime();
        long dateAlarm = cal.getTimeInMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date newDate = datelimit;
        final String dateTime = dateFormat.format(datelimit);
        // holder.timeLimit.setText(dateTime);

        Calendar calnow = Calendar.getInstance();
        calnow.getTime();
        Date dateskrg = calnow.getTime();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
        Date newDate2 = dateskrg;
        long datenow = calnow.getTimeInMillis();
        // alarm Manager
        AlarmManager am = (AlarmManager) mCtx.getSystemService(mCtx.ALARM_SERVICE);
        Intent intent = new Intent(mCtx, TimerJadwalTask.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // put extra
        intent.putExtra("idJadwal", jadwal.getId());
        SharedPreferences sharedPref = mCtx.getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
        intent.putExtra("PsikologId", sharedPref.getInt("idPsikolog", -1));
        intent.putExtra("KlienId", jadwal.getKlien_id());

        PendingIntent pi = PendingIntent.getBroadcast(mCtx, jadwal.getId(), intent, 0);
        //      am.set(AlarmManager.RTC, dateAlarm, pi);

        //   Toast.makeText(mCtx, "Notif Set ", Toast.LENGTH_SHORT).show();
        Log.d("alarm manager ", "onBindViewHolder: alarm manager" + jadwal.getId());
        // intent.removeExtra("idJadwal");

        if (dateAlarm > datenow) {
            CountDownTimer cd = new CountDownTimer(dateAlarm - datenow, 1000) {
                @Override
                public void onTick(long l) {
                    holder.timeLimit.setText(dateTime);
                    int hours = (int) (((l / 1000) / 3600) % 24);
                    int minutes = (int) ((l / 1000) / 60);
                    int seconds = (int) ((l / 1000) % 60);
                    String limitFormat = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                    holder.timeLimit2.setText("00:" + limitFormat);
                }

                @Override
                public void onFinish() {
                    if (jadwal.getId() == 5) {
                        holder.timeLimit.setText("Kedaluarsa");
                        holder.timeLimit2.setVisibility(View.GONE);

                        final int idJadwal = jadwal.getId();
                        int psikologId = jadwal.getPsikolog_id();
                        Log.d("alarm manager ", "onBindViewHolder: alarm fired" + idJadwal + psikologId);
//                        Call<Reponse_updateJadwal> call = RetrofitClient.getInstance().getApi().updateJadwal12(token, idJadwal, psikologId, jadwal.getKlien_id(), 12);
//                        call.enqueue(new Callback<Reponse_updateJadwal>() {
//                            @Override
//                            public void onResponse(Call<Reponse_updateJadwal> call, Response<Reponse_updateJadwal> response) {
//                                Log.d("alarm manager ", "onBindViewHolder: alarm on respon");
//
//                                if (response.isSuccessful()) {
//                                    Log.d("alarm manager ", "onBindViewHolder: alarm succes");
//
////                    Toast.makeText(mCtx, "Permintaan Klien Kedaluarsa", Toast.LENGTH_LONG).show();
//                                    Intent intent = new Intent(mCtx, PermintaanKlien.class);
//                                    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    mCtx.startActivity(intent);
//                                } else {
//                                    holder.bt_konfirmasi.setEnabled(false);
//                                    holder.timeLimit.setText("Kedaluarsa");
//                                    holder.timeLimit2.setVisibility(View.GONE);
//                                    Log.d("alarm manager ", "onBindViewHolder: alarm failure 2");
//
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<Reponse_updateJadwal> call, Throwable t) {
//
////                Toast.makeText(mCtx, "Terjadi Kesalahan Data 2", Toast.LENGTH_LONG).show();
//                                holder.bt_konfirmasi.setEnabled(false);
//                                holder.timeLimit.setText("Kedaluarsa");
//                                holder.timeLimit2.setVisibility(View.GONE);
//                                Log.d("alarm manager ", "onBindViewHolder: alarm failure 2");
//
//                            }
//                        });

                    } else {
                        holder.bt_konfirmasi.setEnabled(false);
                        holder.timeLimit.setText("Kedaluarsa");
                        holder.timeLimit2.setVisibility(View.GONE);
                    }
                }
            }.start();

        } else {
            holder.bt_konfirmasi.setEnabled(false);
            holder.timeLimit.setText("Kedaluarsa");
            holder.timeLimit2.setVisibility(View.GONE);
        }


        holder.bt_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jadwal.getKlien().getParent_id() == 0) {
                    holder.hubungan = "Pendaftar Diri Sendiri";
                } else {
                    holder.hubungan = "Didaftarkan Pemilik Akun";
                }
                Intent detail = new Intent(mCtx, DetailKonsultasi_Klien.class);
                detail.putExtra("idJadwal", jadwal.getId());
                detail.putExtra("idsesi", jadwal.getSesi_id());
                detail.putExtra("idklien", jadwal.getKlien_id());
                detail.putExtra("idlayanan", jadwal.getLayanan_id());
                detail.putExtra("namaKlien", jadwal.getKlien().getUser().getName());
                detail.putExtra("layanan", jadwal.getLayanan().getNama());
                detail.putExtra("psikolog", user.getName());
                detail.putExtra("tanggal", jadwal.getTanggal());
                detail.putExtra("status", jadwal.getStatus().getNama());
                detail.putExtra("sesi", jadwal.getSesi().getNama());
                detail.putExtra("jam", jadwal.getSesi().getJam());
                detail.putExtra("keluhan", jadwal.getKeluhan());
                detail.putExtra("hubungan", holder.hubungan);
                detail.putExtra("foto", jadwal.getKlien().getUser().getFoto());

                if (jadwal.getRuangan_id() == 0) {
                    detail.putExtra("ruangan", "Menunggu Konfirmasi Admin");
                } else {
                    detail.putExtra("ruangan", jadwal.getRuangan().getNama());
                }
                mCtx.startActivity(detail);
            }
        });

        if (holder.buttonLayout.getVisibility() == View.GONE) {
            holder.permintaanKlien.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.buttonLayout.setVisibility(View.VISIBLE);
                }
            });
        } else if (holder.buttonLayout.getVisibility() == View.VISIBLE) {
            holder.permintaanKlien.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.buttonLayout.setVisibility(View.GONE);
                }
            });
        }
        holder.bt_tolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.token = "Bearer " + user.getToken();
                SharedPreferences sharedPref = mCtx.getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
                final int psikologId = sharedPref.getInt("idPsikolog", -1);
                Call<Reponse_updateJadwal> call = RetrofitClient.getInstance().getApi().updateJadwal12(holder.token, jadwal.getId(), psikologId, jadwal.getKlien_id(), 12);
                call.enqueue(new Callback<Reponse_updateJadwal>() {
                    @Override
                    public void onResponse(Call<Reponse_updateJadwal> call, Response<Reponse_updateJadwal> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(mCtx, "Permintaan Klien Ditolak", Toast.LENGTH_LONG).show();

                            Intent sukses = new Intent(mCtx, PermintaanKlien.class);
                            mCtx.startActivity(sukses);
                        } else {
                            Toast.makeText(mCtx, "Permintaa Gagal", Toast.LENGTH_LONG).show();
                            // Toast.makeText(mCtx, psikologId + " "+ holder.token, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Reponse_updateJadwal> call, Throwable t) {
                        Toast.makeText(mCtx, "Terjadi Kesalahan Data", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        holder.bt_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.hubungan = "Klien";

                Intent detail = new Intent(mCtx, DetailKonsultasi_Klien.class);
                detail.putExtra("idJadwal", jadwal.getId());
                detail.putExtra("idsesi", jadwal.getSesi_id());
                detail.putExtra("idklien", jadwal.getKlien_id());
                detail.putExtra("idlayanan", jadwal.getLayanan_id());

                detail.putExtra("namaKlien", jadwal.getKlien().getUser().getName());
                detail.putExtra("layanan", jadwal.getLayanan().getNama());
                detail.putExtra("psikolog", user.getName());
                detail.putExtra("tanggal", jadwal.getTanggal());
                detail.putExtra("status", jadwal.getStatus().getNama());
                detail.putExtra("sesi", jadwal.getSesi().getNama());
                detail.putExtra("jam", jadwal.getSesi().getJam());
                detail.putExtra("keluhan", jadwal.getKeluhan());
                detail.putExtra("hubungan", holder.hubungan);
                if (jadwal.getRuangan_id() == 0) {
                    detail.putExtra("ruangan", "Menunggu Konfirmasi Admin");
                } else {
                    detail.putExtra("ruangan", jadwal.getRuangan().getNama());
                }
                mCtx.startActivity(detail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return jadwalList.size();
    }

    public class PermintaanKlienViewHolder extends RecyclerView.ViewHolder {
        TextView jenisLayanan, namaKlien, tanggal, status, timeLimit, timeLimit2;
        Button bt_tolak, bt_konfirmasi, bt_detail;
        LinearLayout permintaanKlien, buttonLayout;
        private String token, newTime;
        String hubungan;
        int idPsikolog, idJadwal, idStatus;
        int mSelectedItem;
        CircleImageView avatarKlien;


        public PermintaanKlienViewHolder(View itemView) {
            super(itemView);
            permintaanKlien = itemView.findViewById(R.id.permintaanKlien);
            timeLimit = itemView.findViewById(R.id.timeLimit);
            timeLimit2 = itemView.findViewById(R.id.timeLimit2);
            buttonLayout = itemView.findViewById(R.id.buttonLayout);
            jenisLayanan = itemView.findViewById(R.id.namakonsul);
            namaKlien = itemView.findViewById(R.id.namaklien);
            status = itemView.findViewById(R.id.status);
            tanggal = itemView.findViewById(R.id.tanggalKonsul);
            bt_tolak = itemView.findViewById(R.id.bt_tolak);
            bt_konfirmasi = itemView.findViewById(R.id.bt_konfirmasi);
            bt_detail = itemView.findViewById(R.id.bt_detail);
            avatarKlien = itemView.findViewById(R.id.avatarKlien);

            mSelectedItem = getAdapterPosition();

        }
    }


}
