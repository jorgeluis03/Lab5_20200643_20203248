package com.example.lab5_20200643_20203248;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab5_20200643_20203248.databinding.ActivityMainBinding;
import com.example.lab5_20200643_20203248.trabajador.Trabajador;
import com.example.lab5_20200643_20203248.tutor.Tutor;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonTutor.setOnClickListener(view -> {
            Intent intent = new Intent(this, Tutor.class);
            startActivity(intent);
        });
        binding.buttonTrabajador.setOnClickListener(view -> {
            Intent intent = new Intent(this, Trabajador.class);
            startActivity(intent);
        });

    }
}