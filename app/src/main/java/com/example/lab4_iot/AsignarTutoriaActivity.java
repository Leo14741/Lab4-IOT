package com.example.lab4_iot;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AsignarTutoriaActivity extends AppCompatActivity {

    private EditText editTextCodigoTutor;
    private EditText editTextIdEmpleado;
    private Button btnAsignar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

                // Realizar la consulta a la base de datos para obtener el manager_id del empleado
                // y validar si coincide con el código del tutor
                boolean esManager = validarSiEsManager(codigoTutor, idEmpleado);

                if (!esManager) {
                    // Mostrar notificación de que el tutor no es el manager del empleado
                    Toast.makeText(AsignarTutoriaActivity.this, "El tutor no es el manager del empleado", Toast.LENGTH_SHORT).show();
                    return; // Salir de la función si la validación falla
                }

                // Realizar la consulta a la base de datos para verificar si el empleado ya tiene una cita asignada
                boolean tieneCitaAsignada = validarSiTieneCitaAsignada(idEmpleado);

                if (tieneCitaAsignada) {
                    // Mostrar notificación de que el empleado ya tiene una cita asignada
                    Toast.makeText(AsignarTutoriaActivity.this, "El trabajador ya tiene una cita asignada. Elija otro trabajador", Toast.LENGTH_SHORT).show();
                    return; // Salir de la función si la validación falla
                }

                // Si pasa todas las validaciones, mostrar notificación de éxito
                Toast.makeText(AsignarTutoriaActivity.this, "Asignación del trabajador correcta", Toast.LENGTH_SHORT).show();
            }

            // Función para validar si el tutor es el manager del empleado
            private boolean validarSiEsManager(String codigoTutor, String idEmpleado) {
                // Aquí deberías realizar la consulta a la base de datos y comparar el manager_id del empleado con el código del tutor
                // Si coincide, retornar true, de lo contrario, retornar false
                // Por ejemplo:
                // return managerIdDelEmpleado.equals(codigoTutor);
                // Aquí debes reemplazar managerIdDelEmpleado con tu consulta a la base de datos
                return false;
            }

            // Función para validar si el empleado ya tiene una cita asignada
            private boolean validarSiTieneCitaAsignada(String idEmpleado) {
                // Aquí deberías realizar la consulta a la base de datos para verificar si el empleado ya tiene una cita asignada
                // Si tiene una cita asignada, retornar true, de lo contrario, retornar false
                // Por ejemplo:
                // return citaAsignada;
                // Aquí debes reemplazar citaAsignada con tu consulta a la base de datos
                return false;
            }

        });
    }
}
