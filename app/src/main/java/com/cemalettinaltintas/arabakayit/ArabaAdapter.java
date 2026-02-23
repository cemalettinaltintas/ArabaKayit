package com.cemalettinaltintas.arabakayit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class ArabaAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> list;
    private DatabaseHelper dbHelper;

    public ArabaAdapter(Context context, ArrayList<String> list, DatabaseHelper db) {
        super(context, R.layout.list_item, list);
        this.context = context;
        this.list = list;
        this.dbHelper = db;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        TextView txtBilgi = convertView.findViewById(R.id.txtArabaBilgi);
        Button btnSil = convertView.findViewById(R.id.btnSil);

        String mevcutAraba = list.get(position);
        txtBilgi.setText(mevcutAraba);

        btnSil.setOnClickListener(v -> {
            dbHelper.veriSil(mevcutAraba); // Veritabanından sil
            list.remove(position); // Listeden sil
            notifyDataSetChanged(); // Ekranı güncelle
        });

        return convertView;
    }
}