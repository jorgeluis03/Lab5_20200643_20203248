package com.example.lab5_20200643_20203248.services;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TrabajadorService {
    @FormUrlEncoded
    @POST("/trabajador/tutoria/")
    Call<HashMap<String, String>> postFeedback(@Field("codigoTrabajador") int codigoTrabajador,
                                               @Field("feedback") String feedback);
}
