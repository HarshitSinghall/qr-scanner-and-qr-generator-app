package com.developersths.qrreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developersths.qrreader.databinding.ActivityHistoryBinding;

import java.util.ArrayList;
import com.developersths.qrreader.dbHandler.dbHelper;
import com.developersths.qrreader.model.QR_object;

public class HistoryActivity extends AppCompatActivity implements selectListener {

    ActivityHistoryBinding binding;
    RecyclerView recyclerView;
    ArrayList<QR_object> qr_list;
    HistoryRecycler adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recyclerview;

    }

    @Override
    public void onItemClicked(QR_object qr_code) {
        Dialog dialog = new Dialog(HistoryActivity.this);
        dialog.setContentView(R.layout.qr_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        Button cancel_btn = dialog.findViewById(R.id.cancel_btn);
        Button delete_btn = dialog.findViewById(R.id.delete_btn);
        TextView title = dialog.findViewById(R.id.qr_title_dialog);
        title.setText(qr_code.getTitle());
        ImageView qr = dialog.findViewById(R.id.dialog_qr);
        qr.setImageBitmap(qr_code.getQr_image());

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper db = new dbHelper(HistoryActivity.this);
                db.delete_qr(qr_code.getId());
                Toast.makeText(HistoryActivity.this, "QR Deleted successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                onResume();
            }
        });

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbHelper db = new dbHelper(this);
        qr_list = db.get_qr();
        if (qr_list != null){
            adaptor = new HistoryRecycler(qr_list, getApplicationContext(), this);
            recyclerView.setAdapter(adaptor);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }else{
            Toast.makeText(HistoryActivity.this, "sorry! No QR is available", Toast.LENGTH_SHORT).show();
        }
    }
}