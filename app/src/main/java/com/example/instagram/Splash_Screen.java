package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity {

    public static int Splash = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                    Intent splashScreen = new Intent(Splash_Screen.this, MainActivity.class);
                    startActivity(splashScreen);
                    finish();
            }
        }, Splash);
    }
}
