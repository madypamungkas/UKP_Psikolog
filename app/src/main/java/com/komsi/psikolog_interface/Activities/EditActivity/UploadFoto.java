package com.komsi.psikolog_interface.Activities.EditActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.psikolog_interface.Activities.MainActivity;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.Models.Response_Details;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadFoto extends AppCompatActivity implements View.OnClickListener{
    Button bt_gallery, bt_uploadFoto;
    ImageView uploadFoto;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    User user = SharedPrefManager.getInstance(this).getUser();
    final String token = "Bearer "+ user.getToken();
    Details details = SharedPrefManager.getInstance(UploadFoto.this).getDetails();
    final int idUser = details.getUserId();
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_foto);
        TextView back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadFoto.super.onBackPressed();
            }
        });

        bt_gallery = (Button)findViewById(R.id.bt_gallery);
        bt_uploadFoto = (Button)findViewById(R.id.bt_uploadFoto);
        uploadFoto = (ImageView)findViewById(R.id.uploadFoto);

        bt_gallery.setOnClickListener(this);
        bt_uploadFoto.setOnClickListener(this);

        SharedPreferences sharedPrefFCM = getSharedPreferences("linkFoto", Context.MODE_PRIVATE);
        String link = sharedPrefFCM.getString("link", null);
        final String url = "http://psikologi.ridwan.info/images/" + details.getFoto();
        uploadFoto = (ImageView)findViewById(R.id.uploadFoto);
        Picasso.with(this).load(url).error(R.drawable.menu_icon_user).into(uploadFoto);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_gallery:
                selectImage();
                break;
            case R.id.bt_uploadFoto:
                loading = ProgressDialog.show(this, null, "Sedang Memproses...", true, false);

                uploadImage();

                break;
        }
    }
    private void uploadImage(){
        SharedPreferences sharedPref = getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
        final int iduser= sharedPref.getInt("user_id", 0);


        String image = imgToString();
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().uploadImg(token, iduser, image);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(UploadFoto.this, "Berhasil Meng Upload Foto", Toast.LENGTH_LONG).show();
                    loadDetails();
                    Intent intent = new Intent(UploadFoto.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(UploadFoto.this, "Gagal Meng Unggah Foto" , Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) { loading.dismiss();
                Toast.makeText(UploadFoto.this, "Gagal", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode==RESULT_OK && data!= null){
            Uri path = data.getData();
            uploadFoto.setVisibility(View.VISIBLE);
            bt_uploadFoto.setEnabled(true);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                uploadFoto.setImageBitmap(bitmap);
             } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    private String imgToString(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        bt_uploadFoto.setEnabled(true);
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }
    public void loadDetails(){
        retrofit2.Call<Response_Details> call = RetrofitClient
                .getInstance()
                .getApi()
                .getDetails(token);
        call.enqueue(new retrofit2.Callback<Response_Details>() {
            @Override
            public void onResponse(retrofit2.Call<Response_Details> call, retrofit2.Response<Response_Details> response) {
                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(UploadFoto.this).saveDetails(response.body().getDetails());
                    Intent intent = new Intent(UploadFoto.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    Toast.makeText(UploadFoto.this, "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Response_Details> call, Throwable t) {
                Toast.makeText(UploadFoto.this, "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
            }
        });
    }
}

