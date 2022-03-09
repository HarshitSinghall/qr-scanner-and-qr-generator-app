package com.developersths.qrreader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developersths.qrreader.model.QR_object;

import java.util.ArrayList;

public class HistoryRecycler extends RecyclerView.Adapter<HistoryRecycler.ViewHolder> {

    ArrayList<QR_object> qr_list;
    Context context;
    private selectListener listener;

    public HistoryRecycler(ArrayList<QR_object> qr_list, Context context, selectListener listener) {
        this.qr_list = qr_list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QR_object qr = qr_list.get(position);
        holder.qr_img.setImageBitmap(qr.getQr_image());
        holder.qr_title.setText(qr.getTitle());

        holder.qr_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(qr);
            }
        });

    }

    @Override
    public int getItemCount() {
        return qr_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView qr_img;
        TextView qr_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            qr_img = itemView.findViewById(R.id.qr_code);
            qr_title = itemView.findViewById(R.id.qr_title);
        }
    }
}
