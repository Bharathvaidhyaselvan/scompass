package com.packages.scompass;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_DURATION = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splashLogo = findViewById(R.id.splash_logo);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        splashLogo.startAnimation(rotation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent mainIntent;
                if (user != null) {
                    mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                } else {
                    mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_DURATION);
    }
}

