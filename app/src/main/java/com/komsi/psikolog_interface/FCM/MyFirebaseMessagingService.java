package com.komsi.psikolog_interface.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.komsi.psikolog_interface.Activities.MainActivity;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG ="fcm_spikolog";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (SharedPrefManager.getInstance(this).isLoggedIn())
                return;
            if (remoteMessage.getData().size() > 0)
            {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                createNotif(remoteMessage.getData().get("pesan"));
                /*Map<String, String >map = remoteMessage.getData();
                createNotif(map);
*/
            }

        }
    }


    private void createNotif(String pesan){

//        String pesan = json.get("pesan");
//        String subject = json.get("subject");


        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivities(this, 0, new Intent[] { i }, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification n = new Notification.Builder(this)
                .setTicker("MeetMyPsy")
                .setContentTitle(pesan)
                .setContentText(pesan)
               // .setSmallIcon(R.mipmap.ic_launcher)
                // .setLargeIcon(bmURL)
                .setAutoCancel(true).setContentIntent(pi)
                .build();

        NotificationManager man = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        man.notify(1, n);
        try
        {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(this, notification);
            r.play();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }



   /* private void createNotif(Map<String, String> json){

        String pesan = json.get("pesan");
        String subject = json.get("subject");


        Intent i = new Intent(this, DaftarActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivities(this, 0, new Intent[] { i }, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification n = new Notification.Builder(this)
                .setTicker("MeetMyPsy")
                .setContentTitle(subject)
                .setContentText(pesan)
                .setSmallIcon(R.mipmap.ic_launcher)
                // .setLargeIcon(bmURL)
                .setAutoCancel(true).setContentIntent(pi)
                .build();

        NotificationManager man = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        man.notify(1, n);
        try
        {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(this, notification);
            r.play();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
*/
    public void playBeep()
    {

    }

}
