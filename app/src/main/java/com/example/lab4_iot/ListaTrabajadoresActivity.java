package com.example.lab4_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ListaTrabajadoresActivity extends AppCompatActivity {

    private EditText editTextCodigoTutor;
    private Button btnDescargarLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_trabajadores);

        editTextCodigoTutor = findViewById(R.id.editTextCodigoTutor);
        btnDescargarLista = findViewById(R.id.btnDescargarLista);

        btnDescargarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes implementar la lógica para descargar la lista
                String codigoTutor = editTextCodigoTutor.getText().toString();
                // Utiliza Firebase Storage u otro método para descargar la lista
            }
        });
    }
}