package com.example.lab4_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
                String codigoTutor = editTextCodigoTutor.getText().toString();
                String url = "http://localhost:3000/trabajadores/" + codigoTutor;

                // Realizar la solicitud HTTP en un AsyncTask
                new DescargarListaTask().execute(url);
            }
        });
    }

    private class DescargarListaTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                FileOutputStream outputStream = openFileOutput("lista_trabajadores.txt", MODE_PRIVATE);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();

                urlConnection.disconnect();

                result = "Lista descargada y guardada correctamente";

            } catch (Exception e) {
                e.printStackTrace();
                result = "Error al descargar o guardar la lista";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);

            // Mostrar mensaje al usuario (por ejemplo, mediante Toast)
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            // Puedes agregar código adicional aquí si es necesario
        }
    }
}

