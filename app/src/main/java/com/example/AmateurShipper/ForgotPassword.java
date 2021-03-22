package com.example.AmateurShipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ForgotPassword extends AppCompatActivity {
    EditText inputMobile, inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    TextView tv_forgotpassword, tv_mobile_number, tv_otp, tv_otp_send_to, tv_country_code;
    //LottieAnimationView icon_receive_code, icon_fp;
    Button btn_getOTP, btn_verifyOTP;
    View dash;
    String phone, codesent;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

     //   icon_receive_code = findViewById(R.id.animation_view_received);
       // icon_fp = findViewById(R.id.animation_view_fp);

        dash = findViewById(R.id.v_dash);

        tv_country_code = findViewById(R.id.tv_countryCode);
        tv_mobile_number = findViewById(R.id.tv_mobile_number);
        tv_otp = findViewById(R.id.tv_otp);
        tv_otp_send_to = findViewById(R.id.tv_otp_send_to);
        tv_forgotpassword = findViewById(R.id.tv_forgotpassword);

        btn_verifyOTP = findViewById(R.id.buttonVerify);
        btn_getOTP = findViewById(R.id.buttonGetOTP);
        inputMobile = findViewById(R.id.inputMobile);

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);



        btn_getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_forgotpassword.setVisibility(View.GONE);
                tv_mobile_number.setVisibility(View.GONE);
                inputMobile.setVisibility(View.GONE);
                tv_country_code.setVisibility(View.GONE);
                btn_getOTP.setVisibility(View.GONE);
                dash.setVisibility(View.GONE);
               // icon_fp.setVisibility(View.GONE);

                //icon_receive_code.setVisibility(View.INVISIBLE);
                tv_otp.setVisibility(View.VISIBLE);
                tv_otp_send_to.setVisibility(View.VISIBLE);
                inputCode1.setVisibility(View.VISIBLE);
                inputCode2.setVisibility(View.VISIBLE);
                inputCode3.setVisibility(View.VISIBLE);
                inputCode4.setVisibility(View.VISIBLE);
                inputCode5.setVisibility(View.VISIBLE);
                inputCode6.setVisibility(View.VISIBLE);
                btn_verifyOTP.setVisibility(View.VISIBLE);

                sendVerificationCode(inputMobile.getText().toString());

            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //here you can open new activity
                            Intent intent = new Intent(ForgotPassword.this, InputNewPassword.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ForgotPassword.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void sendVerificationCode(String phone) {
        phone = inputMobile.getText().toString();
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
                ForgotPassword.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        String SMScode = phoneAuthCredential.getSmsCode();
                        Toast.makeText(ForgotPassword.this, "SMS code: " + SMScode, Toast.LENGTH_SHORT).show();
                        if (SMScode != null){
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, SMScode);
                            signInWithPhoneAuthCredential(credential);
                        }
                        Toast.makeText(ForgotPassword.this, "Happy new year-COMPLETED", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationID, forceResendingToken);
                        codesent = verificationID;
                       // Toast.makeText(ForgotPassword.this, "Happy new year-CODESENT", Toast.LENGTH_SHORT).show();
                        btn_verifyOTP
                    }
                }
        );

    }


}