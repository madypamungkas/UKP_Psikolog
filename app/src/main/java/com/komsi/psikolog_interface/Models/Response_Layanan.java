package com.komsi.psikolog_interface.Models;

import java.util.List;

public class Response_Layanan {
    private String status;
    private List<LayananModel> layanan;

    public Response_Layanan(String status, List<LayananModel> layanan) {
        this.status = status;
        this.layanan = layanan;
    }

    public String getStatus() {
        return status;
    }

    public List<LayananModel> getLayanan() {
        return layanan;
    }
}
