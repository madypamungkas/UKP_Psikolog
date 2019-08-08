package com.komsi.psikolog_interface.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.komsi.psikolog_interface.Activities.DetailKonsultasi;
import com.komsi.psikolog_interface.Activities.DetailKonsultasi_Klien;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.JadwalModels;
import com.komsi.psikolog_interface.Models.Reponse_updateJadwal;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KonsultasiAdapter extends RecyclerView.Adapter<KonsultasiAdapter.KonsultasiViewHolder> {
    private Context mCtx;
    private List<JadwalModels> jadwalList;
    User user = SharedPrefManager.getInstance(mCtx).getUser();


    public KonsultasiAdapter(Context mCtx, List<JadwalModels> jadwalList) {
        this.mCtx = mCtx;
        this.jadwalList = jadwalList;
    }

    @Override
    public KonsultasiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.cari_klien_list_layout, parent, false);
        return new KonsultasiAdapter.KonsultasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final KonsultasiViewHolder holder, int position) {
        final JadwalModels jadwal = jadwalList.get(position);
        holder.nama_konsul.setText(jadwal.getLayanan().getNama());
        holder.nama_klien.setText(jadwal.getKlien().getUser().getName());
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
        cal.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, jam, menit, detik);
        cal.add(Calendar.MINUTE, 10);
        Date date = cal.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date newDate = date;
        String dateTime = dateFormat.format(date);
        holder.timeLimit.setText(" - ");

        if(Calendar.getInstance().getTime() == date ){

        }
        else {


        }


        holder.detail_klien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.buttonPanel.setVisibility(View.VISIBLE);

            }
        });


        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(jadwal.getKlien().getParent_id() == 0){
                    holder.hubungan = "Pendaftar Diri Sendiri";
                }
                else {
                    holder.hubungan = "Didaftarkan Pemilik Akun";
                }
                Intent detail =new  Intent(mCtx, DetailKonsultasi_Klien.class);
                detail.putExtra("idJadwal", jadwal.getId());
                detail.putExtra("namaKlien", jadwal.getKlien().getUser().getName());
                detail.putExtra("layanan", jadwal.getLayanan().getNama());
                detail.putExtra("psikolog", jadwal.getStatus().getNama());
                detail.putExtra("tanggal", jadwal.getTanggal());
                detail.putExtra("status", jadwal.getStatus().getNama());
                detail.putExtra("sesi", jadwal.getSesi().getNama());
                detail.putExtra("jam", jadwal.getSesi().getJam());
                detail.putExtra("keluhan", jadwal.getKeluhan());
                detail.putExtra("hubungan", holder.hubungan);
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
        holder.konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    /*    if(jadwal.getKlien().get >= 1){
            holder.detail_klien.setBackgroundColor(Integer.parseInt("#23A7D8"));
        }
        else {
            holder.detail_klien.setBackgroundColor(Integer.parseInt("#0066CC"));
        }*/
    }

    @Override
    public int getItemCount() {
        return jadwalList.size();
    }

    public class KonsultasiViewHolder extends RecyclerView.ViewHolder{

        TextView nama_konsul, nama_klien, timeLimit;
        Button detail, konfirmasi;
        LinearLayout detail_klien, buttonPanel;
        String hubungan;
        private String token;
        int idPsikolog, idJadwal, idStatus;
        CircleImageView avatarKlien;


        public KonsultasiViewHolder(View itemView) {
            super(itemView);
            nama_klien = (TextView) itemView.findViewById(R.id.nama_klien);
            nama_konsul = (TextView) itemView.findViewById(R.id.nama_konsul);
            timeLimit = itemView.findViewById(R.id.timeLimit);
            detail = (Button) itemView.findViewById(R.id.detail);
            konfirmasi = (Button) itemView.findViewById(R.id.konfirmasi);
            detail_klien = (LinearLayout) itemView.findViewById(R.id.detail_klien);
            buttonPanel = (LinearLayout) itemView.findViewById(R.id.buttonPanel);
            avatarKlien = itemView.findViewById(R.id.avatarKlien);
            }
    }
}
