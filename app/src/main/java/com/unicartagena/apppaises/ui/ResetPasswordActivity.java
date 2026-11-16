package com.unicartagena.apppaises.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unicartagena.apppaises.R;
import com.unicartagena.apppaises.api.ApiError;
import com.unicartagena.apppaises.api.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Paso 2: el usuario escribe el código recibido y la nueva contraseña. */
public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etEmail, etCodigo, etNueva;
    private Button btnRestablecer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etEmail = findViewById(R.id.etEmailReset);
        etCodigo = findViewById(R.id.etCodigoReset);
        etNueva = findViewById(R.id.etNuevaPassword);
        btnRestablecer = findViewById(R.id.btnRestablecer);

        String email = getIntent().getStringExtra("email");
        if (email != null) etEmail.setText(email);

        btnRestablecer.setOnClickListener(v -> restablecer());
        findViewById(R.id.btnVolverLoginReset).setOnClickListener(v -> finish());
    }

    private void restablecer() {
        String email = etEmail.getText().toString().trim();
        String codigo = etCodigo.getText().toString().trim();
        String nueva = etNueva.getText().toString().trim();

        if (email.isEmpty() || codigo.isEmpty() || nueva.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nueva.length() < 6) {
            etNueva.setError("Mínimo 6 caracteres");
            return;
        }

        btnRestablecer.setEnabled(false);
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("codigo", codigo);
        body.put("nuevoPassword", nueva);

        RetrofitClient.getApiService().resetPassword(body).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                btnRestablecer.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this,
                            "Contraseña actualizada. Ya puedes iniciar sesión.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(ResetPasswordActivity.this,
                            ApiError.from(response, "No se pudo restablecer"), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                btnRestablecer.setEnabled(true);
                Toast.makeText(ResetPasswordActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
