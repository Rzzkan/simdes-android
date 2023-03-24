package com.simdes.appgo.adapter;

import android.annotation.SuppressLint;
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
import com.simdes.appgo.DetailApbDesaActivity;
import com.simdes.appgo.R;
import com.simdes.appgo.UploadSyaratPengajuanSuratActivity;
import com.simdes.appgo.model.ApbDesaModel;
import com.simdes.appgo.model.SyaratPengajuanSuratModel;
import com.simdes.appgo.network.Config;
import com.simdes.appgo.utils.DataHelper;

import java.util.ArrayList;

public class SyaratPengajuanSuratAdapter extends RecyclerView.Adapter<SyaratPengajuanSuratAdapter.ViewHolder>{

    public ArrayList<SyaratPengajuanSuratModel> rvData;
    public Context context;
    public int poss;

    public SyaratPengajuanSuratAdapter(ArrayList<SyaratPengajuanSuratModel> inputData, Context context) {
        this.rvData = inputData;
        this.context = context;
        this.poss = poss;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtJudul;
        public LinearLayout btnItem;
        public ImageView imgBerkas;

        public ViewHolder(View v) {
            super(v);
            txtJudul = v.findViewById(R.id.txtJudul);
            imgBerkas = v.findViewById(R.id.imgBerkas);
            btnItem = v.findViewById(R.id.btnItem);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.syarat_pengajuan_surat_adapter, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txtJudul.setText(rvData.get(position).syarat.nama);

        Glide.with(context)
                .load(Config.PATH_IMG + rvData.get(position).berkas)
                .apply(new RequestOptions().placeholder(R.drawable.no_image_available).error(R.drawable.no_image_available))
                .into(holder.imgBerkas);

        holder.btnItem.setOnClickListener(view -> {
            DataHelper.data_syarat_surat_user = rvData.get(position);
            Intent u = new Intent(context.getApplicationContext(), UploadSyaratPengajuanSuratActivity.class);
            u.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(u);
        });
    }

    @Override
    public int getItemCount() {
        return rvData != null ? rvData.size() : 0;
    }
}
