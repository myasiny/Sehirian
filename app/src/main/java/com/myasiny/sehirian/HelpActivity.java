package com.myasiny.sehirian;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class HelpActivity extends AppCompatActivity {
    AdView mAdView;
    InterstitialAd ad_interstitial;
    ImageButton button_back;
    ImageView pic_timeline, pic_gpa, pic_inbox, pic_event, pic_grade, pic_file, pic_youtube, pic_twitter, pic_github;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        button_back = (ImageButton) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("E5BDC074A0A9B6C1B1EB67A1B076A50B").build();
        mAdView.loadAd(adRequest);

        ad_interstitial = new InterstitialAd(this);
        ad_interstitial.setAdUnitId("ca-app-pub-5486013930231502/1799066711");
        ad_interstitial.loadAd(new AdRequest.Builder().addTestDevice("E5BDC074A0A9B6C1B1EB67A1B076A50B").build());
        ad_interstitial.setAdListener(new AdListener(){
            public void onAdLoaded(){
                ad_interstitial.show();
            }
        });

        pic_timeline = (ImageView) findViewById(R.id.pic_timeline);
        pic_timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/ntwTrFyt-Kw")));
            }
        });

        pic_gpa = (ImageView) findViewById(R.id.pic_gpa);
        pic_gpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/jxjXvgrD2Hg")));
            }
        });

        pic_inbox = (ImageView) findViewById(R.id.pic_inbox);
        pic_inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/YrH8hdARTpg")));
            }
        });

        pic_event = (ImageView) findViewById(R.id.pic_event);
        pic_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/eQhKOsNH3Bs")));
            }
        });

        pic_grade = (ImageView) findViewById(R.id.pic_grade);
        pic_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/q-7N5dNPfwY")));
            }
        });

        pic_file = (ImageView) findViewById(R.id.pic_file);
        pic_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/866LpAyaVRk")));
            }
        });

        pic_youtube = (ImageView) findViewById(R.id.pic_youtube);
        pic_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/channel/UChHZS2SEkcLgJehH8AHTUGA")));
            }
        });

        pic_twitter = (ImageView) findViewById(R.id.pic_twitter);
        pic_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/myasiny13")));
            }
        });

        pic_github = (ImageView) findViewById(R.id.pic_github);
        pic_github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/myasiny")));
            }
        });
    }
}