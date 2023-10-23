package com.example.lab5_20200643_20203248.services;

import com.example.lab5_20200643_20203248.entity.TrabajadorEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TutorService {
    @GET("/tutor/{codigo}")
    public Call<List<TrabajadorEntity>> getTrabajadores(@Path("codigo") int codigo);
}
