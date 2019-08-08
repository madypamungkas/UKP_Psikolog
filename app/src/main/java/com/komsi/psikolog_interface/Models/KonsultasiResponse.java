package com.komsi.psikolog_interface.Models;

import java.util.List;

public class KonsultasiResponse {
    private int id;
    private String nama;
    private String  created_at, updated_at, deleted_at;
    private List<JadwalModels> jadwal;

    public KonsultasiResponse(int id, String nama, String created_at, String updated_at, String deleted_at, List<JadwalModels> jadwal) {
        this.id = id;
        this.nama = nama;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.jadwal = jadwal;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
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

    public List<JadwalModels> getJadwal() {
        return jadwal;
    }
}
