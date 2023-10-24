package com.example.lab5_20200643_20203248.tutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.lab5_20200643_20203248.R;
import com.example.lab5_20200643_20203248.databinding.ActivityTutorAsigarTutoriaBinding;
import com.example.lab5_20200643_20203248.entity.TrabajadorEntity;
import com.example.lab5_20200643_20203248.services.TutorService;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TutorAsigarTutoria extends AppCompatActivity {
    private ActivityTutorAsigarTutoriaBinding binding;
    private TutorService tutorService;
    private final String HOST = "10.0.2.2";
    String IDcanalTutor = "channelTutor";
    Button asignar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorAsigarTutoriaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbarTutorAsignarTutorial);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tutorService = new Retrofit.Builder()
                .baseUrl("http://"+HOST+":8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TutorService.class);

        asignar = binding.buttonAsignarTutor;

        asignar.setOnClickListener(v -> {
            if (accesoInternet()){
                String codigoTutor = binding.inputCoidigoTutor.getEditText().getText().toString();
                String codigoTrabajador = binding.inputCoidigoTrabajador.getEditText().getText().toString();

                if (!TextUtils.isEmpty(codigoTutor) && !TextUtils.isEmpty(codigoTrabajador)){
                    postTutoria(codigoTutor, codigoTrabajador);
                    asignar.setEnabled(false);
                }
                else {
                    Toast.makeText(TutorAsigarTutoria.this, "Ingrese su código de tutor y el de un trabajador", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(TutorAsigarTutoria.this, "Error: Verifique su conexión con internet", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void postTutoria(String codigoTutor, String codigoTrabajador){
        tutorService.postTutoria(Integer.parseInt(codigoTutor), Integer.parseInt(codigoTrabajador)).enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (response.isSuccessful()){
                    HashMap<String, String> root = response.body();
                    switch (root.get("msg")){ // envio de notificaciones
                        case "Trabajador inválido": // el tutor no es manager del trabajador
                            Log.d("msg-test","Trabajdor invalido");
                            lanzarNotificacion(IDcanalTutor,"Asiganción tutoría","El tutor no es manager del empleado");
                            asignar.setEnabled(true);
                            break;
                        case "Trabajador ocupado": // el trabajdor ya tiene tutoria
                            Log.d("msg-test","Tutoria ocuapado");
                            lanzarNotificacion(IDcanalTutor,"Asiganción tutoría","El trabajador ya tiene una cita asignada.  Elija otro trabajador.");
                            asignar.setEnabled(true);
                            break;
                        case "ok":
                            Log.d("msg-test","Tutoria asginada");
                            lanzarNotificacion(IDcanalTutor,"Asiganción tutoría","Asignación del trabajador correcta");
                            asignar.setEnabled(true);
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
    private boolean accesoInternet(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        return tieneInternet;
    }
}