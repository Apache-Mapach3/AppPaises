package com.unicartagena.apppaises.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unicartagena.apppaises.R;
import com.unicartagena.apppaises.api.ApiError;
import com.unicartagena.apppaises.api.RetrofitClient;
import com.unicartagena.apppaises.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre, etEmail, etPassword;
    private Button btnRegistrar, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolver = findViewById(R.id.btnVolverLogin);

        btnRegistrar.setOnClickListener(v -> registrar());
        btnVolver.setOnClickListener(v -> finish());
    }

    private void registrar() {
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email no válido");
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Mínimo 6 caracteres");
            return;
        }

        btnRegistrar.setEnabled(false);
        Usuario usuario = new Usuario(nombre, email, password);

        RetrofitClient.getApiService().registrar(usuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                btnRegistrar.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(RegistroActivity.this, "Registro exitoso. Ahora inicia sesión.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(RegistroActivity.this, ApiError.from(response, "Error en el registro"), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                btnRegistrar.setEnabled(true);
                Toast.makeText(RegistroActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}