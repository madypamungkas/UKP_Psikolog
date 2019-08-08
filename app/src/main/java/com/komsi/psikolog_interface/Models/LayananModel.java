package com.komsi.psikolog_interface.Models;

public class LayananModel {
    private int id;
    private String nama;
    private String deskripsi;
    private String harga;
    private String foto;
    private String created_at;
    private String updated_at;
    private String deleted_at;

    public LayananModel(int id, String nama, String deskripsi, String harga, String foto, String created_at, String updated_at, String deleted_at) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.foto = foto;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getHarga() {
        return harga;
    }

    public String getFoto() {
        return foto;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }
}
