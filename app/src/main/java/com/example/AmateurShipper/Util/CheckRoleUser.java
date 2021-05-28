package com.example.AmateurShipper.Util;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.AmateurShipper.Activity.MainActivity;
import com.example.AmateurShipper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CheckRoleUser extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore mFireStore;
    com.airbnb.lottie.LottieAnimationView warning;
    TextView banned;
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_role_user);

        warning = findViewById(R.id.animation_view);
        banned = findViewById(R.id.tv_banned);
        logo = findViewById(R.id.image_owl);
        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();
        String idUser = mAuth.getCurrentUser().getUid();
        DocumentReference docRef = mFireStore.collection("ProfileShipper").document(idUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        document.getData();
                        if (document.get("role").equals("1")||document.get("role").equals("2")){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_mid_left);
                        }else{
                            logo.setVisibility(View.GONE);
                            warning.setVisibility(View.VISIBLE);
                            banned.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}