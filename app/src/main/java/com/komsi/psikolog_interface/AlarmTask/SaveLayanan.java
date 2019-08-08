package com.komsi.psikolog_interface.AlarmTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.EditActivity.ResetKeahlian;

import java.util.HashMap;

public class SaveLayanan extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPref = context.getSharedPreferences("Array Layanan", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String a = sharedPref.getString("idLayanan", null);
        editor.putString("idLayanan1",String.valueOf(intent.getIntExtra("idLayanan", 15)));
        editor.apply();
        Log.d("layanan", a);
//        HashMap<String , Integer > map = new HashMap<>();
//        map.put("layanan1", intent.getIntExtra("idLayanan", 0) );
//        Toast.makeText(context, map.get("layanan1")+ " ", Toast.LENGTH_SHORT).show();
//        Log.d("layanan", String.valueOf(map.get("layanan1")));

//     for (int v : map.values()){
//         Log.d("hashmap map", String.valueOf(v));
//     }
    }
}

/*

 if (sharedPref.getInt("idLayanan1", 0) == 0 ){
         editor.putInt("idLayanan1",intent.getIntExtra("idLayanan", 15));
         editor.apply();

         }
         else if(sharedPref.getInt("idLayanan2", 0) == 0 ){
         editor.putInt("idLayanan2",intent.getIntExtra("idLayanan", 15));
         editor.apply();
         }
         else if (sharedPref.getInt("idLayanan3", 0) == 0 ){
         editor.putInt("idLayanan2",intent.getIntExtra("idLayanan", 15));
         editor.apply();
         }*/
