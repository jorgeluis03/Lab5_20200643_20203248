package com.example.lab5_20200643_20203248.services;

import com.example.lab5_20200643_20203248.entity.TrabajadorEntity;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TutorService {
    @GET("/tutor/{codigoTutor}")
    public Call<TrabajadorEntity> getTrabajador();
}
