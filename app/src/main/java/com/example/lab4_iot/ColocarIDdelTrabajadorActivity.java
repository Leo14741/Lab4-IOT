package com.example.lab4_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
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
                String url = "http://192.168.1.40:3000/empleados/" + codigoTrabajador;

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

            // Utiliza la variable meetingDate como desees, ahora contiene el meeting_date obtenido
        }
    }
}