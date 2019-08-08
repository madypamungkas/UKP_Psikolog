package com.komsi.psikolog_interface.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonDefault {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    public JsonDefault(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public  String getMessage() {
        return message;
    }
}
