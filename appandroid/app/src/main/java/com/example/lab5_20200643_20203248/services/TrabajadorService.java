package com.example.lab5_20200643_20203248.services;

import com.example.lab5_20200643_20203248.entity.TrabajadorEntity;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TrabajadorService {
    @GET("/trabajador/tutoria/{codigo}")
    Call<HashMap<String, String>> getTutoria(@Path("codigo") int codigo);
    @GET("/trabajador/{codigoTrabajador}")
    Call<List<TrabajadorEntity>> getTrabajadorByCodigo(@Path("codigoTrabajador") int codigoTrabajador);

    @FormUrlEncoded
    @POST("/trabajador/tutoria/")
    Call<HashMap<String, String>> postFeedback(@Field("codigoTrabajador") int codigoTrabajador,
                                               @Field("feedback") String feedback);
}
