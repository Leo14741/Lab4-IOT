package com.example.lab4_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ColocarIDdelTrabajadorActivity extends AppCompatActivity {

    private EditText editTextCodigoTrabajador;
    private Button btnBuscarTrabajador;
    private String codigoTrabajador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colocar_iddel_trabajador);

        editTextCodigoTrabajador = findViewById(R.id.editTextCodigoTrabajador);
        btnBuscarTrabajador = findViewById(R.id.btnBuscarTrabajador);


        btnBuscarTrabajador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codigoTrabajador = editTextCodigoTrabajador.getText().toString();
                String url = "http://192.168.1.40:3000/empleados/" + codigoTrabajador;

            }
        });







    }
}