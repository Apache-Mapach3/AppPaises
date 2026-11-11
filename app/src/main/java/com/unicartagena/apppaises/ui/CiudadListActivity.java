package com.unicartagena.apppaises.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unicartagena.apppaises.R;
import com.unicartagena.apppaises.adapter.CiudadAdapter;
import com.unicartagena.apppaises.api.RetrofitClient;
import com.unicartagena.apppaises.model.Ciudad;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CiudadListActivity extends AppCompatActivity {

    private RecyclerView rvCiudades;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciudad_list);

        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        rvCiudades = findViewById(R.id.rvCiudades);
        rvCiudades.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnAgregarCiudad).setOnClickListener(v -> {
            Intent intent = new Intent(this, CiudadFormActivity.class);
            startActivity(intent);
        });

        cargarCiudades();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarCiudades();
    }

    private void cargarCiudades() {
        String token = "Bearer " + prefs.getString("token", "");
        RetrofitClient.getApiService().listarCiudades(token).enqueue(new Callback<List<Ciudad>>() {
            @Override
            public void onResponse(Call<List<Ciudad>> call, Response<List<Ciudad>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rvCiudades.setAdapter(new CiudadAdapter(response.body(), prefs, CiudadListActivity.this));
                }
            }

            @Override
            public void onFailure(Call<List<Ciudad>> call, Throwable t) {
                Toast.makeText(CiudadListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}