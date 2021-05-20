package com.example.AmateurShipper.Util;

import android.content.Context;

import com.example.AmateurShipper.Callback.DataStatisticCallback;
import com.example.AmateurShipper.Model.DataStatisticObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class fecthDataStatistic {
     DatabaseReference mDatabase;
     Context mContext;

    public fecthDataStatistic(Context mContext) {
        this.mContext = mContext;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void fecthData(final DataStatisticCallback dataStatisticCallback, String uId){
        ArrayList<DataStatisticObject> lDataStatistic = new ArrayList<>();

        mDatabase.child("received_order_status").child(uId).orderByChild("status").equalTo("2").addValueEventListener(new ValueEventListener() {
            String day,amount;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snap : snapshot.getChildren()){
                        day = snap.child("thoi_gian").getValue(String.class);
                        amount = snap.child("phi_giao").getValue(String.class);
                        lDataStatistic.add(new DataStatisticObject(day,amount));
                    }
                    dataStatisticCallback.onSuccess(lDataStatistic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    dataStatisticCallback.onError(error.toString());
            }
        });
    }
}
