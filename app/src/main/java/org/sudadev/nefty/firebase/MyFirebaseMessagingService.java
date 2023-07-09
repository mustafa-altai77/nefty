package org.sudadev.nefty.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.sudadev.nefty.R;
import org.sudadev.nefty.common.Constants;
import org.sudadev.nefty.ui.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final String TAG = "MyFirebaseMessagingSer";

    @Override
    public void onNewToken(String firebaseToken) {
        new SendingFirebaseToken().send(this, firebaseToken);
    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String data_type = remoteMessage.getData().get("data_type");
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        String id = remoteMessage.getData().get("id");

        if (remoteMessage.getData().get("data_type").equals("cast")){
            data_type = "cast";
            showNotification(title, message, "0", data_type);
        }
        else {
            showNotification(message, title, id, data_type);
        }
    }

    private void showNotification(String message, String title, String id, String data_type) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("id", id);
        intent.putExtra("data_type", data_type);
        intent.putExtra("message", message);

        int fileId = R.raw.consequence;

        Uri customSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + fileId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent).setSound(customSoundUri)
                .setAutoCancel(true);

        int notificationId = (int) System.currentTimeMillis();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel =
                    new NotificationChannel(Constants.CHANNEL_ID,
                            Constants.CHANNEL_Name, NotificationManager.IMPORTANCE_HIGH);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            notificationChannel.setSound(customSoundUri, attributes);

            mNotificationManager.createNotificationChannel(notificationChannel);
            mBuilder.setChannelId(Constants.CHANNEL_ID);
            int notificationId2 = (int) System.currentTimeMillis();
            mNotificationManager.notify(notificationId2, mBuilder.build());
            return;
        }

        notificationManager.notify(notificationId, mBuilder.build());
    }
}
