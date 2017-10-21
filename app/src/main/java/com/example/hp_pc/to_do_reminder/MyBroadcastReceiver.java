package com.example.hp_pc.to_do_reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by HP-PC on 20-07-2017.
 */

public class MyBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm Completed", Toast.LENGTH_SHORT).show();

        Intent i=new Intent(context,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,i,0);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder builder=new Notification.Builder(context);
        String x=intent.getStringExtra("title");
        builder.setContentTitle("Here is your reminder...");
        builder.setContentText(x);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        //builder.setOngoing(true);
        builder.setSound(uri);
        builder.setSmallIcon(R.drawable.todoicon);

        Notification notification=builder.build();
        notification.vibrate=new long[]{100,200,300};
        int random= (int) (Math.random()*1000);
        NotificationManager manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(random,notification);
    }
}
