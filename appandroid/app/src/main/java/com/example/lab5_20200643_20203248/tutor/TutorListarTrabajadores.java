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
import com.example.lab5_20200643_20203248.databinding.ActivityTutorListarTrabajadoresBinding;
import com.example.lab5_20200643_20203248.entity.TrabajadorEntity;
import com.example.lab5_20200643_20203248.services.TutorService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TutorListarTrabajadores extends AppCompatActivity {
    private ActivityTutorListarTrabajadoresBinding binding;
    private TutorService tutorService;
    private final String HOST = "192.168.1.9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorListarTrabajadoresBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbarListaTrabaja);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tutorService = new Retrofit.Builder()
                .baseUrl("http://"+HOST+":8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TutorService.class);

        binding.buttonDescargar.setOnClickListener(v -> {
            if (accesoInternet()){
                String codigo = binding.inputCodigoTutor.getEditText().getText().toString();
                fetchTrabajadores(codigo);
            }
            else{
                Toast.makeText(TutorListarTrabajadores.this, "Error: Verifique su conexión con internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTrabajadores(String codigo){
        tutorService.getTrabajadores(Integer.parseInt(codigo)).enqueue(new Callback<List<TrabajadorEntity>>() {
            @Override
            public void onResponse(Call<List<TrabajadorEntity>> call, Response<List<TrabajadorEntity>> response) {
                if (response.isSuccessful()){
                    List<TrabajadorEntity> trabajadores = response.body();
                    guardarEnMemoria(trabajadores);
                }
            }

            @Override
            public void onFailure(Call<List<TrabajadorEntity>> call, Throwable t) {
                Log.d("msg-test", "error: "+t.getMessage());
            }
        });
    }

    private void guardarEnMemoria(List<TrabajadorEntity> trabajadores){

    }

    private boolean accesoInternet(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        return tieneInternet;
    }
}