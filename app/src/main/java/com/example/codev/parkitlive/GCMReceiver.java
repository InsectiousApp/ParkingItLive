package com.example.codev.parkitlive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Set;

/**
 * Created by Codev on 2/3/2016.
 */
public class GCMReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Receivedsmthing", "somethingnew");
       // Toast.makeText(context, "Got a message", Toast.LENGTH_SHORT).show();


//        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
//        NotificationManager nm =
//                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        b.setContentText("Someone parked outside");
//        b.setContentTitle("ParkItLive");
//        b.setSmallIcon(R.drawable.car);
//        Intent i1 = new Intent();
//        i1.setClass(context, MainActivity.class);
//
//
//        PendingIntent i = PendingIntent.getActivity(context, 1, i1, 0);
//        b.setContentIntent(i);
//        b.addAction(R.drawable.car, "text", i);
//        nm.notify(1, b.build());

        Intent i=new Intent();
        i.setClass(context, CarDetectedActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        Bundle extras = intent.getExtras();
        String a = extras.getString("Hello");
        Set<String> keys = extras.keySet();
     //   Toast.makeText(context, extras.getString("Hello"), Toast.LENGTH_LONG).show();


    }
}
