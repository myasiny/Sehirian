package com.myasiny.sehirian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {
    AdView mAdView;
    Button button_share;
    EditText edit_post;
    ImageButton button_back;
    ImageView share_success, share_character, share_fail;
    ProgressBar share_wait;
    SharedPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        button_back = (ImageButton) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("E5BDC074A0A9B6C1B1EB67A1B076A50B").build();
        mAdView.loadAd(adRequest);

        button_share = (Button) findViewById(R.id.button_share);
        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_wait = (ProgressBar) findViewById(R.id.share_wait);
                share_wait.setVisibility(View.VISIBLE);
                share_wait.bringToFront();

                share_success = (ImageView) findViewById(R.id.share_success);
                share_success.setVisibility(View.GONE);

                share_character = (ImageView) findViewById(R.id.share_character);
                share_character.setVisibility(View.GONE);

                share_fail = (ImageView) findViewById(R.id.share_fail);
                share_fail.setVisibility(View.GONE);

                edit_post = (EditText) findViewById(R.id.edit_post);
                if (edit_post.getText().toString().matches("")) {
                    share_wait.setVisibility(View.GONE);
                    share_fail.setVisibility(View.VISIBLE);
                    share_fail.bringToFront();
                } else if (edit_post.getText().toString().length() > 140) {
                    share_wait.setVisibility(View.GONE);
                    share_character.setVisibility(View.VISIBLE);
                    share_character.bringToFront();
                } else {
                    final DatabaseReference post_id = FirebaseDatabase.getInstance().getReference("id");
                    post_id.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Integer value_id = dataSnapshot.getValue(Integer.class);
                            Log.d("Id Post", "Success...\t" + String.valueOf(value_id));

                            DatabaseReference post_public = FirebaseDatabase.getInstance().getReference("timeline").child(String.valueOf(value_id - 1));
                            post_public.setValue(preference.getString("username", "") + "_=>_" + edit_post.getText().toString() + "_=>_" + String.valueOf(Calendar.getInstance().getTime()));

                            DatabaseReference post_private = FirebaseDatabase.getInstance().getReference(preference.getString("username", "")).child(String.valueOf(value_id - 1));
                            post_private.setValue(preference.getString("username", "") + "_=>_" + edit_post.getText().toString() + "_=>_" + String.valueOf(Calendar.getInstance().getTime()));

                            post_id.setValue(value_id - 1);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.w("Id Post", "Fail...\t", error.toException());
                        }
                    });

                    share_wait.setVisibility(View.GONE);
                    share_success.setVisibility(View.VISIBLE);
                    share_success.bringToFront();

                    Intent intent = new Intent(PostActivity.this, TimelineActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
