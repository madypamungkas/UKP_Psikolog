package com.komsi.psikolog_interface.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.komsi.psikolog_interface.Models.JadwalModels;
import com.komsi.psikolog_interface.Models.LayananModel;
import com.komsi.psikolog_interface.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LayananPsikologAdapter extends RecyclerView.Adapter<LayananPsikologAdapter.LayananPsikologViewHolder> {
    private Context mCtx;
    private List<LayananModel> layananList;

    public LayananPsikologAdapter(Context mCtx, List<LayananModel> layananList) {
        this.mCtx = mCtx;
        this.layananList = layananList;
    }

    @Override
    public LayananPsikologViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.layanan_psikolog_list_layout, parent, false);
        return new LayananPsikologAdapter.LayananPsikologViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LayananPsikologViewHolder holder, int position) {
        final LayananModel layananModel = layananList.get(position);
        holder.namaLayanan.setText(layananModel.getNama());
        holder.deskripsiLayanan.setText(layananModel.getDeskripsi());
        Picasso.with(mCtx).load("http://psikologi.ridwan.info/images/"+layananModel.getFoto()).error(R.drawable.konfirmasi)
                .into(holder.check);



    }

    @Override
    public int getItemCount() {
        return layananList.size();
    }

    class LayananPsikologViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkLayanan;
        TextView namaLayanan, deskripsiLayanan;
        ImageView check;

        public LayananPsikologViewHolder(View itemView) {
            super(itemView);
            checkLayanan = itemView.findViewById(R.id.checkLayanan);
            namaLayanan = itemView.findViewById(R.id.namaLayanan);
            deskripsiLayanan = itemView.findViewById(R.id.deskripsiLayanan);
            check = itemView.findViewById(R.id.check);

        }
    }
}
