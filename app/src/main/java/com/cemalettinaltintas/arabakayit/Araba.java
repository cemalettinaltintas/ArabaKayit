package com.cemalettinaltintas.arabakayit;

public class Araba {
    private int id;
    private String marka;
    private String model;

    public Araba(int id, String marka, String model) {
        this.id = id;
        this.marka = marka;
        this.model = model;
    }

    // Getter metodları (Verilere ulaşmak için)
    public int getId() { return id; }
    public String getMarka() { return marka; }
    public String getModel() { return model; }

    // Listede görünecek metin
    @Override
    public String toString() {
        return marka + " " + model;
    }
}