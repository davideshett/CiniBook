package com.example.davideshett.david.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.example.davideshett.david.R;
import com.example.davideshett.david.config.Config;

public class NotificationHelper extends ContextWrapper {

    private static final String CINEBOOK_CHANNEL_ID = "com.example.davideshett.CINEBOOK";
    private static final String CINEBOOK_CHANNEL_NAME = "CINEBOOK";

    private NotificationManager manager;


    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(CINEBOOK_CHANNEL_ID,CINEBOOK_CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setName("CINEBOOK");
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {

        if (manager == null){
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        }
        return  manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannel(String title, String body,   Bitmap bitmap){

        Notification.Style style = new Notification.BigPictureStyle().bigPicture(bitmap);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

      return new Notification.Builder(getApplicationContext(),CINEBOOK_CHANNEL_ID)
        .setSmallIcon(R.drawable.cine)
              .setContentTitle(Config.title)
              .setAutoCancel(true)
              .setSound(uri)
              .setStyle(style);
    }
}
