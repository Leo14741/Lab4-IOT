package com.example.lab4_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TrabajadorActivity extends AppCompatActivity {

    private Button descargarHorarios;
    private Button buscarFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajador);

        descargarHorarios = findViewById(R.id.descargarHorarios);
        buscarFeedback = findViewById(R.id.buscarFeedback);


        descargarHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrabajadorActivity.this, DescargarHorariosTrabajadorActivity.class);
                startActivity(intent);
            }
        });






    }
}