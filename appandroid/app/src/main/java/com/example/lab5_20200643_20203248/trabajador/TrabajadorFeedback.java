package com.example.lab5_20200643_20203248.trabajador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lab5_20200643_20203248.R;
import com.example.lab5_20200643_20203248.databinding.ActivityTrabajadorFeedbackBinding;
import com.example.lab5_20200643_20203248.services.TrabajadorService;
import com.example.lab5_20200643_20203248.services.TutorService;
import com.example.lab5_20200643_20203248.tutor.TutorAsigarTutoria;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrabajadorFeedback extends AppCompatActivity {
    private ActivityTrabajadorFeedbackBinding binding;
    private TrabajadorService trabajadorService;
    private final String HOST = "192.168.1.9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbarFeedback);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        trabajadorService = new Retrofit.Builder()
                .baseUrl("http://"+HOST+":8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TrabajadorService.class);

        binding.buttonEnviar.setOnClickListener(v -> {
            if (accesoInternet()){
                String codigoTrabajador = binding.inputCodigoTrabajadorFeedback.getEditText().getText().toString();
                String feedback = binding.textAreaFeedback.getEditText().getText().toString();
                postFeedback(codigoTrabajador, feedback);
            }
            else{
                Toast.makeText(TrabajadorFeedback.this, "Error: Verifique su conexión con internet", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void postFeedback(String codigoTrabajador, String feedback){
        trabajadorService.postFeedback(Integer.parseInt(codigoTrabajador), feedback).enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (response.isSuccessful()){
                    HashMap<String, String> root = response.body();
                    switch (root.get("msg")){
                        case "ok":
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

    private boolean accesoInternet(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        return tieneInternet;
    }
}