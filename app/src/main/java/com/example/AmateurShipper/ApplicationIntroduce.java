package com.example.AmateurShipper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;



public class ApplicationIntroduce extends AppCompatActivity {
    Animation topAnim, bottomAnim;
    ImageView nightowl;
    TextView slogan, logo;
    final Handler handler = new Handler(Looper.getMainLooper());
    private static int SPLASH_SCREEN = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_application_introduce);

        Context context;
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        nightowl = findViewById(R.id.image_owl);
        slogan = findViewById(R.id.tv_slogan);
        logo = findViewById(R.id.logo);
//        nightowl.setAnimation(topAnim);
//        logo.setAnimation(bottomAnim);
//        slogan.setAnimation(bottomAnim);

       handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ApplicationIntroduce.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_mid_left);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}