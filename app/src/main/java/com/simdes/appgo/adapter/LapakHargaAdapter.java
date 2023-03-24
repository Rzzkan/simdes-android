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
import com.simdes.appgo.DetailBeritaActivity;
import com.simdes.appgo.DetailLapakDesaActivity;
import com.simdes.appgo.LapakDesaActivity;
import com.simdes.appgo.R;
import com.simdes.appgo.model.BeritaModel;
import com.simdes.appgo.model.LapakDesaModel;
import com.simdes.appgo.network.Config;
import com.simdes.appgo.utils.DataHelper;
import com.simdes.appgo.utils.GeneralHelper;

import java.util.ArrayList;

public class LapakHargaAdapter extends RecyclerView.Adapter<LapakHargaAdapter.ViewHolder>{

    public ArrayList<LapakDesaModel> rvData;
    public Context context;
    public int poss;

    public LapakHargaAdapter(ArrayList<LapakDesaModel> inputData, Context context) {
        this.rvData = inputData;
        this.context = context;
        this.poss = poss;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtUser, txtJudul, txtHarga;
        public ImageView imgGambar;
        public LinearLayout btnItem;

        public ViewHolder(View v) {
            super(v);
            txtUser = v.findViewById(R.id.txtUser);
            txtJudul = v.findViewById(R.id.txtJudul);
            txtHarga = v.findViewById(R.id.txtHarga);
            imgGambar = v.findViewById(R.id.imgGambar);
            btnItem = v.findViewById(R.id.btnItem);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lapak_desa_adapter, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txtJudul.setText(rvData.get(position).judul);
        holder.txtHarga.setText(GeneralHelper.formatBalance(rvData.get(position).harga));
        holder.txtUser.setText("Oleh: " + rvData.get(position).user.name);

        Glide.with(context)
                .load(Config.PATH_IMG + rvData.get(position).gambar)
                .apply(new RequestOptions().placeholder(R.drawable.logo_simdes).error(R.drawable.logo_simdes))
                .into(holder.imgGambar);

        holder.btnItem.setOnClickListener(view -> {
            DataHelper.data_lapak_desa = rvData.get(position);
            Intent u = new Intent(context.getApplicationContext(), DetailLapakDesaActivity.class);
            u.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(u);
        });
    }

    @Override
    public int getItemCount() {
        return rvData != null ? rvData.size() : 0;
    }
}
