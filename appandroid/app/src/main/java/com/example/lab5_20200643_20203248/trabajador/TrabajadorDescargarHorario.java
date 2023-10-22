package com.example.lab5_20200643_20203248.trabajador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.lab5_20200643_20203248.R;

public class TrabajadorDescargarHorario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajador_descargar_horario);


        Toolbar toolbar = findViewById(R.id.toolbarTrabajdorHorario);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}