package com.example.AmateurShipper.Callback;
import com.example.AmateurShipper.Model.DataStatisticObject;
import java.util.ArrayList;

public interface DataStatisticCallback {
    void onSuccess(ArrayList<DataStatisticObject> lists);
    void onError(String message);
}
