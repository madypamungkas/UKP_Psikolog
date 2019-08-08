package com.komsi.psikolog_interface.Models;

import java.util.List;

public class JadwalResponse {
    private List<JadwalModels> jadwal;

    public JadwalResponse(List<JadwalModels> jadwal) {
        this.jadwal = jadwal;
    }

    public List<JadwalModels> getJadwal() {
        return jadwal;
    }
}
