//package com.simdes.appgo.adapter;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.simdes.appgo.R;
//import com.simdes.appgo.utils.GeneralHelper;
//
//import java.util.ArrayList;
//
//public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ViewHolder>{
//
//    public ArrayList<ForumModel> rvData;
//    public Context context;
//    public int poss;
//
//    public ForumAdapter(ArrayList<ForumModel> inputData, Context context) {
//        this.rvData = inputData;
//        this.context = context;
//        this.poss = poss;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView txtNamaUser, txtPertanyaan;
//        public ImageView imgUser;
//        public LinearLayout btnItem;
//
//        public ViewHolder(View v) {
//            super(v);
//            txtNamaUser = v.findViewById(R.id.txtNamaUser);
//            txtPertanyaan = v.findViewById(R.id.txtPertanyaan);
//            imgUser = v.findViewById(R.id.imgUser);
//            btnItem = v.findViewById(R.id.btnItem);
//        }
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // membuat view baru
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_forum, parent, false);
//        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
//        ViewHolder vh = new ViewHolder(v);
//        return vh;
//    }
//
//    @SuppressLint("ResourceAsColor")
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//
//        holder.txtNamaUser.setText("@" + rvData.get(position).sebagai + " - " + rvData.get(position).user.name);
//        holder.txtPertanyaan.setText(rvData.get(position).pertanyaan);
//
//        Glide.with(context)
//                .load(rvData.get(position).user.avatar)
//                .apply(new RequestOptions().placeholder(R.drawable.icon_mcl).error(R.drawable.icon_mcl).circleCrop())
//                .apply(new RequestOptions().circleCrop())
//                .into(holder.imgUser);
//
//        holder.btnItem.setOnClickListener(view -> {
//            GeneralHelper.forum = rvData.get(position);
//            Intent u = new Intent(context, KomentarActivity.class);
//            context.startActivity(u);
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return rvData != null ? rvData.size() : 0;
//    }
//}
