package com.cemalettinaltintas.arabakayit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ArabaKayit.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE arabalar (id INTEGER PRIMARY KEY AUTOINCREMENT, marka TEXT, model TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS arabalar");
        onCreate(db);
    }

    public boolean veriEkle(String marka, String model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("marka", marka);
        values.put("model", model);
        long sonuc = db.insert("arabalar", null, values);
        return sonuc != -1; // -1 değilse kayıt başarılıdır
    }
    // DatabaseHelper sınıfının içine ekle:
    public ArrayList<String> tumArabalariGetir() {
        ArrayList<String> liste = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Tüm tabloyu oku
        Cursor cursor = db.rawQuery("SELECT * FROM arabalar ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                // Sütun indislerine göre veriyi al (0:id, 1:marka, 2:model)
                String satir = cursor.getString(1) + " " + cursor.getString(2);
                liste.add(satir);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return liste;
    }
}
