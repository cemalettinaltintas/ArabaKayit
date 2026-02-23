package com.cemalettinaltintas.arabakayit;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    EditText editMarka, editModel;
    Button btnKaydet;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<Araba> arabaListesi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper = new DatabaseHelper(this);
        editMarka = findViewById(R.id.editMarka);
        editModel = findViewById(R.id.editModel);
        btnKaydet = findViewById(R.id.btnKaydet);

        btnKaydet.setOnClickListener(v -> {
            String marka = editMarka.getText().toString();
            String model = editModel.getText().toString();
            boolean eklendi = dbHelper.veriEkle(marka, model);
            if (eklendi) {
                listeyiYenile();
                Toast.makeText(this, "Veri başarıyla eklendi", Toast.LENGTH_SHORT).show();
                editMarka.setText("");
                editModel.setText("");
            }
            else {
                Toast.makeText(this, "Veri ekleme başarısız oldu", Toast.LENGTH_SHORT).show();
            }
        });


        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.listViewArabalar);


        listeyiYenile();
    }
    // Listeyi veritabanından çekip ekrana basan yardımcı metod
    private void listeyiYenile() {
        arabaListesi = dbHelper.tumArabalariGetir();
        ArabaAdapter adapter = new ArabaAdapter(this, arabaListesi, dbHelper);
        listView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.ana_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_listele){
            Toast.makeText(this, "Tümünü Listele seçildi", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId()==R.id.menu_hakkinda){
            Toast.makeText(this, "Hakkında seçildi", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}