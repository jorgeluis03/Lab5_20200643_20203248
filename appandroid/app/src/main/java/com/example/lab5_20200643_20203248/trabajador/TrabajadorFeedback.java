package com.example.lab5_20200643_20203248.trabajador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab5_20200643_20203248.R;
import com.example.lab5_20200643_20203248.databinding.ActivityTrabajadorFeedbackBinding;
import com.example.lab5_20200643_20203248.entity.TrabajadorEntity;
import com.example.lab5_20200643_20203248.services.TrabajadorService;
import com.example.lab5_20200643_20203248.services.TutorService;
import com.example.lab5_20200643_20203248.tutor.TutorAsigarTutoria;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrabajadorFeedback extends AppCompatActivity {
    private ActivityTrabajadorFeedbackBinding binding;
    private TrabajadorService trabajadorService;
    TrabajadorEntity trabajador;
    TextView fechaTutoria;
    TextInputLayout textInputFeedback;
    private final String HOST = "192.168.18.44";
    String IDcanalTrabajador = "channelTrabajador";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbarFeedback);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textInputFeedback = binding.textAreaFeedback;

        trabajadorService = new Retrofit.Builder()
                .baseUrl("http://"+HOST+":8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TrabajadorService.class);

        trabajador = (TrabajadorEntity) getIntent().getSerializableExtra("trabajador");
        if(trabajador.getEmployee_feedback()!=null){

            textInputFeedback.getEditText().setText(trabajador.getEmployee_feedback());
            textInputFeedback.setEnabled(false);

        }else {
            binding.buttonEnviar.setOnClickListener(v -> {

                if (accesoInternet()){


                    String codigoTrabajador = String.valueOf(trabajador.getEmployee_id());
                    String feedback = binding.textAreaFeedback.getEditText().getText().toString();

                    postFeedback(codigoTrabajador, feedback);
                    textInputFeedback.setEnabled(false);
                }
                else{
                    Toast.makeText(TrabajadorFeedback.this, "Error: Verifique su conexión con internet", Toast.LENGTH_SHORT).show();
                }
            });

        }






    }

    private void postFeedback(String codigoTrabajador, String feedback){
        trabajadorService.postFeedback(Integer.parseInt(codigoTrabajador), feedback).enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (response.isSuccessful()){
                    HashMap<String, String> root = response.body();
                    switch (root.get("msg")){
                        case "ok":
                            Log.d("msg-test","respues ok");
                            lanzarNotificacion(IDcanalTrabajador,"Feecback Trabajador","Feedback enviado de manera exitosa");
                            break;
                        case "Tutoria inválida": // cuando la tutoria ya tiene feedback o la tutoria aun no transcurre (fecha tutoria > fecha actual)
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