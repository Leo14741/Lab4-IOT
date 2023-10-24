package com.example.lab4_iot;

import androidx.annotation.RequiresApi;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class TrabajadorActivity extends AppCompatActivity {
    private Button descargarHorarios;
    private Button buscarFeedback;
    private boolean ocultarFeedback;
    private String meetingDate;

    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajador);

        descargarHorarios = findViewById(R.id.descargarHorarios);
        buscarFeedback = findViewById(R.id.buscarFeedback);

        ocultarFeedback = getIntent().getBooleanExtra("ocultarFeedback", false);
        meetingDate = getIntent().getStringExtra("meetingDate");

        if (ocultarFeedback) {
            buscarFeedback.setVisibility(View.GONE);
        }

        descargarHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meetingDate != null && !meetingDate.isEmpty()) {
                    // El trabajador tiene una cita, descarga la imagen
                    descargarImagenHorarios();
                } else {
                    // El trabajador no tiene una cita, muestra una notificación
                    mostrarNotificacionSinCita();
                }
            }
        });
    }

    private void descargarImagenHorarios() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permiso de escritura en almacenamiento externo concedido
            String imageUrl = "https://i.pinimg.com/564x/4e/8e/a5/4e8ea537c896aa277e6449bdca6c45da.jpg";

            // URL URI
            Uri downloadUri = Uri.parse(imageUrl);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setTitle("horario.jpg");
            request.setMimeType("image/jpeg");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "horario.jpg");

            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

            try {
                dm.enqueue(request);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else {
            // Permiso de escritura en almacenamiento externo denegado, solicita permiso
            ActivityCompat.requestPermissions(TrabajadorActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private void mostrarNotificacionSinCita() {
        // Muestra una notificación si el usuario no tiene una cita
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "my_channel_id";
        String channelName = "My Channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(this, TrabajadorActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("No cuenta con tutorías pendientes")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(2, builder.build());
    }

}