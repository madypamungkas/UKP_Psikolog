package com.komsi.psikolog_interface.Models;

public class JadwalModels {
    private int id;
    private String tanggal;
    private int sesi_id;
    private int klien_id;
    private String keluhan;
    private int layanan_id;
    private int tes_id;
    private int ruangan_id;
    private int psikolog_id;
    private int status_id;
    private String  created_at, updated_at, deleted_at;
    private StatusModel status;
    private LayananModel layanan;
    private KlienModel klien;
    private SesiModel sesi;
    private RuanganModel ruangan;

    public JadwalModels(int id, String tanggal, int sesi_id, int klien_id, String keluhan, int layanan_id, int tes_id, int ruangan_id, int psikolog_id, int status_id, String created_at, String updated_at, String deleted_at, StatusModel status, LayananModel layanan, KlienModel klien, SesiModel sesi, RuanganModel ruangan) {
        this.id = id;
        this.tanggal = tanggal;
        this.sesi_id = sesi_id;
        this.klien_id = klien_id;
        this.keluhan = keluhan;
        this.layanan_id = layanan_id;
        this.tes_id = tes_id;
        this.ruangan_id = ruangan_id;
        this.psikolog_id = psikolog_id;
        this.status_id = status_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.status = status;
        this.layanan = layanan;
        this.klien = klien;
        this.sesi = sesi;
        this.ruangan = ruangan;
    }

    public int getId() {
        return id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public int getSesi_id() {
        return sesi_id;
    }

    public int getKlien_id() {
        return klien_id;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public int getLayanan_id() {
        return layanan_id;
    }

    public int getTes_id() {
        return tes_id;
    }

    public int getRuangan_id() {
        return ruangan_id;
    }

    public int getPsikolog_id() {
        return psikolog_id;
    }

    public int getStatus_id() {
        return status_id;
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

    public StatusModel getStatus() {
        return status;
    }

    public LayananModel getLayanan() {
        return layanan;
    }

    public KlienModel getKlien() {
        return klien;
    }

    public SesiModel getSesi() {
        return sesi;
    }

    public RuanganModel getRuangan() {
        return ruangan;
    }
}
