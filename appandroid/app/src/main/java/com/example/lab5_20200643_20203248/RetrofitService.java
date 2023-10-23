package com.example.lab5_20200643_20203248;

import com.example.lab5_20200643_20203248.entity.TrabajadorEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {
    @GET("/trabajador/{codigoTrabajador}")
    Call<TrabajadorEntity> getTrabajadorByCodigo(@Path("codigoTrabajador") int codigoTrabajador);
}
