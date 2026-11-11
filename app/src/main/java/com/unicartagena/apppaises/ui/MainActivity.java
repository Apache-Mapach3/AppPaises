package com.unicartagena.apppaises.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unicartagena.apppaises.R;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        findViewById(R.id.btnPaises).setOnClickListener(v ->
                startActivity(new Intent(this, PaisListActivity.class)));

        findViewById(R.id.btnCiudades).setOnClickListener(v ->
                startActivity(new Intent(this, CiudadListActivity.class)));

        findViewById(R.id.btnReportes).setOnClickListener(v ->
                Toast.makeText(this, "Próximamente: Reportes", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnSensores).setOnClickListener(v ->
                Toast.makeText(this, "Próximamente: Sensores", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnCerrarSesion).setOnClickListener(v -> {
            prefs.edit().remove("token").apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}