package com.komsi.psikolog_interface.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.komsi.psikolog_interface.R;

import java.util.ArrayList;

public class ChoosenLayananAdapter extends RecyclerView.Adapter<ChoosenLayananAdapter.ChoosenLayananViewHolder>{
    private ArrayList<Integer> mId = new ArrayList<>();
    private ArrayList<String> mName = new ArrayList<>();
    private Context mCtx;

    public ChoosenLayananAdapter(ArrayList<Integer> mId, ArrayList<String> mName) {
        this.mId = mId;
        this.mName = mName;
    }

    @Override
    public ChoosenLayananViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.choosen_layanan_list_layout, parent, false);
        return new ChoosenLayananAdapter.ChoosenLayananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChoosenLayananViewHolder holder, int position) {

    holder.idLayanan.setText(mName.get(position));
    }

    @Override
    public int getItemCount() {
        return mId.size();
    }

    class ChoosenLayananViewHolder extends RecyclerView.ViewHolder{
        TextView idLayanan;

        public ChoosenLayananViewHolder(View itemView) {
            super(itemView);
            idLayanan = itemView.findViewById(R.id.idLayanan);
        }
    }
}
