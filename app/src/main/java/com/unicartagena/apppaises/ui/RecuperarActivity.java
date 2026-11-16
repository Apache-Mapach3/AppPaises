package com.unicartagena.apppaises.ui;

import android.content.Intent;
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

/** Paso 1: el usuario pide un código de recuperación a su correo. */
public class RecuperarActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);

        etEmail = findViewById(R.id.etEmailRecuperar);
        btnEnviar = findViewById(R.id.btnEnviarCodigo);

        btnEnviar.setOnClickListener(v -> enviar());
        findViewById(R.id.btnVolverLoginRec).setOnClickListener(v -> finish());
        findViewById(R.id.btnYaTengoCodigo).setOnClickListener(v -> irAReset());
    }

    private void enviar() {
        String email = etEmail.getText().toString().trim();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email no válido");
            return;
        }

        btnEnviar.setEnabled(false);
        Map<String, String> body = new HashMap<>();
        body.put("email", email);

        RetrofitClient.getApiService().recuperar(body).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                btnEnviar.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(RecuperarActivity.this,
                            "Si el correo está registrado, te enviamos un código.", Toast.LENGTH_LONG).show();
                    irAReset();
                } else {
                    Toast.makeText(RecuperarActivity.this,
                            ApiError.from(response, "No se pudo enviar el código"), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                btnEnviar.setEnabled(true);
                Toast.makeText(RecuperarActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void irAReset() {
        Intent i = new Intent(this, ResetPasswordActivity.class);
        i.putExtra("email", etEmail.getText().toString().trim());
        startActivity(i);
        finish();
    }
}
