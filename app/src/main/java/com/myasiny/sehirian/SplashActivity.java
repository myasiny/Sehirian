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
    ImageView logo;
    SharedPreferences preference;
    SharedPreferences.Editor preference_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = (ImageView) findViewById(R.id.logo);
        Animation anim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.anim_fade);
        anim.setDuration(750);
        logo.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;

                preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                preference_edit = preference.edit();
                if (preference.getAll().containsKey("userinmemory")) {
                    intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    intent.putExtra("usermail", preference.getString("usermail", ""));
                    intent.putExtra("userpass", preference.getString("userpass", ""));
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
