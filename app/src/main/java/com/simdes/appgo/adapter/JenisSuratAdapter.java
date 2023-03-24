package com.simdes.appgo.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.simdes.appgo.DetailPengumumanActivity;
import com.simdes.appgo.PengajuanSuratActivity;
import com.simdes.appgo.R;
import com.simdes.appgo.model.JenisSuratModel;
import com.simdes.appgo.model.PengumumanModel;
import com.simdes.appgo.network.Config;
import com.simdes.appgo.utils.DataHelper;

import java.util.ArrayList;

public class JenisSuratAdapter extends RecyclerView.Adapter<JenisSuratAdapter.ViewHolder>{

    public ArrayList<JenisSuratModel> rvData;
    public Context context;
    public int poss;

    public JenisSuratAdapter(ArrayList<JenisSuratModel> inputData, Context context) {
        this.rvData = inputData;
        this.context = context;
        this.poss = poss;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNamaSurat;
        public LinearLayout btnItem;

        public ViewHolder(View v) {
            super(v);
            txtNamaSurat = v.findViewById(R.id.txtNamaSurat);
            btnItem = v.findViewById(R.id.btnItem);
        }
    }

//    PengajuanSuratActivity pengajuanSuratActivity;

//    JenisSuratAdapter (Activity activity){
//        pengajuanSuratActivity = (PengajuanSuratActivity) activity;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jenis_surat, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txtNamaSurat.setText(rvData.get(position).nama);

        if(Integer.parseInt(rvData.get(position).id) == DataHelper.id_jenis_surat){
            holder.btnItem.setBackgroundColor(context.getResources().getColor(R.color.gray_e));
        }else {
            holder.btnItem.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        holder.btnItem.setOnClickListener(view -> {
            DataHelper.id_jenis_surat = Integer.parseInt(rvData.get(position).id);
            ((PengajuanSuratActivity) context).initRVJenisSurat(rvData);
        });
    }

    @Override
    public int getItemCount() {
        return rvData != null ? rvData.size() : 0;
    }
}
