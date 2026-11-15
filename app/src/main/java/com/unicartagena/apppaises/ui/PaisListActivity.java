package com.unicartagena.apppaises.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unicartagena.apppaises.R;
import com.unicartagena.apppaises.adapter.PaisAdapter;
import com.unicartagena.apppaises.api.RetrofitClient;
import com.unicartagena.apppaises.model.Pais;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaisListActivity extends AppCompatActivity {

    private RecyclerView rvPaises;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pais_list);

        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        rvPaises = findViewById(R.id.rvPaises);
        rvPaises.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnAgregarPais).setOnClickListener(v ->
                startActivity(new Intent(this, PaisFormActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPaises(); // Solo aquí, NO en onCreate
    }

    private void cargarPaises() {
        String token = "Bearer " + prefs.getString("token", "");
        RetrofitClient.getApiService().listarPaises(token).enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rvPaises.setAdapter(new PaisAdapter(response.body(), prefs, PaisListActivity.this));
                } else if (response.code() == 401) {
                    prefs.edit().remove("token").apply();
                    startActivity(new Intent(PaisListActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(PaisListActivity.this, "Error " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                Toast.makeText(PaisListActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}