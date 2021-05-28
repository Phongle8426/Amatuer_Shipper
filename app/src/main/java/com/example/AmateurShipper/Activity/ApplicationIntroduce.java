package com.example.AmateurShipper.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.AmateurShipper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ApplicationIntroduce extends AppCompatActivity {
    Animation topAnim, bottomAnim;
    ImageView nightowl;
    TextView slogan2, slogan1;
    FirebaseAuth mAuth;
    FirebaseFirestore mFireStore;
    com.airbnb.lottie.LottieAnimationView warning;
    TextView banned;
    boolean checkrole = true;
    final Handler handler = new Handler(Looper.getMainLooper());
    private static int SPLASH_SCREEN = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_application_introduce);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        warning = findViewById(R.id.animation_view);
        banned = findViewById(R.id.tv_banned);
        nightowl = findViewById(R.id.image_owl);
        slogan2 = findViewById(R.id.tv_slogan2);
        slogan1 = findViewById(R.id.tv_slogan1);
        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
//        nightowl.setAnimation(topAnim);
//        logo.setAnimation(bottomAnim);
//        slogan.setAnimation(bottomAnim);

        if (user !=null){
            checkRole();
        }else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ApplicationIntroduce.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_mid_left);
                    finish();
                }
            }, SPLASH_SCREEN);
        }
    }

    public void checkRole(){
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
                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            startActivity(intent);
                            //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_mid_left);
                        }else{
                            slogan1.setVisibility(View.GONE);
                            slogan2.setVisibility(View.GONE);
                            nightowl.setVisibility(View.GONE);
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