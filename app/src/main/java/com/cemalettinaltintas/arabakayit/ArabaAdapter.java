package com.cemalettinaltintas.arabakayit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ArabaAdapter extends ArrayAdapter<Araba> {
    private Context context;
    private ArrayList<Araba> list;
    private DatabaseHelper dbHelper;

    public ArabaAdapter(Context context, ArrayList<Araba> list, DatabaseHelper db) {
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

        Araba mevcutAraba = list.get(position);
        txtBilgi.setText(mevcutAraba.getMarka() + " " + mevcutAraba.getModel());

        btnSil.setOnClickListener(v -> {
            // Nesnenin içindeki gerçek ID'yi gönderiyoruz
            dbHelper.veriSil(mevcutAraba.getId());
            list.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Kayıt Silindi", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }
}