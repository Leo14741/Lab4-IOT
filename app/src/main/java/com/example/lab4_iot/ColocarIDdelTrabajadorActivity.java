package com.example.lab4_iot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ColocarIDdelTrabajadorActivity extends AppCompatActivity {

    private EditText editTextCodigoTrabajador;
    private Button btnBuscarTrabajador;
    private String codigoTrabajador;
    private String meetingDate;

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
                String url = "http://192.168.1.40:3000/meeting_date/" + codigoTrabajador;

                // Realizar la solicitud HTTP en un AsyncTask
                new ObtenerMeetingDateTask().execute(url);
            }
        });
    }

    private class ObtenerMeetingDateTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                reader.close();
                inputStream.close();
                urlConnection.disconnect();

                // Parsea el JSON para obtener el campo meeting_date
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                meetingDate = jsonObject.getString("meeting_date");

                result = "Meeting Date obtenida correctamente";
            } catch (Exception e) {
                e.printStackTrace();
                result = "Error al obtener Meeting Date";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);

            // Mostrar mensaje al usuario (por ejemplo, mediante Toast)
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            // Verifica si meetingDate no está vacío o nulo
            if (meetingDate != null && !meetingDate.isEmpty()) {
                // El trabajador tuvo una tutoría, inicia TrabajadorActivity con los botones visibles
                Intent intent = new Intent(ColocarIDdelTrabajadorActivity.this, TrabajadorActivity.class);
                intent.putExtra("meetingDate", meetingDate);

                // Crear y mostrar la notificación
                createNotification();

                startActivity(intent);
            } else {
                // El trabajador no tuvo una tutoría, inicia TrabajadorActivity con el botón de feedback oculto
                Intent intent = new Intent(ColocarIDdelTrabajadorActivity.this, TrabajadorActivity.class);
                intent.putExtra("meetingDate", ""); // Puedes pasar meetingDate como cadena vacía
                intent.putExtra("ocultarFeedback", true); // Indica que el botón de feedback debe ocultarse
                startActivity(intent);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "my_channel_id";
        String channelName = "My Channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Construir la notificación
        Notification notification = new Notification.Builder(this, channelId)
                .setSmallIcon(R.drawable.baseline_radio_24)
                .setContentTitle("Cita programada")
                .setContentText("La fecha de su cita es " + meetingDate)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .build();

        // Mostrar la notificación
        notificationManager.notify(1, notification);
    }
}