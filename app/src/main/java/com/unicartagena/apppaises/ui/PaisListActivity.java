package com.unicartagena.apppaises.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
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
    private PaisAdapter adapter;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pais_list);

        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        rvPaises = findViewById(R.id.rvPaises);
        rvPaises.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnAgregarPais).setOnClickListener(v -> {
            Intent intent = new Intent(this, PaisFormActivity.class);
            startActivity(intent);
        });

        cargarPaises();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPaises();
    }

    private void cargarPaises() {
        String token = "Bearer " + prefs.getString("token", "");
        RetrofitClient.getApiService().listarPaises(token).enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new PaisAdapter(response.body(), prefs, PaisListActivity.this);
                    rvPaises.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                Toast.makeText(PaisListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}