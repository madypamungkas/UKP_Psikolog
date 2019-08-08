package com.komsi.psikolog_interface.Models;

public class SesiModel {
    private int id;
    private String nama;
    private String jam;
    private String  created_at, updated_at, deleted_at;

    public SesiModel(int id, String nama, String jam, String created_at, String updated_at, String deleted_at) {
        this.id = id;
        this.nama = nama;
        this.jam = jam;
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

    public String getJam() {
        return jam;
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
