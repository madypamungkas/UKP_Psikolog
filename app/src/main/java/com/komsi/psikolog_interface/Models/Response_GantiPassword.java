package com.komsi.psikolog_interface.Models;

public class Response_GantiPassword {
    GantiPasswordModels password;

    public Response_GantiPassword(GantiPasswordModels password) {
        this.password = password;
    }

    public GantiPasswordModels getPassword() {
        return password;
    }
}
