package com.unicartagena.apppaises.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unicartagena.apppaises.R;
import com.unicartagena.apppaises.api.RetrofitClient;
import com.unicartagena.apppaises.model.Ciudad;
import com.unicartagena.apppaises.model.Pais;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportesActivity extends AppCompatActivity {

    private TextView tvTotalPaises, tvTotalCiudades, tvPromedioHabitantes;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        tvTotalPaises = findViewById(R.id.tvTotalPaises);
        tvTotalCiudades = findViewById(R.id.tvTotalCiudades);
        tvPromedioHabitantes = findViewById(R.id.tvPromedioHabitantes);

        findViewById(R.id.btnRefrescarReportes).setOnClickListener(v -> cargarDatos());

        cargarDatos();
    }

    private void cargarDatos() {
        String token = "Bearer " + prefs.getString("token", "");

        // Cargar Países
        RetrofitClient.getApiService().listarPaises(token).enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pais> paises = response.body();
                    tvTotalPaises.setText(String.valueOf(paises.size()));

                    // Calcular promedio de habitantes
                    long totalHab = paises.stream().mapToLong(Pais::getNumHabitantes).sum();
                    long promedio = paises.isEmpty() ? 0 : totalHab / paises.size();
                    tvPromedioHabitantes.setText(String.format("%,d habitantes", promedio));
                }
            }
            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                Toast.makeText(ReportesActivity.this, "Error al cargar países", Toast.LENGTH_SHORT).show();
            }
        });

        // Cargar Ciudades
        RetrofitClient.getApiService().listarCiudades(token).enqueue(new Callback<List<Ciudad>>() {
            @Override
            public void onResponse(Call<List<Ciudad>> call, Response<List<Ciudad>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvTotalCiudades.setText(String.valueOf(response.body().size()));
                }
            }
            @Override
            public void onFailure(Call<List<Ciudad>> call, Throwable t) {
                Toast.makeText(ReportesActivity.this, "Error al cargar ciudades", Toast.LENGTH_SHORT).show();
            }
        });
    }
}