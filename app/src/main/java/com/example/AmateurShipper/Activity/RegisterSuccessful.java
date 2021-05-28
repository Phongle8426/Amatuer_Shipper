package com.example.AmateurShipper.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.AmateurShipper.Model.ProfileObject;
import com.example.AmateurShipper.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.AmateurShipper.Activity.RegisterActivity.iEmalvalue;
import static com.example.AmateurShipper.Activity.RegisterActivity.iNamevalue;
import static com.example.AmateurShipper.Activity.RegisterActivity.iPhonevalue;

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
        String idUser = mAuth.getCurrentUser().getUid();
        Log.i("TAG", "onCreate: " + iName + "/" + iPhone + "/" + iEmail);
        ProfileObject profileObject = new ProfileObject(iName,iPhone,"",iEmail,"","",
                "0","",idUser,"0","","1");
        mFireStore.collection("ProfileShipper").document(idUser)
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