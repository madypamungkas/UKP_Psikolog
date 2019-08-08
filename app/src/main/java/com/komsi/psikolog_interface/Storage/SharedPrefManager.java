package com.komsi.psikolog_interface.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.Models.GantiPasswordModels;
import com.komsi.psikolog_interface.Models.Psikolog;
import com.komsi.psikolog_interface.Models.User;

public class SharedPrefManager {
    public static final String SHARED_PREF_NAME = "user_shared_pref";
    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx){
        this.mCtx= mCtx;

    }
    public static synchronized SharedPrefManager getInstance(Context mCtx){
        if(mInstance == null){
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;

    }

    public void saveUser(User user){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("username_id", user.getUsername_id());
        editor.putString("email", user.getEmail());
        editor.putString("token", user.getToken());
        editor.apply();

    }
    public void savePsikolog(Psikolog psikolog) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", psikolog.getId());
        editor.putString("gelar", psikolog.getGelar());
        editor.putString("no_sipp", psikolog.getNo_sipp());
        editor.putInt("user_id", psikolog.getUser_id());
        editor.apply();
    }
    public  Psikolog getPsikolog(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Psikolog(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("status", null),
                sharedPreferences.getString("sukses", null),
                sharedPreferences.getInt("user_id", -1)

        );
    }
    public void saveDetails(Details details){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", details.getUserId());
        editor.putString("name", details.getName());
        editor.putString("email", details.getEmail());
        editor.putString("email_verified_at", details.getEmail_verified_at());
        //editor.putString("password", details.getPassword());
        //editor.putString("remember_token", details.getRemember_token());
        editor.putString("level", details.getLevel());
        editor.putString("username", details.getUsername());
        editor.putString("nik", details.getNik());
        editor.putString("tanggal_lahir", details.getTanggal_lahir());
        editor.putString("jenis_kelamin", details.getJenis_kelamin());
        editor.putString("alamat", details.getAlamat());
        editor.putString("no_telepon", details.getNo_telepon());
        editor.putString("foto", details.getFoto());
        editor.putString("isActive", details.getIsActive());
        editor.putString("fcm_token", details.getFcm_token());
        editor.putString("status", details.getStatus());
        editor.putString("created_at", details.getCreated_at());
        editor.putString("updated_at", details.getUpdated_at());
        editor.putString("deleted_at", details.getDeleted_at());

        editor.apply();

    }
    public void saveStatusGantiPassword(GantiPasswordModels gantiPassModel){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("status", gantiPassModel.getStatus());
        editor.putString("sukses", gantiPassModel.getStatus());

        editor.apply();

    }

    public GantiPasswordModels getGantiPass(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new GantiPasswordModels(
                sharedPreferences.getString("status", null),
                sharedPreferences.getString("sukses", null)

        );
    }
    public User getUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("username_id", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("token", null)

        );
    }

    public Details getDetails(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Details(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("email_verified_at", null),
                //sharedPreferences.getString("password", null),
                //sharedPreferences.getString("remember_token", null),
                sharedPreferences.getString("level", null),
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("nik", null),
                sharedPreferences.getString("tanggal_lahir", null),
                sharedPreferences.getString("jenis_kelamin", null),
                sharedPreferences.getString("alamat", null),
                sharedPreferences.getString("no_telepon", null),
                sharedPreferences.getString("foto", null),
                sharedPreferences.getString("isActive", null),
                sharedPreferences.getString("fcm_token", null),
                sharedPreferences.getString("status", null),
                sharedPreferences.getString("created_at", null),
                sharedPreferences.getString("updated_at", null),
                sharedPreferences.getString("deleted_at", null)

        );
    }
    public void getLink(String link){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        link = "";
        sharedPreferences.getString("link", null);
        editor.apply();
    }
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString("username_id",null) !=null)
            return true;
        return false;
    }


    public void clear(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
