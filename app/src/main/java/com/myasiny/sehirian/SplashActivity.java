package com.myasiny.sehirian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    ImageView pic_logo;
    SharedPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation anim_fade = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.anim_fade);
        pic_logo = (ImageView) findViewById(R.id.pic_logo);
        pic_logo.startAnimation(anim_fade);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;

                preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if (preference.getAll().containsKey("isChecked")) {
                    intent = new Intent(SplashActivity.this, TimelineActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
