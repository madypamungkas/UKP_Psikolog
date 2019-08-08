package com.komsi.psikolog_interface.Models;

import com.komsi.psikolog_interface.Models.User;

public class LoginResponse {
    private User user;


    public LoginResponse(User user) {
        this.user = user;

    }

    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }
}
