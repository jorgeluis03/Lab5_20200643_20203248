package com.example.lab5_20200643_20203248.trabajador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab5_20200643_20203248.R;
import com.example.lab5_20200643_20203248.databinding.ActivityTrabajadorBinding;

public class Trabajador extends AppCompatActivity {
    ActivityTrabajadorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbarTrabajador);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.cardDescargaHorarios.setOnClickListener(view -> {
            //Descargar imagen
        });

        binding.cardFeedback.setOnClickListener(view -> {
            Intent intent = new Intent(this,TrabajadorFeedback.class);
            startActivity(intent);
        });
    }
}