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
import com.simdes.appgo.model.ApbDesaModel;
import com.simdes.appgo.model.PengaduanModel;
import com.simdes.appgo.network.Config;
import com.simdes.appgo.utils.DataHelper;
import com.simdes.appgo.utils.GeneralHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class PengaduanAdapter extends RecyclerView.Adapter<PengaduanAdapter.ViewHolder>{

    public ArrayList<PengaduanModel> rvData;
    public Context context;
    public int poss;

    public PengaduanAdapter(ArrayList<PengaduanModel> inputData, Context context) {
        this.rvData = inputData;
        this.context = context;
        this.poss = poss;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtUser, txtJudul, txtTindakan, txtCatatan;
        public LinearLayout btnItem;
        public ImageView imgGambar,
                imgCekProses, imgCekSetuju, imgCekTolak;

        public ViewHolder(View v) {
            super(v);
            txtUser = v.findViewById(R.id.txtUser);
            txtJudul = v.findViewById(R.id.txtJudul);
            txtTindakan = v.findViewById(R.id.txtTindakan);
            txtCatatan = v.findViewById(R.id.txtCatatan);
            imgGambar = v.findViewById(R.id.imgGambar);
//            imgCekProses = v.findViewById(R.id.imgCekProses);
//            imgCekSetuju = v.findViewById(R.id.imgCekSetuju);
//            imgCekTolak = v.findViewById(R.id.imgCekTolak);
            btnItem = v.findViewById(R.id.btnItem);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pengaduan_adapter, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txtJudul.setText(rvData.get(position).deskripsi + "\n" + GeneralHelper.formatDate(rvData.get(position).created_at));
        holder.txtTindakan.setText(rvData.get(position).tindakan);
        holder.txtCatatan.setText(rvData.get(position).catatan);

//        if (rvData.get(position).tindakan.equals("Disetujui")) {
//            holder.imgCekSetuju.setVisibility(View.VISIBLE);
//            holder.imgCekTolak.setVisibility(View.GONE);
//            holder.imgCekProses.setVisibility(View.GONE);
//        }else if(rvData.get(position).tindakan.equals("Tidak Disetujui")){
//            holder.imgCekSetuju.setVisibility(View.GONE);
//            holder.imgCekTolak.setVisibility(View.VISIBLE);
//            holder.imgCekProses.setVisibility(View.GONE);
//        }else {
//            holder.imgCekSetuju.setVisibility(View.GONE);
//            holder.imgCekTolak.setVisibility(View.GONE);
//            holder.imgCekProses.setVisibility(View.VISIBLE);
//        }

        if (rvData.get(position).gambar != null) {
            if (!rvData.get(position).gambar.equals("")) {
                Glide.with(context)
                        .load(Config.PATH_IMG + rvData.get(position).gambar)
                        .apply(new RequestOptions().placeholder(R.drawable.bg_lay_register).error(R.drawable.bg_lay_register))
                        .into(holder.imgGambar);
            } else {
                holder.imgGambar.setVisibility(View.GONE);
            }
        } else {
            holder.imgGambar.setVisibility(View.GONE);
        }

        holder.btnItem.setOnClickListener(view -> {
//            DataHelper.data_pengaduan = rvData.get(position);
//            Intent u = new Intent(context.getApplicationContext(), DetailApbDesaActivity.class);
//            u.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(u);
        });
    }

    @Override
    public int getItemCount() {
        return rvData != null ? rvData.size() : 0;
    }
}
