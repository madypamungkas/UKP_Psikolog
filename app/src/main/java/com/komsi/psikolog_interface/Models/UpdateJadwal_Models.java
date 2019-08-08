package com.komsi.psikolog_interface.Models;

public class UpdateJadwal_Models {
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

    public UpdateJadwal_Models(int id, String tanggal, int sesi_id, int klien_id, String keluhan, int layanan_id, int tes_id, int ruangan_id, int psikolog_id, int status_id) {
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
}
