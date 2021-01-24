package com.example.happyflo;
import android.app.Notification;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

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

        Bundle results = RemoteInput.getResultsFromIntent(intent);
        if (results != null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Happy Flow")
                    .setContentText("response recorded!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(200, builder.build());
            CharSequence quickReplyResult = results.getCharSequence("key-text-reply");
            System.out.println(quickReplyResult);

        }




        String contentText;

        contentText = "How are you feeling? Great/Good/Okay/Bad?";


        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_launcher_background,
                        "Reply", replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Happy Flow")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(action)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());



        Bundle results2 = RemoteInput.getResultsFromIntent(intent);
        if (results2 != null) {
            NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context, "notify")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Happy Flow")
                    .setContentText("response recorded!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);


            NotificationManagerCompat notificationManager2 = NotificationManagerCompat.from(context);
            notificationManager.notify(200, builder2.build());
            CharSequence quickReplyResult = results2.getCharSequence("key-text-reply");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            System.out.println(quickReplyResult);

        }







    }
}
