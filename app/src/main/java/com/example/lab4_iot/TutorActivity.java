package com.example.lab4_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TutorActivity extends AppCompatActivity {

    private Button listaTrabajadoresButton;
    private Button buscarTrabajadorButton;
    private Button asignarTutoriaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);

        listaTrabajadoresButton = findViewById(R.id.listaTrabajadores);
        buscarTrabajadorButton = findViewById(R.id.buscarTrabajador);
        asignarTutoriaButton = findViewById(R.id.asignarTutoria);

        listaTrabajadoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorActivity.this, ListaTrabajadoresActivity.class);
                startActivity(intent);
            }
        });

        buscarTrabajadorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorActivity.this, BuscarTrabajadorActivity.class);
                startActivity(intent);
            }
        });

        asignarTutoriaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorActivity.this, ListaTrabajadoresActivity.class);
                startActivity(intent);
            }
        });
    }
}