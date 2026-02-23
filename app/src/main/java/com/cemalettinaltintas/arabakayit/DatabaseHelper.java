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
    // Listeleme Metodu
    public ArrayList<Araba> tumArabalariGetir() {
        ArrayList<Araba> liste = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM arabalar", null);

        if (cursor.moveToFirst()) {
            do {
                Araba araba = new Araba(
                        cursor.getInt(0),  // ID
                        cursor.getString(1), // Marka
                        cursor.getString(2)  // Model
                );
                liste.add(araba);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return liste;
    }

    // Silme Metodu (Artık ID ile çalışıyor)
    public void veriSil(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("arabalar", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void veriGuncelle(int id, String yeniMarka, String yeniModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("marka", yeniMarka);
        values.put("model", yeniModel);

        // Hangi ID'ye sahip satır güncellenecek?
        db.update("arabalar", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
