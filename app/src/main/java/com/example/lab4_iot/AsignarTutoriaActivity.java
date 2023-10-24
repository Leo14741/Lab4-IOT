package com.example.lab4_iot;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AsignarTutoriaActivity extends AppCompatActivity {

    private EditText editTextCodigoTutor;
    private EditText editTextIdEmpleado;
    private Button btnAsignar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_tutoria);

        editTextCodigoTutor = findViewById(R.id.editTextCodigoTutor);
        editTextIdEmpleado = findViewById(R.id.editTextIdEmpleado);
        btnAsignar = findViewById(R.id.btnAsignar);

        btnAsignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigoTutor = editTextCodigoTutor.getText().toString();
                String idEmpleado = editTextIdEmpleado.getText().toString();
                new ObtenerManagerIdTask().execute(idEmpleado, codigoTutor);
            }
        });
    }

    private class ObtenerManagerIdTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String idEmpleado = params[0];
            String codigoTutor = params[1];
            // Aquí realiza la solicitud HTTP al servidor Node.js para obtener el manager_id del empleado
            // y luego maneja la respuesta en onPostExecute
            return null; // Debes reemplazar esto con tu lógica de obtención del manager_id
        }

        @Override
        protected void onPostExecute(String managerId) {
            if (managerId != null && managerId.equals(editTextCodigoTutor.getText().toString())) {
                new ValidarCitaTask().execute(editTextIdEmpleado.getText().toString());
            } else {
                Toast.makeText(AsignarTutoriaActivity.this, "El tutor no es el manager del empleado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ValidarCitaTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String idEmpleado = params[0];
            // Aquí realiza la solicitud HTTP al servidor Node.js para verificar si el empleado tiene una cita asignada
            // y luego maneja la respuesta en onPostExecute
            return null; // Debes reemplazar esto con tu lógica de validación de cita
        }

        @Override
        protected void onPostExecute(Boolean tieneCitaAsignada) {
            if (tieneCitaAsignada) {
                Toast.makeText(AsignarTutoriaActivity.this, "El trabajador ya tiene una cita asignada. Elija otro trabajador", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AsignarTutoriaActivity.this, "Asignación del trabajador correcta", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

