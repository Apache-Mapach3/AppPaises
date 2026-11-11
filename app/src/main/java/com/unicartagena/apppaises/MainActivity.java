package com.unicartagena.apppaises;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unicartagena.apppaises.ui.CiudadListActivity;
import com.unicartagena.apppaises.ui.LoginActivity;
import com.unicartagena.apppaises.ui.PaisListActivity;
import com.unicartagena.apppaises.ui.ReportesActivity;
import com.unicartagena.apppaises.ui.SensoresActivity;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recuperar el token guardado
        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        // Si NO hay token, mandar al usuario al Login
        if (prefs.getString("token", null) == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // Cierra esta actividad para que no pueda volver atrás con el botón "Atrás"
            return;
        }


        findViewById(R.id.btnPaises).setOnClickListener(v ->
                startActivity(new Intent(this, PaisListActivity.class)));

        findViewById(R.id.btnCiudades).setOnClickListener(v ->
                startActivity(new Intent(this, CiudadListActivity.class)));

        findViewById(R.id.btnReportes).setOnClickListener(v ->
                startActivity(new Intent(this, ReportesActivity.class)));

        findViewById(R.id.btnSensores).setOnClickListener(v ->
                startActivity(new Intent(this, SensoresActivity.class)));

        findViewById(R.id.btnCerrarSesion).setOnClickListener(v -> {
            // Borrar el token y volver al login
            prefs.edit().remove("token").apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}