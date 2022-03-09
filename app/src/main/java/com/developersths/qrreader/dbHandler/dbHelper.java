package com.developersths.qrreader.dbHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.developersths.qrreader.HistoryActivity;
import com.developersths.qrreader.params.params;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.developersths.qrreader.model.QR_object;

public class dbHelper extends SQLiteOpenHelper {
    public dbHelper(Context context) {
        super(context, params.DB_NAME, null, params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + params.TABLE_NAME + "(" + params.KEY_ID + " INTEGER PRIMARY KEY," + params.KEY_TITLE
                + " TEXT , " + params.KEY_IMAGE + " BLOB )";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long upload_qr(QR_object qrObject){
        String title = qrObject.getTitle();
        Bitmap qr_bitmap = qrObject.getQr_image();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        qr_bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] byteImage = byteArrayOutputStream.toByteArray();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(params.KEY_TITLE, title);
        values.put(params.KEY_IMAGE, byteImage);
        return db.insert(params.TABLE_NAME, null, values);
    }

    public ArrayList<QR_object> get_qr(){
        ArrayList<QR_object> qr_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + params.TABLE_NAME;
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()){
            do {
                QR_object qr_item = new QR_object();
                qr_item.setId(cursor.getInt(0));
                qr_item.setTitle(cursor.getString(1));
                byte[] qr_bytes = cursor.getBlob(2);
                Bitmap qr_bitmap = BitmapFactory.decodeByteArray(qr_bytes, 0, qr_bytes.length);
                qr_item.setQr_image(qr_bitmap);
                qr_list.add(qr_item);
            }while(cursor.moveToNext());
        }else{
            return null;
        }
        db.close();
        if (qr_list.isEmpty()){
            return null;
        }else{
            return qr_list;
        }
    }

    public void delete_qr(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int c = db.delete(params.TABLE_NAME, params.KEY_ID + "=?", new String[]{String.valueOf(id)});
        Log.d("Harshit", "delete_qr: " + c);
        db.close();
    }

}
