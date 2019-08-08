package com.komsi.psikolog_interface.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.komsi.psikolog_interface.Activities.Loading;
import com.komsi.psikolog_interface.Activities.LoginActivity;
import com.komsi.psikolog_interface.Activities.MainActivity;
import com.komsi.psikolog_interface.Activities.Menu.CariKlien;
import com.komsi.psikolog_interface.Activities.Menu.JadwalSaya;
import com.komsi.psikolog_interface.Activities.Menu.KeahlianPsikolog;
import com.komsi.psikolog_interface.Activities.Notifikasi;
import com.komsi.psikolog_interface.Activities.Menu.PermintaanKlien;
import com.komsi.psikolog_interface.Api.RetrofitClient;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.Models.Psikolog;
import com.komsi.psikolog_interface.Models.Response_Details;
import com.komsi.psikolog_interface.Models.User;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Activities.Menu.UbahBiodata;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.CONNECTIVITY_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {


    public HomeFragment() {
        // Required empty public constructor
    }

    ProgressDialog loading;
    Switch switchStatus;
    CardView userProfile;
    User user = SharedPrefManager.getInstance(getActivity()).getUser();
    String token = "Bearer " + user.getToken();
    Psikolog psikolog = SharedPrefManager.getInstance(getActivity()).getPsikolog();
    Details details = SharedPrefManager.getInstance(getActivity()).getDetails();
    String statusApprove = details.getStatus();
    SwipeRefreshLayout swiperefresh;
    private int refresh_count = 0;
    private ConnectivityManager connectivityManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_home, container, false);
        final Details details = SharedPrefManager.getInstance(getActivity()).getDetails();

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
        String gelar = sharedPref.getString("gelar", null);
        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        swiperefresh = fragmentView.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDetails();
                 isApproved();
            }
        });
        LinearLayout permintaan = (LinearLayout) fragmentView.findViewById(R.id.permintaanKlien);
        LinearLayout cari = (LinearLayout) fragmentView.findViewById(R.id.cariKlien);
        LinearLayout jadwal = (LinearLayout) fragmentView.findViewById(R.id.jadwalSaya);
        LinearLayout biodata = (LinearLayout) fragmentView.findViewById(R.id.ubahBiodata);
        LinearLayout keahlian = (LinearLayout) fragmentView.findViewById(R.id.layananPsikolog);
        TextView namaPsikolog = (TextView) fragmentView.findViewById(R.id.namaPsikolog);
        TextView statusAktivasi = (TextView) fragmentView.findViewById(R.id.statusAktivasi);
        switchStatus = fragmentView.findViewById(R.id.switchStatus);
        userProfile = fragmentView.findViewById(R.id.userProfile);
        namaPsikolog.setText(details.getName() + " " + gelar);
        final String url = "http://psikologi.ridwan.info/images/" + details.getFoto();
        ImageView avatar = (ImageView) fragmentView.findViewById(R.id.avatar);
        Picasso.with(getActivity()).load(url).error(R.drawable.menu_icon_user).into(avatar);
        statusAktivasi.setText("Status Anda " + details.getIsActive());
        ImageView notif1 = (ImageView) fragmentView.findViewById(R.id.notif1);
        permintaan.setOnClickListener(this);
        jadwal.setOnClickListener(this);
        cari.setOnClickListener(this);
        biodata.setOnClickListener(this);
        keahlian.setOnClickListener(this);
        notif1.setOnClickListener(this);

        return fragmentView;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.permintaanKlien:
                if (statusApprove.equals("Approved")) {
                    Intent daftar = new Intent(getActivity(), PermintaanKlien.class);
                    startActivity(daftar);
                } else {
                    alertNotApproved();
                }
                break;
            case R.id.cariKlien:
                if (statusApprove.equals("Approved")) {
                    Intent jadwal = new Intent(getActivity(), CariKlien.class);
                    startActivity(jadwal);
                } else {
                    alertNotApproved();
                }
                break;
            case R.id.jadwalSaya:
                if (statusApprove.equals("Approved")) {
                    Intent layanan = new Intent(getActivity(), JadwalSaya.class);
                    startActivity(layanan);
                } else {
                    alertNotApproved();
                }
                break;
            case R.id.ubahBiodata:
                if (statusApprove.equals("Approved")) {
                    Intent biodata = new Intent(getActivity(), UbahBiodata.class);
                    startActivity(biodata);
                } else {
                    alertNotApproved();
                }
                break;
            case R.id.layananPsikolog:
                if (statusApprove.equals("Approved")) {
                    Intent keahlian = new Intent(getActivity(), KeahlianPsikolog.class);
                    startActivity(keahlian);
                } else {
                    alertNotApproved();
                }
                break;
          /*  case    R.id.notif2 :
                Intent notif = new Intent(getActivity(), Notifikasi.class);
                startActivity(notif);
                break;*/
            case R.id.notif1:
                Intent notif1 = new Intent(getActivity(), Notifikasi.class);
                startActivity(notif1);
                break;

        }
    }

    public void isApproved() {
        if (statusApprove.equals("Approved")) {
            switchStatusAktif();
        } else {
            alertNotApproved();
            switchStatus.setChecked(false);
            switchStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchStatus.setChecked(false);
                    alertNotApproved();
                }
            });

            Call<ResponseBody> call = RetrofitClient.getInstance().getApi().updateStatusAktif(token, psikolog.getUser_id(), " Tidak Aktif");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "Status Anda  Tidak Aktif", Toast.LENGTH_LONG).show();
                        switchStatus.setChecked(false);
                        //    loadUpdate();
                    } else {
                        switchStatus.setChecked(true);
                        Toast.makeText(getActivity(), "Gagal Terjadi Kesalahan Sistem", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    switchStatus.setChecked(true);
                    Toast.makeText(getActivity(), "Gagal. Periksa Kembali Koneksi Anda", Toast.LENGTH_LONG).show();

                }
            });

        }
    }

    public void switchStatusAktif() {
        switchStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(getActivity(), null, "Sedang Memproses...", true, false);
                if (switchStatus.isChecked()) {
                    Call<ResponseBody> call = RetrofitClient.getInstance().getApi().updateStatusAktif(token, psikolog.getUser_id(), "Aktif");
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            loading.dismiss();
                            if (response.isSuccessful()) {
                                Toast.makeText(getActivity(), "Status Anda Aktif", Toast.LENGTH_LONG).show();
                                switchStatus.setChecked(true);
                                loadUpdate();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                switchStatus.setChecked(false);
                                Toast.makeText(getActivity(), "Gagal Terjadi Kesalahan Sistem", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            switchStatus.setChecked(false);
                            loading.dismiss();

                            Toast.makeText(getActivity(), "Gagal. Periksa Kembali Koneksi Anda", Toast.LENGTH_LONG).show();

                        }
                    });
                } else {
                    Call<ResponseBody> call = RetrofitClient.getInstance().getApi().updateStatusAktif(token, psikolog.getUser_id(), " Tidak Aktif");
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            loading.dismiss();
                            if (response.isSuccessful()) {
                                Toast.makeText(getActivity(), "Status Anda  Tidak Aktif", Toast.LENGTH_LONG).show();
                                switchStatus.setChecked(false);
                                loadUpdate();
                            } else {
                                switchStatus.setChecked(true);
                                Toast.makeText(getActivity(), "Gagal Terjadi Kesalahan Sistem", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            switchStatus.setChecked(true);
                            loading.dismiss();

                            Toast.makeText(getActivity(), "Gagal. Periksa Kembali Koneksi Anda", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });
    }

    public void loadDetails() {
        retrofit2.Call<Response_Details> call = RetrofitClient
                .getInstance()
                .getApi()
                .getDetails(token);
        call.enqueue(new retrofit2.Callback<Response_Details>() {
            @Override
            public void onResponse(retrofit2.Call<Response_Details> call, retrofit2.Response<Response_Details> response) {
                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(getActivity()).saveDetails(response.body().getDetails());
                    statusApprove = response.body().getDetails().getStatus();
                    refresh_count++;
                    swiperefresh.setRefreshing(false);
                    if (refresh_count > 3) {
                        refresh_count = 0;
                        swiperefresh.setRefreshing(false);
                    }

                } else {
                    Toast.makeText(getActivity(), "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
                    swiperefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Response_Details> call, Throwable t) {
                Toast.makeText(getActivity(), "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
                swiperefresh.setRefreshing(false);
            }
        });

//        refresh_count++;
//        if (refresh_count > 3){
//            refresh_count = 0;
//        swiperefresh.setRefreshing(false);
//        }
    }

    public void loadUpdate() {
        retrofit2.Call<Response_Details> call = RetrofitClient
                .getInstance()
                .getApi()
                .getDetails(token);
        call.enqueue(new retrofit2.Callback<Response_Details>() {
            @Override
            public void onResponse(retrofit2.Call<Response_Details> call, retrofit2.Response<Response_Details> response) {
                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(getActivity()).saveDetails(response.body().getDetails());
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Response_Details> call, Throwable t) {
                Toast.makeText(getActivity(), "Error, Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void alertNotApproved() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Akun Anda Belum Terverifikasi")
                .setMessage(" Akun Anda Belum Terverifikasi, Silahkan Hubungi Administrator Sistem Untuk Melakukan Verifikasi Data Psikolog ")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDetails();
        isApproved();
        if (details.getIsActive().equals("Aktif")) {
            switchStatus.setChecked(true);
        } else {
            switchStatus.setChecked(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        cekConnectivity();
        // showResultDialogue();
    }

    private void cekConnectivity() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Snackbar.make(getActivity().findViewById(R.id.homeFragment), "Anda Terhubung", Snackbar.LENGTH_LONG);
        } else {
            Snackbar.make(getActivity().findViewById(R.id.homeFragment), "Koneksi Terputus, Anda Tidak Terhubung Dengan Layanan Internet", Snackbar.LENGTH_LONG).show();

        }
    }

    public void showResultDialogue() {
        AlertDialog.Builder builder;
        SharedPreferences sharedPref = getActivity().getSharedPreferences("linkFoto", Context.MODE_PRIVATE);
        String link = sharedPref.getString("link", "psikologi.ridwan.info/images/");
        final String url = link + details.getFoto();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Token FCM")
                .setMessage("Token FCM " + url)
                .setPositiveButton("Copy Token", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Hasil Scanning", url);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getActivity(), "Hasil Scan Berhasil Ter-Copy", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

}


