package ar.com.develup.tateti.servicios;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import ar.com.develup.tateti.R;
import ar.com.develup.tateti.actividades.ActividadInicial;

/**
 * Created by facundomr on 4/2/17.
 */

public class ServicioNotificaciones extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData() != null) {
            mostrarNotificacion(remoteMessage.getData());
        }

    }

    private void mostrarNotificacion(Map<String, String> notificationData) {


            Intent intent = new Intent(this, ActividadInicial.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            String titulo = notificationData.get("title");

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(titulo)
                    .setContentText(notificationData.get("body"))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationData.get("body")))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
    }

}