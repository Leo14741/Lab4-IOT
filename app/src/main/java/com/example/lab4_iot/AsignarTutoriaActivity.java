package com.example.lab4_iot;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.lab4_iot.entity.Employee;
import com.example.lab4_iot.repo.ApiService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AsignarTutoriaActivity extends AppCompatActivity {

    private EditText editTextCodigoTutor;
    private EditText editTextIdEmpleado;
    private Button btnAsignar;
    private ApiService apiService;

    private static final String CHANNEL_ID = "channel_id";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_tutoria);

        editTextCodigoTutor = findViewById(R.id.editTextCodigoTutor);
        editTextIdEmpleado = findViewById(R.id.editTextIdEmpleado);
        btnAsignar = findViewById(R.id.btnAsignar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.40:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        btnAsignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String employee_id = editTextIdEmpleado.getText().toString();
                String codigoTutor = editTextCodigoTutor.getText().toString();

                new ManagerIdTask().execute(employee_id, codigoTutor);
            }
        });
    }

    private class ManagerIdTask extends AsyncTask<String, Void, Employee> {
        private String employee_id;
        private String codigoTutor;

        @Override
        protected Employee doInBackground(String... params) {
            employee_id = params[0];
            codigoTutor = params[1];

            Call<Employee> call = apiService.obtenerDatosEmpleado(employee_id, codigoTutor);

            try {
                Response<Employee> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    return response.body();
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Employee employee) {
            if (employee != null) {
                String manager_id = String.valueOf(employee.getManager_id());
                int meeting = employee.getMeeting();
                System.out.println(meeting+""+manager_id+""+codigoTutor);
                if (manager_id.equals(codigoTutor)) {
                    if (meeting==1) {
                        Toast.makeText(AsignarTutoriaActivity.this, "El trabajador ya tiene una cita asignada. Elija otro trabajador", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AsignarTutoriaActivity.this, "Asignación del trabajador correcta", Toast.LENGTH_SHORT).show();

                        new ActualizarCitaTask().execute(employee_id);
                    }
                } else {
                    Toast.makeText(AsignarTutoriaActivity.this, "El tutor no es el manager del empleado", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AsignarTutoriaActivity.this, "Error al obtener la información del empleado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ActualizarCitaTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String employee_id = params[0];

            Call<Void> call = apiService.actualizarCita(employee_id);

            try {
                Response<Void> response = call.execute();
                return response.isSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(AsignarTutoriaActivity.this, "Cita actualizada correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AsignarTutoriaActivity.this, "Error al actualizar la cita", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void mostrarNotificacion(String mensaje) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Canal de Notificación";
            String description = "Descripción del canal";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Notificación de Asignación")
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
