package com.example.lab5_20200643_20203248.trabajador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.lab5_20200643_20203248.R;
import com.example.lab5_20200643_20203248.RetrofitService;
import com.example.lab5_20200643_20203248.databinding.ActivityTrabajadorBinding;
import com.example.lab5_20200643_20203248.entity.TrabajadorEntity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Trabajador extends AppCompatActivity {
    ActivityTrabajadorBinding binding;
    RetrofitService retrofitService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbarTrabajador);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.buttonConsultarTrabajador.setOnClickListener(v -> {
            String codigoTrabajdor = binding.textFieldCodigoTrabajador.getEditText().getText().toString();

            retrofitService = new Retrofit.Builder()
                    .baseUrl("http://192.168.18.44:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RetrofitService.class);

            obtnerRespuestaRetrofit(Integer.parseInt(codigoTrabajdor));

        });

        binding.cardDescargaHorarios.setOnClickListener(view -> {

            Intent intent = new Intent(this, TrabajadorDescargarHorario.class);
            startActivity(intent);
        });

        binding.cardFeedback.setOnClickListener(view -> {
            Intent intent = new Intent(this,TrabajadorFeedback.class);
            startActivity(intent);
        });
    }

    public void obtnerRespuestaRetrofit(int codigo){
        retrofitService.getTrabajadorByCodigo(codigo).enqueue(new Callback<TrabajadorEntity>() {
            @Override
            public void onResponse(Call<TrabajadorEntity> call, Response<TrabajadorEntity> response) {
                // Verificar si la respuesta es exitosa
                if (response.isSuccessful()) {

                    TrabajadorEntity userData = response.body();
                    if (userData != null ) {
                        TrabajadorEntity trabajador = userData;

                        /*TextInputEditText nombre = binding.textFieldNombre.findViewById(R.id.textInputEditTextNombre);
                        TextInputEditText apellido = binding.textFieldApelldio.findViewById(R.id.textInputEditTextApellido);
                        TextInputEditText correo = binding.textFieldCorreo.findViewById(R.id.textInputEditTextCorreo);
                        TextInputEditText password = binding.textFieldPassword.findViewById(R.id.textInputEditTextPassword);

                        binding.urlFoto.setText(userProfile.getPicture().getFoto());
                        nombre.setText(userProfile.getName().getNombre());
                        apellido.setText(userProfile.getName().getApellido());
                        correo.setText(userProfile.getEmail());
                        password.setText(userProfile.getLogin().getPassword());*/

                        Log.d("tag_smg", "Nombre: " + trabajador.getFirst_name());
                        Log.d("tag_smg", "Apellido: " + trabajador.getLast_name());
                        Log.d("tag_smg", "Correo: " + trabajador.getEmail());
                        Log.d("tag_smg", "Manager: " + trabajador.getManager_id());
                    }

                }
            }

            @Override
            public void onFailure(Call<TrabajadorEntity> call, Throwable t) {

            }

        });
    }

}