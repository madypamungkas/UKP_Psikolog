package com.komsi.psikolog_interface.Models;

public class FCM_NotifModel {
    String pesan;
    String subject;

    public FCM_NotifModel(String pesan, String subject) {
        this.pesan = pesan;
        this.subject = subject;
    }

    public String getPesan() {
        return pesan;
    }

    public String getSubject() {
        return subject;
    }
}
