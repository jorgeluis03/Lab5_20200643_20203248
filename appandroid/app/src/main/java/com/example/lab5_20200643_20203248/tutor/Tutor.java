package com.example.lab5_20200643_20203248.tutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab5_20200643_20203248.R;
import com.example.lab5_20200643_20203248.databinding.ActivityTutorBinding;
import com.google.android.material.card.MaterialCardView;

public class Tutor extends AppCompatActivity {
    ActivityTutorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbarTutor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.cardDescargarLista.setOnClickListener(view -> {
            Intent intent = new Intent(this,TutorListarTrabajadores.class);
            startActivity(intent);
        });

        binding.cardBuscarDelegado.setOnClickListener(view -> {
            Intent intent = new Intent(this, TutorBuscarTrabajador.class);
            startActivity(intent);
        });

        binding.cardAsignarTutoria.setOnClickListener(view -> {
            Intent intent = new Intent(this, TutorAsigarTutoria.class);
            startActivity(intent);
        });

    }
}