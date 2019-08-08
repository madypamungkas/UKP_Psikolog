package com.komsi.psikolog_interface.Models;

import java.util.List;

public class Response_LayananPsikolog {
    private int id;
    private String gelar;
    private String no_sipp;
    private int keahlian_id;
    private int user_id;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private List<LayananModel> layanan;

    public Response_LayananPsikolog(int id, String gelar, String no_sipp, int keahlian_id, int user_id, String created_at, String updated_at, String deleted_at, List<LayananModel> layanan) {
        this.id = id;
        this.gelar = gelar;
        this.no_sipp = no_sipp;
        this.keahlian_id = keahlian_id;
        this.user_id = user_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.layanan = layanan;
    }

    public int getId() {
        return id;
    }

    public String getGelar() {
        return gelar;
    }

    public String getNo_sipp() {
        return no_sipp;
    }

    public int getKeahlian_id() {
        return keahlian_id;
    }

    public int getUser_id() {
        return user_id;
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

    public List<LayananModel> getLayanan() {
        return layanan;
    }
}
