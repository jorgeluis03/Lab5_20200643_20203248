package com.example.lab5_20200643_20203248;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.lab5_20200643_20203248.databinding.ActivityMainBinding;
import com.example.lab5_20200643_20203248.trabajador.Trabajador;
import com.example.lab5_20200643_20203248.tutor.Tutor;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    String IDcanalTrabajador = "channelTrabajador";
    String IDcanalTutor = "channelTutor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        crearCanalesNotificacion();

        binding.buttonTutor.setOnClickListener(view -> {
            lanzarNotificacionTutor();
            Intent intent = new Intent(this, Tutor.class);
            startActivity(intent);
        });
        binding.buttonTrabajador.setOnClickListener(view -> {
            lanzarNotificacionTrabajador();
            Intent intent = new Intent(this, Trabajador.class);
            startActivity(intent);
        });

    }
    public void crearCanalesNotificacion() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Canal para notificaciones de Trabajador
            NotificationChannel canalTrabajador = new NotificationChannel(IDcanalTrabajador,
                    "Canal notificaciones Trabajador",
                    NotificationManager.IMPORTANCE_HIGH);
            canalTrabajador.setDescription("Canal para notificaciones de Trabajador");

            // Canal para notificaciones de Tutor
            NotificationChannel canalTutor = new NotificationChannel(IDcanalTutor,
                    "Canal notificaciones Tutor",
                    NotificationManager.IMPORTANCE_HIGH);
            canalTutor.setDescription("Canal para notificaciones de Tutor");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(canalTrabajador);
            notificationManager.createNotificationChannel(canalTutor);

            pedirPermisos();
        }
    }

    public void pedirPermisos() {
        //android.os.Build.VERSION_CODES.TIRAMISU == 33
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{POST_NOTIFICATIONS},
                    101);
        }
    }


    private void lanzarNotificacion(String channel, String title, String content) {
        //Crear notificación
        //Intent intent = new Intent(this, MainActivity2.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.drawable.icon_inicio)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification notification = builder.build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, notification);
        }
    }

    public void lanzarNotificacionTrabajador() {
        lanzarNotificacion(IDcanalTrabajador, "Trabajador", "Está entrando en modo Empleado");
    }

    public void lanzarNotificacionTutor() {
        lanzarNotificacion(IDcanalTutor, "Tutor", "Está entrando en modo Tutor");
    }



}