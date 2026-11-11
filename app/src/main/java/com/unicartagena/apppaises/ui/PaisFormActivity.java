package com.unicartagena.apppaises.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unicartagena.apppaises.R;
import com.unicartagena.apppaises.api.RetrofitClient;
import com.unicartagena.apppaises.model.Pais;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaisFormActivity extends AppCompatActivity {

    private EditText etNombre, etPresidente, etContinente, etHimno, etHabitantes,
            etDepartamentos, etMunicipios, etIdioma, etUniversidades, etDemocracia;
    private SharedPreferences prefs;
    private Long paisId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pais_form);

        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        etNombre = findViewById(R.id.etNombre);
        etPresidente = findViewById(R.id.etPresidente);
        etContinente = findViewById(R.id.etContinente);
        etHimno = findViewById(R.id.etHimno);
        etHabitantes = findViewById(R.id.etHabitantes);
        etDepartamentos = findViewById(R.id.etDepartamentos);
        etMunicipios = findViewById(R.id.etMunicipios);
        etIdioma = findViewById(R.id.etIdioma);
        etUniversidades = findViewById(R.id.etUniversidades);
        etDemocracia = findViewById(R.id.etDemocracia);

        // Si viene un país para editar
        if (getIntent().hasExtra("pais")) {
            Pais pais = (Pais) getIntent().getSerializableExtra("pais");
            if (pais != null) {
                paisId = pais.getId();
                etNombre.setText(pais.getNombre());
                etPresidente.setText(pais.getPresidente());
                etContinente.setText(pais.getContinente());
                etHimno.setText(pais.getNombreHimno());
                etHabitantes.setText(String.valueOf(pais.getNumHabitantes()));
                etDepartamentos.setText(String.valueOf(pais.getNumDepartamentos()));
                etMunicipios.setText(String.valueOf(pais.getNumMunicipios()));
                etIdioma.setText(pais.getIdiomaPpal());
                etUniversidades.setText(String.valueOf(pais.getNumUniversidades()));
                etDemocracia.setText(pais.getTipoDemocracia());
            }
        }

        findViewById(R.id.btnGuardar).setOnClickListener(v -> guardarPais());
    }

    private void guardarPais() {
        String token = "Bearer " + prefs.getString("token", "");

        Pais pais = new Pais();
        pais.setNombre(etNombre.getText().toString());
        pais.setPresidente(etPresidente.getText().toString());
        pais.setContinente(etContinente.getText().toString());
        pais.setNombreHimno(etHimno.getText().toString());
        pais.setNumHabitantes(Integer.parseInt(etHabitantes.getText().toString()));
        pais.setNumDepartamentos(Integer.parseInt(etDepartamentos.getText().toString()));
        pais.setNumMunicipios(Integer.parseInt(etMunicipios.getText().toString()));
        pais.setIdiomaPpal(etIdioma.getText().toString());
        pais.setNumUniversidades(Integer.parseInt(etUniversidades.getText().toString()));
        pais.setTipoDemocracia(etDemocracia.getText().toString());

        Call<Pais> call;
        if (paisId == null) {
            call = RetrofitClient.getApiService().crearPais(token, pais);
        } else {
            call = RetrofitClient.getApiService().actualizarPais(token, paisId, pais);
        }

        call.enqueue(new Callback<Pais>() {
            @Override
            public void onResponse(Call<Pais> call, Response<Pais> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PaisFormActivity.this, "Guardado exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PaisFormActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pais> call, Throwable t) {
                Toast.makeText(PaisFormActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}