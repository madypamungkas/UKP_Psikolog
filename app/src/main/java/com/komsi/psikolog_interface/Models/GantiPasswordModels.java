package com.komsi.psikolog_interface.Models;

public class GantiPasswordModels {
    private String status;
    private String sukses;

    public GantiPasswordModels(String status, String sukses) {
        this.status = status;
        this.sukses = sukses;
    }

    public String getStatus() {
        return status;
    }

    public String getSukses() {
        return sukses;
    }
}
