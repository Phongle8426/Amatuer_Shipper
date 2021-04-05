package com.example.AmateurShipper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class LoginActivity extends AppCompatActivity {
    ImageView loginFacebook;
    FirebaseAuth mAuth;
    EditText editTextPhonenumber, editTextPassword;
    String phonenumber, password;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    CircularProgressButton loginButton;
    TextView forgotPassword;
    CheckBox saveAcount;


    public static final String MyPREFERENCES = "MyPrefs"; //biến lưu của cặp user/password
    public static final String USERNAME = "userNameKey"; // biến lưu username
    public static final String PASS = "passKey";        // biến lưu password
    public static final String REMEMBER = "remember";  // biến lưu lựa chọn remember me
    SharedPreferences sharedpreferences; // khai báo sharedpreference để lưu cặp user/passs
    // private FirebaseAuth mFirebaseAuth;
    //private CallbackManager mCallbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                getWindow().setDecorFitsSystemWindows(false);
//            }
        }
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.cirLoginButton);
        editTextPhonenumber = findViewById(R.id.editTextPhoneNumber);
        editTextPassword = findViewById(R.id.editTextPassword);
        forgotPassword = findViewById(R.id.tv_forgotpassword);
        saveAcount = findViewById(R.id.cbSaveAcount);
        mAuth = FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        loadData();
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_mid_left);
            }
        });

        editTextPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    editTextPassword.setFocusable(false);
                    editTextPassword.setFocusableInTouchMode(true);
                    return true;
                } else {
                    return false;
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatePassword() | !validatePhone()){
                    return;
                }
                loginByPhone(editTextPhonenumber.getText().toString(),editTextPhonenumber.getText().toString());
//                FirebaseApp.initializeApp(getApplicationContext());
//                rootNode = FirebaseDatabase.getInstance();
//                databaseReference = rootNode.getReference().child("users").child(editTextPhonenumber.getText().toString());
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()){
//                            phonenumber = snapshot.child("phone").getValue(String.class);
//                            password = snapshot.child("password").getValue(String.class);
//                            if(password.equals(editTextPassword.getText().toString())){
////                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                                startActivity(intent);
////                                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_mid_left);
//                            }
//                        }
//                        else{
//                            Toast.makeText(LoginActivity.this, "Datasnapshot is not exist!!!!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            }
        });
    }


    public void loginByPhone(final String phone, final String pass){
        mAuth.signInWithEmailAndPassword(phone ,pass)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (saveAcount.isChecked())
                                saveData(phone,pass);
                            else
                                clearData();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_mid_left);
                        }
                    }
                });

        Toast.makeText(getApplicationContext(), phone+"@gmail.com", Toast.LENGTH_SHORT).show();
    }

    private boolean validatePhone(){
        if(editTextPhonenumber.getText().toString().isEmpty()){
            editTextPhonenumber.setError("Field cannot be empty");
            return false;
        }
        else{
            editTextPhonenumber.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        if(editTextPassword.getText().toString().isEmpty()){
            editTextPassword.setError("Field cannot be empty");
            return false;
        }
        else{
            editTextPassword.setError(null);
            return true;
        }
    }
    public void onLoginClick(View View){
        startActivity(new Intent(this,GetOTP.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_mid_left);
    }

    public void saveData(String username, String Pass){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASS, Pass);
        editor.putBoolean(REMEMBER,saveAcount.isChecked());
        editor.commit();
    }
    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    private void loadData() {
        if(sharedpreferences.getBoolean(REMEMBER,false)) {
            String user_name = sharedpreferences.getString(USERNAME, "");
            String passw = sharedpreferences.getString(PASS, "");
            editTextPhonenumber.setText(user_name);
            editTextPassword.setText(passw);
            saveAcount.setChecked(true);
            loginByPhone(user_name+"@gmail.com",passw);
        }
        else
            saveAcount.setChecked(false);
    }
}