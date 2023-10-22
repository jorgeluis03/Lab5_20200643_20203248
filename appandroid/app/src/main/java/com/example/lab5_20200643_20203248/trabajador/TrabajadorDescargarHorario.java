package com.example.lab5_20200643_20203248.trabajador;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import com.example.lab5_20200643_20203248.R;
import com.example.lab5_20200643_20203248.databinding.ActivityTrabajadorDescargarHorarioBinding;

import java.io.File;

public class TrabajadorDescargarHorario extends AppCompatActivity {
    ActivityTrabajadorDescargarHorarioBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorDescargarHorarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = findViewById(R.id.toolbarTrabajdorHorario);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.buttonDecargarHorario.setOnClickListener(v -> {
            String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE; //si no funciona android.Manifest.permission…
            launcher.launch(permission);
        });
    }


    ActivityResultLauncher<String> launcher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {

                if (isGranted) { // permiso concedido
                    descargarConDownloadManager();
                } else {
                    Log.e("msg-test", "Permiso denegado");
                }
            });

    public void descargarConDownloadManager() {

        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE; //si no funciona android.Manifest.permission…

        // >29
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            //si tengo permisos
            String fileName = "horario.jpg";
            String endPoint = "https://i.pinimg.com/564x/4e/8e/a5/4e8ea537c896aa277e6449bdca6c45da.jpg";

            Uri downloadUri = Uri.parse(endPoint);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setTitle(fileName);
            request.setMimeType("image/jpeg");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + fileName);

            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            dm.enqueue(request);
        } else {
            //si no tiene permisos
            launcher.launch(permission);
        }
    }

}