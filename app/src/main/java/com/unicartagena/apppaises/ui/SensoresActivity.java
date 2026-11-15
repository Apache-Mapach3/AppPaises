package com.unicartagena.apppaises.ui;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.unicartagena.apppaises.R;

public class SensoresActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensorLuz, sensorAcelerometro;
    private TextView tvLuz, tvTema, tvAcelerometro;

    private float[] ultimaAceleracion = new float[3];
    private long ultimoTiempoShake = 0;
    private static final float UMBRAL_SHAKE = 15f;
    private boolean primeraLectura = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);

        tvLuz = findViewById(R.id.tvLuz);
        tvTema = findViewById(R.id.tvTema);
        tvAcelerometro = findViewById(R.id.tvAcelerometro);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            sensorAcelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        findViewById(R.id.btnVolverMenu).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorLuz != null) {
            sensorManager.registerListener(this, sensorLuz, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (sensorAcelerometro != null) {
            sensorManager.registerListener(this, sensorAcelerometro, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lux = event.values[0];
            tvLuz.setText(String.format("Nivel de luz: %.0f lux", lux));

            if (lux < 10) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                tvTema.setText("Tema actual: Oscuro 🌙");
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                tvTema.setText("Tema actual: Claro ☀️");
            }
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Ignorar la primera lectura para evitar velocidad absurda
            if (primeraLectura) {
                ultimaAceleracion[0] = x;
                ultimaAceleracion[1] = y;
                ultimaAceleracion[2] = z;
                primeraLectura = false;
                return;
            }

            // Calcular magnitud de la aceleración (no suma simple)
            float deltaX = x - ultimaAceleracion[0];
            float deltaY = y - ultimaAceleracion[1];
            float deltaZ = z - ultimaAceleracion[2];
            float magnitud = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

            long ahora = System.currentTimeMillis();
            if (magnitud > UMBRAL_SHAKE && (ahora - ultimoTiempoShake) > 1000) {
                ultimoTiempoShake = ahora;
                tvAcelerometro.setText("¡Dispositivo agitado! 🔄");
                Toast.makeText(this, "¡Agitaste el celular!", Toast.LENGTH_SHORT).show();
            }

            ultimaAceleracion[0] = x;
            ultimaAceleracion[1] = y;
            ultimaAceleracion[2] = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No necesario
    }
}