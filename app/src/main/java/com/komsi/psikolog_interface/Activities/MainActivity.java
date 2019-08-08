package com.komsi.psikolog_interface.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.Menu.UbahBiodata;
import com.komsi.psikolog_interface.Activities.VerifEmail.SendEmailVerif;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Fragment.AccountFragment;
import com.komsi.psikolog_interface.Fragment.HomeFragment;
import com.komsi.psikolog_interface.Fragment.OrderFragment;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.Models.Response_Details;
import com.komsi.psikolog_interface.Models.SendEmailVerif_Model;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;

import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private HomeFragment homeFragment;
    private OrderFragment orderFragment;
    private AccountFragment accountFragment;
    User user = SharedPrefManager.getInstance(this).getUser();
    private String token = "Bearer " + user.getToken();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        homeFragment = new HomeFragment();
        orderFragment = new OrderFragment();
        accountFragment = new AccountFragment();

        setFragment(homeFragment);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                    case R.id.nav_home :
                        // mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_order :
                        //mMainNav.setItemBackgroundResource(R.color.colorAccent);
                        setFragment(orderFragment);
                        return true;
                    case R.id.nav_account :
                        //mMainNav.setItemBackgroundResource(R.color.gradStop);
                        setFragment(accountFragment);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }
    private void setFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();

    }
    Dialog myDialog;
    @Override
    protected void onStart() {
        super.onStart();
        Details details = SharedPrefManager.getInstance(this).getDetails();


    }

    @Override
    protected void onResume() {
        super.onResume();
            getDetails();
        }
      /*  public void CheckVerif(){
            retrofit2.Call<SendEmailVerif_Model> call = RetrofitClient.getInstance().getApi().getStatusVerif(token);
            call.enqueue(new Callback<SendEmailVerif_Model>() {
                @Override
                public void onResponse(retrofit2.Call<SendEmailVerif_Model> call, Response<SendEmailVerif_Model> response) {
                    if(response.body().getMessage()== "Not verified"){

                    }
                }

                @Override
                public void onFailure(retrofit2.Call<SendEmailVerif_Model> call, Throwable t) {

                }
            });

        }*/
        public void getDetails(){

            retrofit2.Call<Response_Details> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getDetails(token);
            call.enqueue(new retrofit2.Callback<Response_Details>() {
                @Override
                public void onResponse(retrofit2.Call<Response_Details> call, retrofit2.Response<Response_Details> response) {
                    if (response.isSuccessful()) {
                        SharedPrefManager.getInstance(MainActivity.this).saveDetails(response.body().getDetails());

                    } else {
                        Toast.makeText(MainActivity.this, "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Response_Details> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
                }
            });
        }
}
