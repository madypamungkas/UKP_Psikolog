package com.komsi.psikolog_interface.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.komsi.psikolog_interface.Activities.Dashboard;
import com.komsi.psikolog_interface.Activities.EditActivity.EditAkun;
import com.komsi.psikolog_interface.Activities.Password.GantiPassword;
import com.komsi.psikolog_interface.Activities.EditActivity.UploadFoto;
import com.komsi.psikolog_interface.Activities.VerifEmail.SendEmailVerif;
import com.komsi.psikolog_interface.Models.Details;
import com.komsi.psikolog_interface.R;
import com.komsi.psikolog_interface.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import static android.content.Context.CONNECTIVITY_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {


    public AccountFragment() {
        // Required empty public constructor
    }
    private ConnectivityManager connectivityManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_account, container, false);
        View fragmentView = inflater.inflate(R.layout.fragment_account, container, false);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);

        Details details = SharedPrefManager.getInstance(getActivity()).getDetails();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("DataPsikolog", Context.MODE_PRIVATE);
        String gelar = sharedPref.getString("gelar", null);
        RelativeLayout logout = (RelativeLayout) fragmentView.findViewById(R.id.logout);
        TextView accUsername = (TextView) fragmentView.findViewById(R.id.accUsername);
        TextView accNohp = (TextView) fragmentView.findViewById(R.id.accNohp);
        TextView accEmail = (TextView) fragmentView.findViewById(R.id.accEmail);
        LinearLayout edit_akun = (LinearLayout) fragmentView.findViewById(R.id.edit_akun);
        TextView ubah_foto = (TextView) fragmentView.findViewById(R.id.ubah_foto);
        LinearLayout ganti = (LinearLayout) fragmentView.findViewById(R.id.gantipass);
        LinearLayout about = (LinearLayout) fragmentView.findViewById(R.id.about);

        ImageView avataracc = (ImageView) fragmentView.findViewById(R.id.avatar);
        SharedPreferences sharedPrefFCM = getActivity().getSharedPreferences("linkFoto", Context.MODE_PRIVATE);
        String link = sharedPrefFCM.getString("link", null);
        final String url = "http://psikologi.ridwan.info/images/" + details.getFoto();
        Picasso.with(getActivity()).load(url).error(R.drawable.menu_icon_user).into(avataracc);


        accUsername.setText(details.getUsername());
        accEmail.setText(details.getEmail());
        accNohp.setText(details.getNo_telepon());

        ganti.setOnClickListener((View.OnClickListener) this);
        about.setOnClickListener((View.OnClickListener) this);
        edit_akun.setOnClickListener((View.OnClickListener) this);
        ubah_foto.setOnClickListener((View.OnClickListener) this);
        logout.setOnClickListener((View.OnClickListener) this);
        return fragmentView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gantipass:
                Intent ganti = new Intent(getActivity(), GantiPassword.class);
                startActivity(ganti);
                break;
            case R.id.about:
                Intent about = new Intent(getActivity(), SendEmailVerif.class);
                startActivity(about);
                break;
            case R.id.edit_akun:
                Intent edit = new Intent(getActivity(), EditAkun.class);
                startActivity(edit);
                break;
            case R.id.logout:
                SharedPrefManager.getInstance(getActivity()).clear();
                Intent intent = new Intent(getActivity(), Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.ubah_foto:
                Intent uploadFoto = new Intent(getActivity(), UploadFoto.class);
                startActivity(uploadFoto);
                break;

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        cekConnectivity();
    }

    private void cekConnectivity() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Snackbar.make(getActivity().findViewById(R.id.accountFragment), "Anda Terhubung", Snackbar.LENGTH_LONG);
        } else {
            Snackbar.make(getActivity().findViewById(R.id.accountFragment), "Koneksi Terputus, Anda Tidak Terhubung Dengan Layanan Internet", Snackbar.LENGTH_LONG).show();

        }
    }


}
