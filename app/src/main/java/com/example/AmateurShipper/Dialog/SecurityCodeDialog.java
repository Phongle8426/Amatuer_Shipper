package com.example.AmateurShipper.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.AmateurShipper.Model.NotificationWebObject;
import com.example.AmateurShipper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static com.example.AmateurShipper.Adapter.PostAdapter.countPostReceived;
import static com.example.AmateurShipper.Fragment.tab_dang_giao.estimatedTimeToSecurity;
import static com.example.AmateurShipper.Fragment.tab_dang_giao.idpostToSecurity;
import static com.example.AmateurShipper.Fragment.tab_dang_giao.idshopToSecurity;
import static com.example.AmateurShipper.Fragment.tab_dang_giao.positionToSecurity;
import static com.example.AmateurShipper.Fragment.tab_dang_giao.securitycodeToSecurity;

public class SecurityCodeDialog extends DialogFragment {

    public interface OnInputSelected{
        void sendInput(int position);
    }
    SharedPreferences sharedpreferencesCountPost;
    EditText code1,code2,code3,code4;
    Button verify;
    public String iDUser,idpost,idshop,code, estimateTime;
    public int position,countPost;
    DatabaseReference mDatabase;
    public OnInputSelected mOnInputSelected;
    final Handler handler = new Handler(Looper.getMainLooper());
    private static int SPLASH_SCREEN = 500;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_security_code,null);
        code1 = view.findViewById(R.id.inputCode1);
        code2 = view.findViewById(R.id.inputCode2);
        code3 = view.findViewById(R.id.inputCode3);
        code4 = view.findViewById(R.id.inputCode4);
        verify = view.findViewById(R.id.buttonVerify);
        sharedpreferencesCountPost = this.getActivity().getSharedPreferences(countPostReceived, Context.MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        builder.setView(view).setTitle("Nhập Mã Xác Thực");
        Bundle mArgs = getArguments();
         idpost = mArgs.getString(idpostToSecurity);
         idshop = mArgs.getString(idshopToSecurity);
         code = mArgs.getString(securitycodeToSecurity);
         position = mArgs.getInt(positionToSecurity);
         estimateTime = mArgs.getString(estimatedTimeToSecurity);
        Log.i(TAG, "onCreateDialog: "+position);
        getUid();
        loadData();
        setupOTPInputs();
        Toast.makeText(getContext(), code, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onCreateDialog: "+ idpost +"/ "+idshop +"/ "+code);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code1.getText().toString().trim().isEmpty()
                        || code2.getText().toString().trim().isEmpty()
                        || code3.getText().toString().trim().isEmpty()
                        || code4.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), "Please enter valid code", Toast.LENGTH_SHORT).show();
                    return;
                }
                String codeVerify =
                        code1.getText().toString()
                                + code2.getText().toString()
                                + code3.getText().toString()
                                + code4.getText().toString();
                if (codeVerify.equals(code)){
                    mDatabase.child("received_order_status").child(iDUser).child(idpost).child("status").setValue("2");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mDatabase.child("Transaction").child(idpost).child("status").setValue("2");
                            handler.removeCallbacks(this);
                        }
                    }, SPLASH_SCREEN);
                    Long tsLong = System.currentTimeMillis()/1000;
                    String timestamp = tsLong.toString();
                    mDatabase.child("received_order_status").child(iDUser).child(idpost).child("thoi_gian").setValue(timestamp);
                    NotificationWebObject noti = new NotificationWebObject(idpost,iDUser,"2",timestamp);
                    mDatabase.child("Notification").child(idshop).push().setValue(noti);
                    mDatabase.child("OrderStatus").child(idshop).child(idpost).child("status").setValue("2");
                    mDatabase.child("OrderStatus").child(idshop).child(idpost).child("read").setValue(0);
                    mDatabase.child("OrderStatus").child(idshop).child(idpost).child("completed_time").setValue(timestamp);
                    saveData(String.valueOf(countPost-1));
                    mOnInputSelected.sendInput(position);
                    dismiss();
                    cancelNotification(estimateTime);
                }else{
                    Toast.makeText(getContext(), "Mã xác nhận không chính xác", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return builder.create();
    }
    public void cancelNotification(String time){
        //Toast.makeText(getActivity(), "cancelSche" + time, Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        //notificationManager.cancel(cancellSche);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel(time);
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.i(ContentValues.TAG, "onAttach: "+ e.getMessage());
        }
    }
    public void getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        iDUser = user.getUid();
    }

    public void saveData(String count){
        SharedPreferences.Editor editor = sharedpreferencesCountPost.edit();
        editor.putString(countPostReceived, count);
        editor.commit();
    }
    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferencesCountPost.edit();
        editor.clear();
        editor.commit();
    }

    private void loadData() {
        countPost = Integer.parseInt(sharedpreferencesCountPost.getString(countPostReceived, "0"));
    }

    private void setupOTPInputs(){
        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    code2.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    code3.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    code4.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
