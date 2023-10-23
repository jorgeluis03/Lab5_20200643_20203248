package com.example.lab5_20200643_20203248.trabajador;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.lab5_20200643_20203248.R;
import com.example.lab5_20200643_20203248.databinding.ActivityTrabajadorDescargarHorarioBinding;
import com.example.lab5_20200643_20203248.services.TrabajadorService;

import java.io.File;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrabajadorDescargarHorario extends AppCompatActivity {
    private ActivityTrabajadorDescargarHorarioBinding binding;
    private TrabajadorService trabajadorService;
    private final String HOST = "192.168.1.9";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorDescargarHorarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbarTrabajdorHorario);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        trabajadorService = new Retrofit.Builder()
                .baseUrl("http://"+HOST+":8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TrabajadorService.class);

        binding.buttonDecargarHorario.setOnClickListener(v -> {
            if (accesoInternet()){
                String codigoTrabajador = binding.inputCodigoTrabajador.getEditText().getText().toString();
                fetchExisteTutoria(codigoTrabajador);

                //String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE; //si no funciona android.Manifest.permission…
                //launcher.launch(permission);
            }
            else{
                Toast.makeText(TrabajadorDescargarHorario.this, "Error: Verifique su conexión con internet", Toast.LENGTH_SHORT).show();
            }
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

    private void fetchExisteTutoria(String codigoTrabajador){
        trabajadorService.getTutoria(Integer.parseInt(codigoTrabajador)).enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (response.isSuccessful()){
                    HashMap<String, String> root = response.body();
                    switch (root.get("msg")){
                        case "ok": // puede descargar los horarios
                            break;
                        case "No cuenta con tutoria": // ejecutar notificacion
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Log.d("msg-test", "error: "+t.getMessage());
            }
        });
    }

    private boolean accesoInternet(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        return tieneInternet;
    }

}