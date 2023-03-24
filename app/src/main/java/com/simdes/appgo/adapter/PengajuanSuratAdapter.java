package com.simdes.appgo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.simdes.appgo.DetailBeritaActivity;
import com.simdes.appgo.R;
import com.simdes.appgo.SyaratSuratUserActivity;
import com.simdes.appgo.model.PengaduanModel;
import com.simdes.appgo.model.PengajuanSuratModel;
import com.simdes.appgo.network.Config;
import com.simdes.appgo.utils.DataHelper;

import java.util.ArrayList;

public class PengajuanSuratAdapter extends RecyclerView.Adapter<PengajuanSuratAdapter.ViewHolder>{

    public ArrayList<PengajuanSuratModel> rvData;
    public Context context;
    public int poss;

    public PengajuanSuratAdapter(ArrayList<PengajuanSuratModel> inputData, Context context) {
        this.rvData = inputData;
        this.context = context;
        this.poss = poss;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtUser, txtJudul, txtTindakan, txtCatatan;
        public LinearLayout btnItem;
        public CardView btnUnduhSurat, btnUpdateSyarat;

        public ViewHolder(View v) {
            super(v);
            txtUser = v.findViewById(R.id.txtUser);
            txtJudul = v.findViewById(R.id.txtJudul);
            txtTindakan = v.findViewById(R.id.txtTindakan);
            txtCatatan = v.findViewById(R.id.txtCatatan);
            btnUnduhSurat = v.findViewById(R.id.btnUnduhSurat);
            btnUpdateSyarat = v.findViewById(R.id.btnUpdateSyarat);
            btnItem = v.findViewById(R.id.btnItem);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pengajuan_surat_adapter, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txtJudul.setText(rvData.get(position).surat.nama);
        holder.txtTindakan.setText(rvData.get(position).tindakan);
        holder.txtCatatan.setText(rvData.get(position).catatan);

        holder.btnUnduhSurat.setVisibility(View.GONE);

        if(rvData.get(position).tindakan != null && !rvData.get(position).tindakan.equals("")){
            holder.btnUnduhSurat.setVisibility(View.VISIBLE);
        }

        holder.btnUpdateSyarat.setOnClickListener(view -> {
            DataHelper.data_pengajuan_surat = rvData.get(position);
            Intent u = new Intent(context.getApplicationContext(), SyaratSuratUserActivity.class);
            u.putExtra("nama_surat", rvData.get(position).surat.nama);
            u.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(u);
        });

        holder.btnUnduhSurat.setOnClickListener(view -> {
            Intent httpIntent = new Intent(Intent.ACTION_VIEW);
            httpIntent.setData(Uri.parse("" + Config.PATH_IMG + rvData.get(position).berkas));
            httpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(httpIntent);
        });
    }

    @Override
    public int getItemCount() {
        return rvData != null ? rvData.size() : 0;
    }
}
