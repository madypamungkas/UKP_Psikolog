package com.komsi.psikolog_interface.Models;

public class ValidasiJadwal_Response {
    String status;
    String message;

    public ValidasiJadwal_Response(String status, String message) {
        this.status = status;
        this.message = message;
    }
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
