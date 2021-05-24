package com.example.AmateurShipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.example.AmateurShipper.Util.NotificationPublisher;

import java.util.UUID;

public class TestNotification extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    public static String NOTIFICATION_ID = "1";
    public static String NOTIFICATION ="1";
    public static String CHANNEL_ID = "123";
    private NotificationManagerCompat notificationManager;
    Button notifi,noti2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_notification);
        notifi = findViewById(R.id.notification);
        noti2= findViewById(R.id.notification2);
        notificationManager = NotificationManagerCompat.from(this);
        notifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSetting(view);
            }
        });
        noti2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleNotification(notification("8 second delay","8"), 8000,"8",8,8);
            }
        });
    }

    public void openSetting(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.noti);
        popupMenu.show();
    }


        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_5:
                    scheduleNotification(notification("5 second delay","1"), 5000,"1",1,1);
                    return true;
                case R.id.action_10:
                    scheduleNotification(notification("10 second delay","2"), 10000,"2",2,2);
                    return true;
                case R.id.action_30:
                    scheduleNotification(notification("30 second delay","3"), 30000,"3",3,3);
                    return true;
                default:
                    return super.onOptionsItemSelected(menuItem);
            }
    }

    private void scheduleNotification(Notification notification, int delay,String id_channel,int request_code,int noti_id) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NOTIFICATION_ID, noti_id);
        notificationIntent.putExtra(NOTIFICATION, notification);
        notificationIntent.putExtra(CHANNEL_ID, id_channel);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, request_code, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public Notification notification(String content,String id_channel){
        String title = "Tesst";
        String message ="CHay dc r ne";
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(id_channel,"n",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id_channel)
                .setSmallIcon(R.drawable.ic_arrow_back)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
////        managerCompat.notify(1, builder.build());
        return builder.build();
    }
}