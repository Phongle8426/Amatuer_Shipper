package com.example.AmateurShipper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.AmateurShipper.Model.ProfileObject;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.AmateurShipper.RegisterActivity.iEmalvalue;
import static com.example.AmateurShipper.RegisterActivity.iNamevalue;
import static com.example.AmateurShipper.RegisterActivity.iPhonevalue;

public class RegisterSuccessful extends AppCompatActivity {
    FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_successful);
        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String iName = intent.getStringExtra(iNamevalue);
        String iPhone = intent.getStringExtra(iPhonevalue);
        String iEmail = intent.getStringExtra(iEmalvalue);

        ProfileObject profileObject = new ProfileObject(iName,iPhone,null,iEmail,null,null,
                "0",null,"aa","0",null,"1");
        mFireStore.collection("ProfileShipper").document( mAuth.getCurrentUser().getUid())
                .set(profileObject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //  Toast.makeText(RegisterActivity.this, "cap nhat thanh cong", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterSuccessful.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_mid_left);
            }
        });

    }
}