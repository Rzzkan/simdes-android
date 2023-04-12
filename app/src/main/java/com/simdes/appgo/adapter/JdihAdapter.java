package com.simdes.appgo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simdes.appgo.DetailApbDesaActivity;
import com.simdes.appgo.R;
import com.simdes.appgo.model.ApbDesaModel;
import com.simdes.appgo.model.JdihModel;
import com.simdes.appgo.utils.DataHelper;

import java.util.ArrayList;

public class JdihAdapter extends RecyclerView.Adapter<JdihAdapter.ViewHolder>{

    public ArrayList<ApbDesaModel> rvData;
    public Context context;
    public int poss;

    public JdihAdapter(ArrayList<ApbDesaModel> inputData, Context context) {
        this.rvData = inputData;
        this.context = context;
        this.poss = poss;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtUser, txtJudul;
        public LinearLayout btnItem;

        public ViewHolder(View v) {
            super(v);
            txtUser = v.findViewById(R.id.txtUser);
            txtJudul = v.findViewById(R.id.txtJudul);
            btnItem = v.findViewById(R.id.btnItem);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.jdih_adapter, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txtJudul.setText(rvData.get(position).judul);
        holder.txtUser.setText("Oleh: " + rvData.get(position).user.name);

        holder.btnItem.setOnClickListener(view -> {
            DataHelper.data_apb_desa = rvData.get(position);
            Intent u = new Intent(context.getApplicationContext(), DetailApbDesaActivity.class);
            u.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(u);
        });
    }

    @Override
    public int getItemCount() {
        return rvData != null ? rvData.size() : 0;
    }
}
