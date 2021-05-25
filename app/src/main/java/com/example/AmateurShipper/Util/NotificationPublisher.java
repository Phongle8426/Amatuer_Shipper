package com.example.AmateurShipper.Util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;



import static com.mapbox.services.android.navigation.ui.v5.feedback.FeedbackBottomSheet.TAG;

public class NotificationPublisher extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
//        String id_channel = intent.getStringExtra(CHANNEL_ID);
//        Log.i(TAG, "onReceive: "+id_channel);
//        if (id_channel=="1"){
//            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//                NotificationChannel notificationChannel = new NotificationChannel(id_channel,"n",NotificationManager.IMPORTANCE_HIGH);
//                NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//                manager.createNotificationChannel(notificationChannel);
//            }
//        }else{
//            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//                NotificationChannel notificationChannel2 = new NotificationChannel(id_channel,"n",NotificationManager.IMPORTANCE_LOW);
//                NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//                manager.createNotificationChannel(notificationChannel2);
//            }
//        }

       // NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra("NOTIFICATION");
        int id = intent.getIntExtra("NOTIFICATION_ID",1);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id, notification);


    }
}
