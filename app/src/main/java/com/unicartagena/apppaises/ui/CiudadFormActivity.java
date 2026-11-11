package com.unicartagena.apppaises.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unicartagena.apppaises.R;
import com.unicartagena.apppaises.api.RetrofitClient;
import com.unicartagena.apppaises.model.Ciudad;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CiudadFormActivity extends AppCompatActivity {

    private EditText etNombre, etPais, etAlcalde, etClima;
    private SharedPreferences prefs;
    private Long ciudadId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciudad_form);

        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        etNombre = findViewById(R.id.etNombre);
        etPais = findViewById(R.id.etPais);
        etAlcalde = findViewById(R.id.etAlcalde);
        etClima = findViewById(R.id.etClima);

        if (getIntent().hasExtra("ciudad")) {
            Ciudad ciudad = (Ciudad) getIntent().getSerializableExtra("ciudad");
            if (ciudad != null) {
                ciudadId = ciudad.getId();
                etNombre.setText(ciudad.getNombre());
                etPais.setText(ciudad.getPaisPertenece());
                etAlcalde.setText(ciudad.getAlcalde());
                etClima.setText(ciudad.getClima());
            }
        }

        findViewById(R.id.btnGuardar).setOnClickListener(v -> guardarCiudad());
    }

    private void guardarCiudad() {
        String token = "Bearer " + prefs.getString("token", "");

        Ciudad ciudad = new Ciudad();
        ciudad.setNombre(etNombre.getText().toString());
        ciudad.setPaisPertenece(etPais.getText().toString());
        ciudad.setAlcalde(etAlcalde.getText().toString());
        ciudad.setClima(etClima.getText().toString());

        Call<Ciudad> call;
        if (ciudadId == null) {
            call = RetrofitClient.getApiService().crearCiudad(token, ciudad);
        } else {
            call = RetrofitClient.getApiService().actualizarCiudad(token, ciudadId, ciudad);
        }

        call.enqueue(new Callback<Ciudad>() {
            @Override
            public void onResponse(Call<Ciudad> call, Response<Ciudad> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CiudadFormActivity.this, "Guardado exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CiudadFormActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Ciudad> call, Throwable t) {
                Toast.makeText(CiudadFormActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}