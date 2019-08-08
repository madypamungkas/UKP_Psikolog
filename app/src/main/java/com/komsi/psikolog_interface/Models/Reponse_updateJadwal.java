package com.komsi.psikolog_interface.Models;

public class Reponse_updateJadwal {
    private String status;
    private UpdateJadwal_Models jadwal;

    public Reponse_updateJadwal(String status, UpdateJadwal_Models jadwal) {
        this.status = status;
        this.jadwal = jadwal;
    }

    public String getStatus() {
        return status;
    }

    public UpdateJadwal_Models getJadwal() {
        return jadwal;
    }
}
