package com.example.AmateurShipper.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.AmateurShipper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    private FirebaseFirestore mFireStore;
    private CircularProgressButton loginButton;
    private EditText name, email, password, repassword;
    CheckBox acceptRule;
    SharedPreferences sharedPreferences;
    String save_phonenumber;
    private String iName, iEmail, iRePassword, iPassword, codeSent;
    public static final String iEmalvalue = "mail";
    public static final String iPhonevalue = "phone";
    public static final String iNamevalue = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        repassword = findViewById(R.id.editTextRepassword);
        password = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.cirRegisterButton);
        acceptRule = findViewById(R.id.cbRule);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mFireStore = FirebaseFirestore.getInstance();

        iEmail = email.getText().toString();
//        iPhonen = phonenumber.getText().toString();
        iPassword = password.getText().toString();
        sharedPreferences = getSharedPreferences(GetOTP.MyPREFERENCES_GETOTP, Context.MODE_PRIVATE);
        loadData();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateName() || !validateEmail()|| !validatePassword() || !validateRePassword()) {
                    return;
                }
                signUpByEmail();
                databaseReference.child("Account_Shipper").child(save_phonenumber).setValue(email.getText().toString());
                signIn();
                }
            });
        }

    public void signUpByEmail(){
        if (acceptRule.isChecked()){
            mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Dang ky thanh cong!!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Loi!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else Toast.makeText(this, R.string.accept_rule, Toast.LENGTH_SHORT).show();

    }

    public void signIn(){
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(getBaseContext(), RegisterSuccessful.class);
                intent.putExtra(iEmalvalue, iEmail);
                intent.putExtra(iPhonevalue, save_phonenumber);
                intent.putExtra(iNamevalue, iName);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "dang nhap bai", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loadData() {
        save_phonenumber = sharedPreferences.getString(GetOTP.PHONENUMBER_GETOTP, "");
        Toast.makeText(getApplicationContext(), save_phonenumber, Toast.LENGTH_SHORT).show();
    }

    private boolean validateName() {
        String noWhiteSpace = "(?=\\S+$)";
        iName = name.getText().toString();
        if (iName.isEmpty()) {
            name.setError("Field cannot be empty");
            return false;
        } else if (iName.length() >= 15) {
            name.setError("Username too long");
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        iEmail = email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (iEmail.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!iEmail.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }
//
//        private boolean validatePhone(){
//        iPhone = phonenumber.getText().toString();
//        if(iPhonen.isEmpty()){
//            phonenumber.setError("Field cannot be empty");
//            return false;
//        }
//        else{
//            phonenumber.setError(null);
//            return true;
//        }
//    }
    private boolean validatePassword() {
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
        if (iName.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!iPassword.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        }else {
            password.setError(null);
            return true;
        }
    }

    private boolean validateRePassword() {
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
        if (iRePassword.isEmpty()) {
            repassword.setError("Field cannot be empty");
            return false;
        } else if (!iRePassword.matches(passwordVal)) {
            repassword.setError("Password is too weak");
            return false;
        } else if (!iRePassword.matches(iPassword)) {
            repassword.setError("Re-password not matches");
            return false;
        }else {
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

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
    }
}