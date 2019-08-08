package com.komsi.psikolog_interface.AlarmTask;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.komsi.psikolog_interface.Activities.MainActivity;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotifTask extends BroadcastReceiver {

    NotificationCompat.Builder notification;
    public static final int uniqeID = 45614;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Alarmnotif", "Alarm Fired");
        notification = new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);
        Details details = SharedPrefManager.getInstance(context).getDetails();



        notification.setSmallIcon(R.drawable.nav_icon_orders);
        notification.setTicker("ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Jadwal Konsultasi Hari Ini");
        // notification.setLargeIcon(R.drawable.nav_icon_orders);
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date newDate = calendar;
        String dateTime = dateFormat.format(calendar);
        //notification.setContentText("Test 2 "+ dateTime);
        notification.setContentText("Terdapat Jadwal Konsultasi Yang Akan Dilaksanakan Hari Ini");

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqeID, notification.build());



        try
        {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
