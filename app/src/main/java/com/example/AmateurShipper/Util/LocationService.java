package com.example.AmateurShipper.Util;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.auth.User;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class LocationService extends Service {


    private static final String TAG = "LocationService";

    private final static long UPDATE_INTERVAL = 4 * 1000;  /* 4 secs */
    private final static long FASTEST_INTERVAL = 2000; /* 2 sec */

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    private DatabaseReference mDatabase;
    Handler mHandler = new Handler();
    boolean check = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals("Start")) {
            mHandler.postDelayed(new Runnable() {
                int i = 0;
                @Override
                public void run() {
                    if (!check){
                        updateLocation();
                        // Log.d("receiver","run: "+ i++);
                        //Toast.makeText(ExampleJobService.this, ""+i, Toast.LENGTH_SHORT).show();
                        mHandler.postDelayed(this, 3000);
                    }
                }
            }, 3000);
        }
        if (intent.getAction().equals("Stop")){
            check = true;
            stopSelf();
        }
        return START_STICKY;
    }

    private void updateLocation() {
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, myLocationBroadcast.class);
        intent.setAction(myLocationBroadcast.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setSmallestDisplacement(30f);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //mHandler.removeCallbacks(null);
        Log.d("LogService","SERVICE HAS BEEN DESTROYED!!!");
    }

}

