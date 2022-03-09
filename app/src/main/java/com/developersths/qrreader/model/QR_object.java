package com.developersths.qrreader.model;

import android.graphics.Bitmap;

public class QR_object {
    String title;
    Bitmap qr_image;
    int id;

    public QR_object(String title, Bitmap qr_image) {
        this.title = title;
        this.qr_image = qr_image;
    }

    public QR_object() {
        //empty constructor
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getQr_image() {
        return qr_image;
    }

    public void setQr_image(Bitmap qr_image) {
        this.qr_image = qr_image;
    }


}
