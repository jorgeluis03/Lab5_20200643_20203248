package com.example.lab5_20200643_20203248.trabajador;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab5_20200643_20203248.R;
import com.example.lab5_20200643_20203248.databinding.ActivityTrabajadorBinding;
import com.example.lab5_20200643_20203248.entity.TrabajadorEntity;
import com.example.lab5_20200643_20203248.services.TrabajadorService;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Trabajador extends AppCompatActivity {
    private final String HOST = "10.0.2.2";
    ActivityTrabajadorBinding binding;
    TrabajadorService trabajadorService;
    TextView nombreTrabajador;
    TextView correoTrabajador;
    EditText codigoTrabajador;
    MaterialCardView feedback;
    TrabajadorEntity trabajadorEcontrado;
    String IDcanalTrabajador = "channelTrabajador";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbarTrabajador);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        trabajadorService = new Retrofit.Builder()
                .baseUrl("http://"+HOST+":8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TrabajadorService.class);

        binding.buttonConsultarTrabajador.setOnClickListener(v -> {
            String codigoTrabajdor = binding.textFieldCodigoTrabajador.getEditText().getText().toString();
            if(!codigoTrabajdor.equals("")){

                obtnerRespuestaDelTrabajador(Integer.parseInt(codigoTrabajdor));

            }else {
                Toast.makeText(this,"Ingrese el codigo de empleado",Toast.LENGTH_SHORT).show();
            }
        });

        binding.cardDescargaHorarios.setOnClickListener(view -> {
            String codigoTrabajdor = binding.textFieldCodigoTrabajador.getEditText().getText().toString();
            if(!codigoTrabajdor.equals("")){

                obtnerRespuestaDeTutoria(Integer.parseInt(codigoTrabajdor));

            }else {
                Toast.makeText(this,"Ingrese el codigo de empleado",Toast.LENGTH_SHORT).show();
            }

        });

        binding.cardFeedback.setOnClickListener(view -> {
            if(trabajadorEcontrado!=null){
                Intent intent = new Intent(this,TrabajadorFeedback.class);
                intent.putExtra("trabajador",trabajadorEcontrado);
                startActivity(intent);
            }

        });
    }

    public void obtnerRespuestaDelTrabajador(int codigo){
        trabajadorService.getTrabajadorByCodigo(codigo).enqueue(new Callback<List<TrabajadorEntity>>() {
            @Override
            public void onResponse(Call<List<TrabajadorEntity>> call, Response<List<TrabajadorEntity>> response) {
                if(response.isSuccessful()){
                    Log.d("msg-test","respuesta success");
                    TrabajadorEntity trabajador = response.body().get(0);
                    trabajadorEcontrado = trabajador;

                    nombreTrabajador = binding.textViewNombre;
                    correoTrabajador = binding.textViewCorreo;
                    codigoTrabajador = binding.textFieldCodigoTrabajador.getEditText();
                    feedback = binding.cardFeedback;


                    nombreTrabajador.setText(trabajador.getFirst_name()+' '+trabajador.getLast_name());
                    correoTrabajador.setText(trabajador.getEmail());

                    if (trabajador.getMeeting()==1){
                        feedback.setVisibility(View.VISIBLE);
                    }else {
                        feedback.setVisibility(View.INVISIBLE);
                    }


                }else {
                    Toast.makeText(Trabajador.this,"algo pasó", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TrabajadorEntity>> call, Throwable t) {

            }
        });
    }
    public void obtnerRespuestaDeTutoria(int codigo){
        trabajadorService.getTutoria(codigo).enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if(response.isSuccessful()){
                    Log.d("msg-test","respuesta success");
                    HashMap<String, String> respuesta = response.body();
                    Log.d("msg-test",respuesta.get("msg"));

                    if(respuesta.get("msg").equals("ok")){
                        if (accesoInternet()){
                            String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE; //si no funciona android.Manifest.permission…
                            launcher.launch(permission);
                        }
                        else{
                            Toast.makeText(Trabajador.this, "Error: Verifique su conexión con internet", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        lanzarNotificacion(IDcanalTrabajador, "Trabajador", "No cuenta con tutorías pendientes");
                    }
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {

            }
        });
    }
    private void lanzarNotificacion(String channel, String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.drawable.icon_inicio)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);
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

    private boolean accesoInternet(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        return tieneInternet;
    }
}