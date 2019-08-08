package com.komsi.psikolog_interface.Models;

public class User {
    private int id;
    private String name;
    private String username_id;
    private String email;
    private String token;

    public User(int id, String name, String username_id, String email, String token) {
        this.id = id;
        this.name = name;
        this.username_id = username_id;
        this.email = email;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername_id() {
        return username_id;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
