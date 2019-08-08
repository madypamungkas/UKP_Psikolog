package com.komsi.psikolog_interface.Models;

public class Default_Response {
    private boolean error;
    private String message;
    private String result;

    public Default_Response(boolean error, String message, String result) {
        this.error = error;
        this.message = message;
        this.result = result;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getResult() {
        return result;
    }
}
