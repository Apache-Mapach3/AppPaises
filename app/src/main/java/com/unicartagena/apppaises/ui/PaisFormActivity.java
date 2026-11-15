package com.unicartagena.apppaises.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
        // Validar campos de texto obligatorios
        if (TextUtils.isEmpty(etNombre.getText().toString().trim())) {
            etNombre.setError("El nombre es obligatorio");
            etNombre.requestFocus();
            return;
        }

        // Validar campos numéricos con try/catch
        long habitantes;
        int departamentos, municipios, universidades;

        try {
            habitantes = Long.parseLong(etHabitantes.getText().toString().trim());
        } catch (NumberFormatException e) {
            etHabitantes.setError("Ingresa un número válido");
            etHabitantes.requestFocus();
            return;
        }

        try {
            departamentos = Integer.parseInt(etDepartamentos.getText().toString().trim());
        } catch (NumberFormatException e) {
            etDepartamentos.setError("Ingresa un número válido");
            etDepartamentos.requestFocus();
            return;
        }

        try {
            municipios = Integer.parseInt(etMunicipios.getText().toString().trim());
        } catch (NumberFormatException e) {
            etMunicipios.setError("Ingresa un número válido");
            etMunicipios.requestFocus();
            return;
        }

        try {
            universidades = Integer.parseInt(etUniversidades.getText().toString().trim());
        } catch (NumberFormatException e) {
            etUniversidades.setError("Ingresa un número válido");
            etUniversidades.requestFocus();
            return;
        }

        String token = "Bearer " + prefs.getString("token", "");

        Pais pais = new Pais();
        pais.setNombre(etNombre.getText().toString().trim());
        pais.setPresidente(etPresidente.getText().toString().trim());
        pais.setContinente(etContinente.getText().toString().trim());
        pais.setNombreHimno(etHimno.getText().toString().trim());
        pais.setNumHabitantes(habitantes);
        pais.setNumDepartamentos(departamentos);
        pais.setNumMunicipios(municipios);
        pais.setIdiomaPpal(etIdioma.getText().toString().trim());
        pais.setNumUniversidades(universidades);
        pais.setTipoDemocracia(etDemocracia.getText().toString().trim());

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
                    Toast.makeText(PaisFormActivity.this, "Error " + response.code() + " al guardar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pais> call, Throwable t) {
                Toast.makeText(PaisFormActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}