package com.example.AmateurShipper.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.AmateurShipper.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class GetOTP extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText inputMobile;
    Button buttonGetOTP;
    ProgressBar progressBar;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    String get_phone_number;
    public static final String MyPREFERENCES_GETOTP = "MYPREF";
    public static final String PHONENUMBER_GETOTP = "phone number";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_o_t_p);
        inputMobile = findViewById(R.id.inputMobile);
        buttonGetOTP = findViewById(R.id.buttonGetOTP);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference();

        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMobile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(GetOTP.this, "Enter mobile", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    databaseReference.child("Account_Shipper").orderByKey().equalTo(inputMobile.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                inputMobile.setError("This number is already exists");
                            } else {
                                progressBar.setVisibility(View.VISIBLE);
                                buttonGetOTP.setVisibility(View.INVISIBLE);
                                sharedPreferences = getSharedPreferences(MyPREFERENCES_GETOTP, Context.MODE_PRIVATE);
                                saveData(inputMobile.getText().toString());
                                sendVerificationCode(inputMobile.getText().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//                                progressBar.setVisibility(View.VISIBLE);
//
//                                buttonGetOTP.setVisibility(View.INVISIBLE);
//                                sharedPreferences = getSharedPreferences(MyPREFERENCES_GETOTP, Context.MODE_PRIVATE);
//                                saveData(inputMobile.getText().toString());
//                                sendVerificationCode(inputMobile.getText().toString());

//                    databaseReference.orderByKey().equalTo(inputMobile.getText().toString()).addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                            if (snapshot.exists()) {
//                                inputMobile.setError("This number is already exists");
//                            } else {
//                                progressBar.setVisibility(View.VISIBLE);
//                                buttonGetOTP.setVisibility(View.INVISIBLE);
//                                sharedPreferences = getSharedPreferences(MyPREFERENCES_GETOTP, Context.MODE_PRIVATE);
//                                saveData(inputMobile.getText().toString());
//                                sendVerificationCode(inputMobile.getText().toString());
//                            }
//                        }
//
//                        @Override
//                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                        });
                    }

    }
    public void saveData(String phone) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GetOTP.PHONENUMBER_GETOTP, phone);
        editor.apply();
    }

    public void sendVerificationCode(String phone) {

        if (phone.isEmpty()) {
            inputMobile.setError("Phone number is required");
            inputMobile.requestFocus();
        }
        if (phone.length() > 10 || phone.length() < 9) {
            inputMobile.setError("please enter a valid phone");
            inputMobile.requestFocus();
            return;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + phone,
                60L,
                TimeUnit.SECONDS,
                GetOTP.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.GONE);
                        buttonGetOTP.setVisibility(View.VISIBLE);
                        Toast.makeText(GetOTP.this, "Happy new year-COMPLETED", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        buttonGetOTP.setVisibility(View.VISIBLE);
                        Toast.makeText(GetOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationID, forceResendingToken);
                        progressBar.setVisibility(View.GONE);
                        buttonGetOTP.setVisibility(View.VISIBLE);
                        Toast.makeText(GetOTP.this, "Happy new year-CODESENT", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                        intent.putExtra("mobile", inputMobile.getText().toString());
                        intent.putExtra("verificationID", verificationID);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_mid_left);
                    }
                });
    }
    private boolean validatePhone(){
        if(inputMobile.getText().toString().isEmpty()){
            inputMobile.setError("Field cannot be empty");
            return false;
        }
        else{
            inputMobile.setError(null);
            return true;
        }
    }
    public void onLoginClick(View View) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }
});}}