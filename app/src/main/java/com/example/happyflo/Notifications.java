package com.example.happyflo;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import android.content.Intent;
import android.os.Bundle;

public class Notifications extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String replyLabel = "Reply";
        RemoteInput remoteInput = new RemoteInput.Builder("key-text-reply")
                .setLabel(replyLabel)
                .build();

        PendingIntent replyPendingIntent =
                PendingIntent.getBroadcast(context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_launcher_background,
                        "Reply", replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        Bundle results = RemoteInput.getResultsFromIntent(intent);
        if (results != null) {
            CharSequence quickReplyResult = results.getCharSequence("key-text-reply");
            System.out.println(quickReplyResult);
        }



        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Happy Flow")
                .setContentText("Are you feeling okay? Type yes or no!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(action);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200,builder.build());



    }
}