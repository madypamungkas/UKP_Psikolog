package com.komsi.psikolog_interface.Models;

public class Psikolog {
    private int id;
    private String gelar;
    private String no_sipp;
    private int user_id;

    public Psikolog(int id, String gelar, String no_sipp, int user_id) {
        this.id = id;
        this.gelar = gelar;
        this.no_sipp = no_sipp;
        this.user_id = user_id;
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

    public int getUser_id() {
        return user_id;
    }
}
