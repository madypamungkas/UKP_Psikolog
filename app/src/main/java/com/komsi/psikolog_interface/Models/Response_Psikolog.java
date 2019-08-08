package com.komsi.psikolog_interface.Models;

public class Response_Psikolog {
    private int id;
    private String name,  email, email_verified_at, level, username, nik, tanggal_lahir, jenis_kelamin, alamat, no_telepon, foto;
    private String updated_at, created_at, deleted_at;
    private Psikolog psikolog;

    public Response_Psikolog(int id, String name, String email, String email_verified_at, String level, String username, String nik, String tanggal_lahir, String jenis_kelamin, String alamat, String no_telepon, String foto, String updated_at, String created_at, String deleted_at, Psikolog psikolog) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.email_verified_at = email_verified_at;
        this.level = level;
        this.username = username;
        this.nik = nik;
        this.tanggal_lahir = tanggal_lahir;
        this.jenis_kelamin = jenis_kelamin;
        this.alamat = alamat;
        this.no_telepon = no_telepon;
        this.foto = foto;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.deleted_at = deleted_at;
        this.psikolog = psikolog;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getLevel() {
        return level;
    }

    public String getUsername() {
        return username;
    }

    public String getNik() {
        return nik;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNo_telepon() {
        return no_telepon;
    }

    public String getFoto() {
        return foto;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public Psikolog getPsikolog() {
        return psikolog;
    }
}
