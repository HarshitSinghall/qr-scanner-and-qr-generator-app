package com.developersths.qrreader;
// CODE BY HARSHIT SINGHAL
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.developersths.qrreader.databinding.ActivityGenerateQrBinding;
import com.developersths.qrreader.model.QR_object;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import com.developersths.qrreader.dbHandler.dbHelper;

import java.util.ArrayList;

public class generate_qr extends AppCompatActivity {

    EditText text_data;
    ImageView qr_image, history_img;
    Button qr_btn, save_btn;
    ActivityGenerateQrBinding binding;
    String data;
    Bitmap qrBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGenerateQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        text_data = binding.textQr;
        qr_image = binding.qrImage;
        qr_btn = binding.generateBtn;
        save_btn = binding.saveBtn;
        history_img = binding.historyImg;

        qr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = text_data.getText().toString().trim();
                if (!data.isEmpty()){
                    // create QR code
                    QRGEncoder qrEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 500);
                    qrBitmap = qrEncoder.getBitmap();
                    qr_image.setImageBitmap(qrBitmap);
                }else{
                    Toast.makeText(generate_qr.this, "Please enter some data to create QR Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qrBitmap != null){
                dbHelper db = new dbHelper(generate_qr.this);
                long rslt = db.upload_qr(new QR_object(data, qrBitmap));
                if (rslt != -1){
                    Toast.makeText(generate_qr.this, "Your QR saved successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(generate_qr.this, "This name is already saved in database", Toast.LENGTH_SHORT).show();
                }

                }
                else{
                    Toast.makeText(generate_qr.this, "Please create QR first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        history_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(generate_qr.this,HistoryActivity.class));
            }
        });

    }
}