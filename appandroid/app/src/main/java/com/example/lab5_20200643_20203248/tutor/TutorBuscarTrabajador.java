package com.example.lab5_20200643_20203248.tutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.lab5_20200643_20203248.R;
import com.example.lab5_20200643_20203248.databinding.ActivityTutorBuscarTrabajadorBinding;
import com.example.lab5_20200643_20203248.entity.TrabajadorEntity;
import com.example.lab5_20200643_20203248.services.TutorService;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TutorBuscarTrabajador extends AppCompatActivity {
    private ActivityTutorBuscarTrabajadorBinding binding;
    private TutorService tutorService;
    private final String HOST = "10.0.2.2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorBuscarTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbarTutorBuscarTrabaj);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tutorService = new Retrofit.Builder()
                .baseUrl("http://"+HOST+":8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TutorService.class);

        binding.buttonDescargar.setOnClickListener(v -> {
            if (accesoInternet()){
                String codigo = binding.inputCodigoTrabajador.getEditText().getText().toString();
                if (!TextUtils.isEmpty(codigo)){
                    fetchTrabajador(codigo);
                }
                else{
                    Toast.makeText(TutorBuscarTrabajador.this, "Ingrese el código de un trabajador", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(TutorBuscarTrabajador.this, "Error: Verifique su conexión con internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTrabajador(String codigo){
        tutorService.getTrabajador(Integer.parseInt(codigo)).enqueue(new Callback<List<TrabajadorEntity>>() {
            @Override
            public void onResponse(Call<List<TrabajadorEntity>> call, Response<List<TrabajadorEntity>> response) {
                if (response.isSuccessful()){
                    if (response.body().size() > 0){
                        TrabajadorEntity trabajador = response.body().get(0);
                        guardarEnMemoria(trabajador);
                    }
                    else{
                        Toast.makeText(TutorBuscarTrabajador.this, "Trabajdor no encontrado", Toast.LENGTH_SHORT).show();
                        Log.d("msg-test", "trabajador no encontrado");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TrabajadorEntity>> call, Throwable t) {
                Toast.makeText(TutorBuscarTrabajador.this, "Ocurrió un error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("msg-test", "error: "+t.getMessage());
            }
        });
    }

    private void guardarEnMemoria(TrabajadorEntity trabajador){
        Gson gson = new Gson();
        String trabajadorJson = gson.toJson(trabajador);
        String fileName = "informacionDe"+trabajador.getEmployee_id();

        try (FileOutputStream fileOutputStream = this.openFileOutput(fileName, Context.MODE_PRIVATE);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD())){
            fileWriter.write(trabajadorJson);
            Toast.makeText(TutorBuscarTrabajador.this, "Información de trabajador guardada", Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private boolean accesoInternet(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        return tieneInternet;
    }
}