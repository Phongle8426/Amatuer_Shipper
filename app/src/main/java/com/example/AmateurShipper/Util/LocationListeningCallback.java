//package com.example.AmateurShipper.Util;
//
//import android.annotation.SuppressLint;
//import android.location.Location;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//
//import com.example.AmateurShipper.MainActivity;
//import com.example.AmateurShipper.MapFragment;
//import com.example.AmateurShipper.R;
//import com.mapbox.android.core.location.LocationEngineCallback;
//import com.mapbox.android.core.location.LocationEngineResult;
//
//import java.lang.ref.WeakReference;
//
//public class LocationListeningCallback
//        implements LocationEngineCallback<LocationEngineResult> {
//
//    private final WeakReference<MapFragment> activityWeakReference;
//
//    public LocationListeningCallback(MapFragment activity) {
//        this.activityWeakReference = new WeakReference<>(activity);
//    }
//
//    /**
//     * The LocationEngineCallback interface's method which fires when the device's location has changed.
//     *
//     * @param result the LocationEngineResult object which has the last known location within it.
//     */
//    @SuppressLint("StringFormatInvalid")
//    @Override
//    public void onSuccess(LocationEngineResult result) {
//        MapFragment activity = activityWeakReference.get();
//
//        if (activity != null) {
//            Location location = result.getLastLocation();
//
//            if (location == null) {
//                return;
//            }
//
//
//// Pass the new location to the Maps SDK's LocationComponent
//            if (activity.mapboxMap != null && result.getLastLocation() != null) {
//                activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
//            }
//        }
//    }
//
//    /**
//     * The LocationEngineCallback interface's method which fires when the device's location can not be captured
//     *
//     * @param exception the exception message
//     */
//    @Override
//    public void onFailure(@NonNull Exception exception) {
//        Log.d("LocationChangeActivity", exception.getLocalizedMessage());
//        MapFragment activity = activityWeakReference.get();
//        if (activity != null) {
//
//        }
//    }
//}
