package com.example.AmateurShipper.Util;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.AmateurShipper.R;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!availableInternet.isNetworkAvailable(context)){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.disconnect_network,null);
            builder.setView(layout_dialog);

            Button btnTry = layout_dialog.findViewById(R.id.btn_retry);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);

            btnTry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onReceive(context,intent);
                }
            });
        }
    }
}
