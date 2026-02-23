package com.cemalettinaltintas.arabakayit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    int seciliId = -1; // -1 ise yeni kayıt, -1'den büyükse güncelleme modundayız demektir.
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

            if (marka.isEmpty() || model.isEmpty()) {
                Toast.makeText(this, "Boş alan bırakmayın", Toast.LENGTH_SHORT).show();
                return;
            }

            if (seciliId == -1) {
                // YENİ KAYIT MODU
                dbHelper.veriEkle(marka, model);
                Toast.makeText(this, "Eklendi!", Toast.LENGTH_SHORT).show();
            } else {
                // GÜNCELLEME MODU
                dbHelper.veriGuncelle(seciliId, marka, model);
                Toast.makeText(this, "Güncellendi!", Toast.LENGTH_SHORT).show();

                // İşlem bitince modu sıfırla
                seciliId = -1;
                btnKaydet.setText("KAYDET");
            }

            // Arayüzü temizle ve listeyi yenile
            editMarka.setText("");
            editModel.setText("");
            listeyiYenile();
        });


        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.listViewArabalar);


        listeyiYenile();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Araba secilenAraba = arabaListesi.get(position);

            // Verileri kutucuklara doldur
            editMarka.setText(secilenAraba.getMarka());
            editModel.setText(secilenAraba.getModel());

            // ID'yi hafızaya al ve butonu güncelle
            seciliId = secilenAraba.getId();
            btnKaydet.setText("GÜNCELLE");
        });

        EditText editArama = findViewById(R.id.editArama);

        editArama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kullanıcı yazdıkça bu metod çalışır
                String arananKelime = s.toString();
                aramaYap(arananKelime);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


    }
    // Arama işlemini yürüten yardımcı metod
    private void aramaYap(String kelime) {
        arabaListesi = dbHelper.arabaAra(kelime);
        ArabaAdapter adapter = new ArabaAdapter(this, arabaListesi, dbHelper);
        listView.setAdapter(adapter);
    }


    // Listeyi veritabanından çekip ekrana basan yardımcı metod
    private void listeyiYenile() {
        arabaListesi = dbHelper.tumArabalariGetir();
        ArabaAdapter adapter = new ArabaAdapter(this, arabaListesi, dbHelper);
        listView.setAdapter(adapter);
    }

    private void formuTemizle() {
        editMarka.setText("");
        editModel.setText("");
        seciliId = -1; // Güncelleme modundan çık
        btnKaydet.setText("KAYDET"); // Butonu eski haline döndür
        Toast.makeText(this, "İşlem iptal edildi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.ana_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_listele) {
            listeyiYenile();
            return true;
        } else if (id == R.id.menu_iptal) {
            formuTemizle(); // Yazdığımız metodu çağırdık
            return true;
        }
        return false;
    }
}