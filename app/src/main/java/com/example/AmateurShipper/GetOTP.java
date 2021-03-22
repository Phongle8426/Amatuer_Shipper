package com.example.AmateurShipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class GetOTP extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText inputMobile;
    Button buttonGetOTP;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
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
        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMobile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(GetOTP.this, "Enter mobile", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                buttonGetOTP.setVisibility(View.INVISIBLE);

                sharedPreferences = getSharedPreferences(MyPREFERENCES_GETOTP, Context.MODE_PRIVATE);
                saveData(inputMobile.getText().toString());
                sendVerificationCode(inputMobile.getText().toString());

            }
        });
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
                    }
                }
        );

    }

    public void onLoginClick(View View) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }
}