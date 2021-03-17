package com.example.AmateurShipper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.IllegalCharsetNameException;
import java.util.concurrent.TimeUnit;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class RegisterActivity extends AppCompatActivity{
    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    private CircularProgressButton loginButton;
    private EditText name, email, password, repassword;
    SharedPreferences sharedPreferences;
    String save_phonenumber;
    private String iName, iEmail, iRePassword, iPassword, codeSent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();

        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        repassword = findViewById(R.id.editTextRePassword);
        password = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.cirRegisterButton);


        iEmail = email.getText().toString();
//        iPhonen = phonenumber.getText().toString();
        iPassword = password.getText().toString();

        sharedPreferences = getSharedPreferences(GetOTP.MyPREFERENCES_GETOTP, Context.MODE_PRIVATE);
        loadData();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateName() | !validateEmail() | !validatePassword() | !validateRePassword()){
                    return;
                }
                FirebaseApp.initializeApp(getApplicationContext());
                rootNode= FirebaseDatabase.getInstance();
                databaseReference = rootNode.getReference("users");
                user_register ur = new user_register(name.getText().toString(), email.getText().toString(), password.getText().toString(), repassword.getText().toString());
                databaseReference.child(save_phonenumber).setValue(ur);

                startActivity(new Intent(RegisterActivity.this,GetOTP.class));
                overridePendingTransition(R.anim.slide_in_right,android.R.anim.slide_in_left);
                mAuth = FirebaseAuth.getInstance();
            }
        });
    }
    public void loadData(){
        save_phonenumber = sharedPreferences.getString(GetOTP.PHONENUMBER_GETOTP, "");
    }
    private boolean validateName(){
        String noWhiteSpace = "(?=\\S+$)";
        iName = name.getText().toString();
        if(iName.isEmpty()){
            name.setError("Field cannot be empty");
            return false;
        }
        else if(iName.length() >= 15){
            name.setError("Username too long");
            return false;
        }
        else{
            name.setError(null);
            return true;
        }
    }
    private boolean validateEmail(){
        iEmail = email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(iEmail.isEmpty()){
            email.setError("Field cannot be empty");
            return false;
        }
        else if(!iEmail.matches(emailPattern)){
            email.setError("Invalid email address");
            return false;
        }
        else{
            email.setError(null);
            return true;
        }
    }
    //    private boolean validatePhone(){
//        iPhonen = phonenumber.getText().toString();
//        if(iPhonen.isEmpty()){
//            phonenumber.setError("Field cannot be empty");
//            return false;
//        }
//        else{
//            phonenumber.setError(null);
//            return true;
//        }
//    }
    private boolean validatePassword(){
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        iPassword = password.getText().toString();
        if(iName.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }
        else if (!iPassword.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }
    private boolean validateRePassword(){
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        iRePassword = repassword.getText().toString();
        if(iRePassword.isEmpty()){
            repassword.setError("Field cannot be empty");
            return false;
        }
        else if (!iRePassword.matches(passwordVal)) {
            repassword.setError("Password is too weak");
            return false;
        }
        else{
            repassword.setError(null);
            return true;
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }
    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }



}