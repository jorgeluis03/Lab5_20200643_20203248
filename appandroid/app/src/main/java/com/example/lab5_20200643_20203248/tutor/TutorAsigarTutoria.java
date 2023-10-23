package com.example.lab5_20200643_20203248.tutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lab5_20200643_20203248.R;
import com.example.lab5_20200643_20203248.databinding.ActivityTutorAsigarTutoriaBinding;
import com.example.lab5_20200643_20203248.services.TutorService;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TutorAsigarTutoria extends AppCompatActivity {
    private ActivityTutorAsigarTutoriaBinding binding;
    private TutorService tutorService;
    private final String HOST = "192.168.1.9";

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

        binding.buttonAsignarTutor.setOnClickListener(v -> {
            if (accesoInternet()){
                String codigoTutor = binding.inputCoidigoTutor.getEditText().getText().toString();
                String codigoTrabajador = binding.inputCoidigoTrabajador.getEditText().getText().toString();
                postTutoria(codigoTutor, codigoTrabajador);
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

                            break;
                        case "Trabajador ocupado": // el trabajdor ya tiene tutoria

                            break;
                        case "ok":

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