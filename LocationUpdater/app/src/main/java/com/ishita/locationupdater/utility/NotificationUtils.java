package com.ishita.locationupdater.utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ishita.locationupdater.R;
import com.ishita.locationupdater.model.LocationInformation;
import com.ishita.locationupdater.ui.LocationActivity;

import java.util.Objects;

public class NotificationUtils {
    public static void showNotification(Context context,LocationInformation info){
            Intent notificationIntent = new Intent(context, LocationActivity.class);

            // Construct a task stack.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

            // Add the location Activity to the task stack as the parent.
            stackBuilder.addParentStack(LocationActivity.class);

            // Push the content Intent onto the stack.
            stackBuilder.addNextIntent(notificationIntent);

            // Get a PendingIntent containing the entire back stack.
            PendingIntent notificationPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = null;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            NotificationChannel channel = new NotificationChannel(context.getString(R.string.channel_name),context.getString(R.string.channel_name),NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context,channel.getId())
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.getString(R.string.new_location,info.getLocationName()))
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.logo)).getBitmap())
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(notificationPendingIntent)
                    .setChannelId(channel.getId());
        } else {

            builder =  new NotificationCompat.Builder(context)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.getString(R.string.new_location,info.getLocationName()))
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.logo)).getBitmap())
                    .setContentIntent(notificationPendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            Log.d("FragmentEventDetails", "scheduleNotification notification builder built");
        }
        Notification notification = builder.build();
        Objects.requireNonNull(notificationManager).notify(1,notification);
        }

}
