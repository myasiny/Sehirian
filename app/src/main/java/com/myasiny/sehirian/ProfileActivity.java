package com.myasiny.sehirian;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    AdView mAdView;
    ImageButton button_back;
    ImageView pic_profile;
    SharedPreferences preference;
    TextView txt_name, txt_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        pic_profile = (ImageView) findViewById(R.id.pic_profile);
        Picasso.with(ProfileActivity.this).load(preference.getString("userpict", "")).fit().centerInside().placeholder(R.drawable.icon_profile).into(pic_profile);

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_name.setText(preference.getString("username", ""));
        txt_name.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));

        txt_mail = (TextView) findViewById(R.id.txt_mail);
        txt_mail.setText(preference.getString("usermail", ""));

        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference(preference.getString("username", ""));
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int c = 0; c < layout.getChildCount(); c++) {
                    layout.getChildAt(c).setVisibility(View.GONE);
                }

                int dp_5 = Math.round(getResources().getDimension(R.dimen.dp_5));
                int dp_10 = Math.round(getResources().getDimension(R.dimen.dp_10));
                int dp_25 = Math.round(getResources().getDimension(R.dimen.dp_25));
                int dp_50 = Math.round(getResources().getDimension(R.dimen.dp_50));

                int no = 0;
                for (final DataSnapshot child: dataSnapshot.getChildren()) {
                    String value = child.getValue(String.class);
                    Log.d("Private Post", "Success...\t" + value);

                    String [] array_value = value.split("_=>_");
                    final String value_user = array_value[0];
                    final String value_post = array_value[1];
                    final String value_date = array_value[2];

                    ImageView pic_avatar = new ImageView(getApplicationContext());
                    pic_avatar.setId(100100100 + no);
                    pic_avatar.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    pic_avatar.setAdjustViewBounds(true);
                    pic_avatar.setPadding(dp_10, dp_10, dp_10, dp_10);
                    RelativeLayout.LayoutParams params_avatar = new RelativeLayout.LayoutParams(
                            dp_50,
                            RelativeLayout.LayoutParams.MATCH_PARENT
                    );

                    TextView txt_user = new TextView(getApplicationContext());
                    txt_user.setId(200100100 + no);
                    txt_user.setText(value_user);
                    txt_user.setTextSize(10);
                    txt_user.setTypeface(null, Typeface.BOLD);
                    txt_user.setTextColor(Color.parseColor("#00A0AF"));
                    txt_user.setMaxLines(1);
                    txt_user.setPadding(dp_5, dp_5, dp_5, dp_5);
                    txt_user.setGravity(Gravity.CENTER_VERTICAL);
                    RelativeLayout.LayoutParams params_user = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    TextView txt_post = new TextView(getApplicationContext());
                    txt_post.setId(300100100 + no);
                    txt_post.setText(value_post);
                    txt_post.setTextSize(15);
                    txt_post.setTextColor(Color.parseColor("#005050"));
                    txt_post.setPadding(dp_5, 0, dp_5, 0);
                    txt_post.setGravity(Gravity.CENTER_VERTICAL);
                    RelativeLayout.LayoutParams params_post = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    ImageView button_remove = new ImageView(getApplicationContext());
                    button_remove.setId(400100100 + no);
                    button_remove.setImageResource(R.drawable.icon_remove);
                    button_remove.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    button_remove.setAdjustViewBounds(true);
                    button_remove.setPadding(dp_5, dp_5, dp_5, dp_5);
                    RelativeLayout.LayoutParams params_remove = new RelativeLayout.LayoutParams(dp_25, dp_25);

                    TextView txt_date = new TextView(getApplicationContext());
                    txt_date.setId(500100100 + no);
                    txt_date.setText(value_date);
                    txt_date.setTextSize(10);
                    txt_date.setTextColor(Color.parseColor("#B1B1B1"));
                    txt_user.setMaxLines(1);
                    txt_date.setPadding(dp_5, dp_5, dp_5, dp_5);
                    txt_date.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
                    RelativeLayout.LayoutParams params_date = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    if (value_user.substring(0, 1).toUpperCase().equals("A")) {
                        pic_avatar.setImageResource(R.drawable.avatar_a);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("B")) {
                        pic_avatar.setImageResource(R.drawable.avatar_b);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("C")) {
                        pic_avatar.setImageResource(R.drawable.avatar_c);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("D")) {
                        pic_avatar.setImageResource(R.drawable.avatar_d);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("E")) {
                        pic_avatar.setImageResource(R.drawable.avatar_e);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("F")) {
                        pic_avatar.setImageResource(R.drawable.avatar_f);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("G")) {
                        pic_avatar.setImageResource(R.drawable.avatar_g);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("H")) {
                        pic_avatar.setImageResource(R.drawable.avatar_h);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("I")) {
                        pic_avatar.setImageResource(R.drawable.avatar_i);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("J")) {
                        pic_avatar.setImageResource(R.drawable.avatar_j);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("K")) {
                        pic_avatar.setImageResource(R.drawable.avatar_k);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("L")) {
                        pic_avatar.setImageResource(R.drawable.avatar_l);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("M")) {
                        pic_avatar.setImageResource(R.drawable.avatar_m);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("N")) {
                        pic_avatar.setImageResource(R.drawable.avatar_n);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("O")) {
                        pic_avatar.setImageResource(R.drawable.avatar_o);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("P")) {
                        pic_avatar.setImageResource(R.drawable.avatar_p);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("R")) {
                        pic_avatar.setImageResource(R.drawable.avatar_r);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("S")) {
                        pic_avatar.setImageResource(R.drawable.avatar_s);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("T")) {
                        pic_avatar.setImageResource(R.drawable.avatar_t);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("U")) {
                        pic_avatar.setImageResource(R.drawable.avatar_u);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("V")) {
                        pic_avatar.setImageResource(R.drawable.avatar_v);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("Y")) {
                        pic_avatar.setImageResource(R.drawable.avatar_y);
                    } else if (value_user.substring(0, 1).toUpperCase().equals("Z")) {
                        pic_avatar.setImageResource(R.drawable.avatar_z);
                    } else {
                        pic_avatar.setImageResource(R.drawable.avatar_x);
                    }

                    if (no % 2 == 0) {
                        pic_avatar.setBackgroundResource(R.drawable.bg_body_w_l);
                        txt_user.setBackgroundResource(R.drawable.bg_body_w_l);
                        txt_post.setBackgroundResource(R.drawable.bg_body_w_l);
                        txt_date.setBackgroundResource(R.drawable.bg_body_w_l);
                        button_remove.setBackgroundResource(R.drawable.bg_body_w_l);
                    } else {
                        pic_avatar.setBackgroundResource(R.drawable.bg_body_w_d);
                        txt_user.setBackgroundResource(R.drawable.bg_body_w_d);
                        txt_post.setBackgroundResource(R.drawable.bg_body_w_d);
                        txt_date.setBackgroundResource(R.drawable.bg_body_w_d);
                        button_remove.setBackgroundResource(R.drawable.bg_body_w_d);
                    }

                    if (no == 0) {
                        params_avatar.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    } else {
                        params_avatar.addRule(RelativeLayout.BELOW, txt_date.getId() - 1);
                        params_user.addRule(RelativeLayout.BELOW, txt_date.getId() - 1);
                    }
                    params_avatar.addRule(RelativeLayout.ALIGN_BOTTOM, button_remove.getId());
                    params_avatar.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    layout.addView(pic_avatar, params_avatar);

                    params_user.addRule(RelativeLayout.RIGHT_OF, pic_avatar.getId());
                    layout.addView(txt_user, params_user);

                    params_post.addRule(RelativeLayout.BELOW, txt_user.getId());
                    params_post.addRule(RelativeLayout.RIGHT_OF, pic_avatar.getId());
                    layout.addView(txt_post, params_post);

                    params_remove.addRule(RelativeLayout.BELOW, txt_post.getId());
                    params_remove.addRule(RelativeLayout.RIGHT_OF, pic_avatar.getId());
                    layout.addView(button_remove, params_remove);

                    params_date.addRule(RelativeLayout.BELOW, txt_post.getId());
                    params_date.addRule(RelativeLayout.RIGHT_OF, button_remove.getId());
                    params_date.addRule(RelativeLayout.ALIGN_BOTTOM, button_remove.getId());
                    params_date.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    layout.addView(txt_date, params_date);

                    button_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            child.getRef().removeValue();

                            DatabaseReference value_public = FirebaseDatabase.getInstance().getReference("timeline").child(child.getKey());
                            value_public.removeValue();
                        }
                    });

                    no += 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Private Post", "Fail...\t", error.toException());
            }
        });
    }
}