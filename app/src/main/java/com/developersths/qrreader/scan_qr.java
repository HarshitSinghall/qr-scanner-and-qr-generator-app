package com.developersths.qrreader;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.qrcode.encoder.QRCode;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.core.BarcodeScannerView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scan_qr extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        camera = new ZXingScannerView(this);
        setContentView(camera);

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        camera.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    @Override
    public void handleResult(Result rawResult) {
        Dialog dialog = new Dialog(scan_qr.this);
        dialog.setContentView(R.layout.success_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        TextView search_btn = dialog.findViewById(R.id.search_btn);
        TextView copy_btn = dialog.findViewById(R.id.copy_btn);
        TextView cancel_btn = dialog.findViewById(R.id.cancel_btn);
        TextView rslt_txt = dialog.findViewById(R.id.rslt_txt);
        Toast.makeText(scan_qr.this, rawResult.getText(), Toast.LENGTH_SHORT).show();
        rslt_txt.setText(rawResult.getText().toString());

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url;
                if(Patterns.WEB_URL.matcher(rawResult.getText()).matches()) {
                    url = rawResult.getText();
                }else {
                    url = "https://www.google.com/#q=" + rawResult.getText().toString();
                }
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                dialog.dismiss();
                camera.resumeCameraPreview(scan_qr.this);

            }
        });

        copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Result", rslt_txt.getText().toString().trim());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(scan_qr.this, "Copied!", Toast.LENGTH_SHORT).show();

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.setResultHandler(this);
        camera.startCamera();
    }

}