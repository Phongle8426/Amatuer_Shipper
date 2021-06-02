package com.example.AmateurShipper.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class myLocationBroadcast  extends BroadcastReceiver {

    private DatabaseReference mDatabase;
    String uid;
    public static final String ACTION_PROCESS_UPDATE ="com.example.AmateurShipper.Util.UPDATE_LOCATION";
    public myLocationBroadcast() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent !=null){
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATE.equals(action)){
                LocationResult result = LocationResult.extractResult(intent);
                if (result!=null){
                    Location location = result.getLastLocation();
                    String location_string = new StringBuilder(""+location.getLatitude())
                            .append("/").append(location.getLongitude()).toString();
                    mDatabase.child("Location_Shipper").child(uid)
                            .child("lat").setValue(location.getLatitude());
                    mDatabase.child("Location_Shipper").child(uid)
                            .child("lng").setValue(location.getLongitude());
                    try {
                        //Toast.makeText(context, location_string, Toast.LENGTH_SHORT).show();
                    }catch (Exception ex){

                    }
                }
            }
        }else Toast.makeText(context, "nothing", Toast.LENGTH_SHORT).show();
    }
}
