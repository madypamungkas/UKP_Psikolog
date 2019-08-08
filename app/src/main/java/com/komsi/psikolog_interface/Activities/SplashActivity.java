package com.komsi.psikolog_interface.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.komsi.psikolog_interface.Activities.Dashboard;
import com.komsi.psikolog_interface.Activities.VerifEmail.SendEmailVerif;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {
    private LinearLayout logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo=(LinearLayout) findViewById(R.id.linelogo_splash);
        Animation myanim= AnimationUtils.loadAnimation(this, R.anim.splash_transition);
        logo.startAnimation(myanim);
        final Intent i =new Intent(this, Dashboard.class);
        Thread timer=new Thread(){
            public void run(){
                try {
                    sleep(4000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Details details = SharedPrefManager.getInstance(this).getDetails();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            if(details.getEmail_verified_at() == null){
                intentVerif();
            }
            else{
                intentMain();
            }
        }
    }
    public void intentMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public  void  intentVerif(){
        Intent intent = new Intent(this, SendEmailVerif.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
