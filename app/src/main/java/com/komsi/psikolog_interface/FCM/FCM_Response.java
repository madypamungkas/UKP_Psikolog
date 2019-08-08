package com.komsi.psikolog_interface.FCM;

import com.komsi.psikolog_interface.Models.FCM_NotifModel;

public class FCM_Response {
    private FCM_NotifModel fcm_notifModel;

    public FCM_Response(FCM_NotifModel fcm_notifModel) {
        this.fcm_notifModel = fcm_notifModel;
    }

    public FCM_NotifModel getFcm_notifModel() {
        return fcm_notifModel;
    }
}
