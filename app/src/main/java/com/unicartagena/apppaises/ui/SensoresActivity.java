package com.unicartagena.apppaises.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.unicartagena.apppaises.R;

public class SensoresActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensorLuz, sensorAcelerometro;
    private TextView tvLuz, tvTema, tvAcelerometro;

    private float ultimoX = 0, ultimoY = 0, ultimoZ = 0;
    private long ultimoTiempoMovimiento = 0;
    private static final int UMBRAL_AGITACION = 15; // Sensibilidad del agitado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);

        tvLuz = findViewById(R.id.tvLuz);
        tvTema = findViewById(R.id.tvTema);
        tvAcelerometro = findViewById(R.id.tvAcelerometro);

        // Configurar sensores
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
        // Registrar los sensores cuando la pantalla está activa
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
        // Desregistrar para ahorrar batería
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            // 🌞 SENSOR DE LUZ: Cambiar tema automáticamente
            float lux = event.values[0];
            tvLuz.setText(String.format("Nivel de luz: %.0f lux", lux));

            if (lux < 10) {
                // Poca luz -> Modo Oscuro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                tvTema.setText("Tema actual: Oscuro 🌙");
            } else {
                // Mucha luz -> Modo Claro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                tvTema.setText("Tema actual: Claro ☀️");
            }
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // 🏃 ACELERÓMETRO: Detectar agitación (Shake)
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long tiempoActual = System.currentTimeMillis();
            if ((tiempoActual - ultimoTiempoMovimiento) > 1000) { // Evitar múltiples detecciones seguidas
                long diff = tiempoActual - ultimoTiempoMovimiento;
                float speed = Math.abs(x + y + z - ultimoX - ultimoY - ultimoZ) / diff * 10000;

                if (speed > UMBRAL_AGITACION) {
                    tvAcelerometro.setText("¡Dispositivo agitado! 🔄");
                    Toast.makeText(this, "¡Agitaste el celular!", Toast.LENGTH_SHORT).show();
                    // Aquí podrías llamar a un método para refrescar datos
                }
                ultimoTiempoMovimiento = tiempoActual;
            }
            ultimoX = x;
            ultimoY = y;
            ultimoZ = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No necesitamos implementar nada aquí
    }
}