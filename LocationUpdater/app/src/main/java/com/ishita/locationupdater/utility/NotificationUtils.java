package com.ishita.locationupdater.utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

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

            Notification.Builder notificationBuilder = new Notification.Builder(context)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.getString(R.string.new_location,info.getLocationName()))
                    .setSmallIcon(R.drawable.logo)
                    .setAutoCancel(true)
                    .setContentIntent(notificationPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        Objects.requireNonNull(notificationManager).notify(1,notificationBuilder.build());
        }

}
