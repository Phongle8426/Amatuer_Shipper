package com.example.AmateurShipper.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class availableInternet {
    public availableInternet() {
    }

    public static boolean isNetworkAvailable(Context con) {
        try {
            ConnectivityManager cm = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (cm != null) {
                NetworkInfo info = cm.getActiveNetworkInfo();
                if (info!=null){
                        if (info.getState() == NetworkInfo.State.CONNECTED)
                        return true;
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
