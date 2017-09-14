package com.myasiny.sehirian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class TimelineActivity extends FragmentActivity {
    AdView mAdView;
    ImageView button_post, check_menu, check_profile;
    PopupWindow popup_menu, popup_profile;
    SharedPreferences.Editor preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("E5BDC074A0A9B6C1B1EB67A1B076A50B").build();
        mAdView.loadAd(adRequest);

        button_post = (ImageView) findViewById(R.id.button_post);
        button_post.bringToFront();
        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimelineActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

        final int[] isMenu = {0};
        check_menu = (ImageView) findViewById(R.id.check_menu);
        check_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenu[0] == 0) {
                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.popup_menu, null);
                    popup_menu = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        popup_menu.showAsDropDown(v, 0, 15, Gravity.TOP | Gravity.LEFT);
                    } else {
                        popup_menu.showAtLocation(v, Gravity.LEFT, 0, 0);
                    }

                    Button button_gpa = (Button) layout.findViewById(R.id.button_gpa);
                    button_gpa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TimelineActivity.this, GpaActivity.class);
                            startActivity(intent);
                        }
                    });

                    Button button_help = (Button) layout.findViewById(R.id.button_help);
                    button_help.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TimelineActivity.this, HelpActivity.class);
                            startActivity(intent);
                        }
                    });

                    Button button_logout = (Button) layout.findViewById(R.id.button_logout);
                    button_logout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            preference.clear();
                            preference.commit();

                            Intent intent = new Intent(TimelineActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    isMenu[0] = 1;
                } else {
                    popup_menu.dismiss();
                    isMenu[0] = 0;
                }
            }
        });

        final int[] isProfile = {0};
        check_profile = (ImageView) findViewById(R.id.check_profile);
        check_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProfile[0] == 0) {
                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.popup_profile, null);
                    popup_profile = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        popup_profile.showAsDropDown(v, 0, 15, Gravity.TOP | Gravity.RIGHT);
                    } else {
                        popup_profile.showAtLocation(v, Gravity.RIGHT, 0, 0);
                    }

                    Button button_profile = (Button) layout.findViewById(R.id.button_profile);
                    button_profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                    });

                    Button button_inbox = (Button) layout.findViewById(R.id.button_inbox);
                    button_inbox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TimelineActivity.this, InboxActivity.class);
                            startActivity(intent);
                        }
                    });

                    Button button_events = (Button) layout.findViewById(R.id.button_events);
                    button_events.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TimelineActivity.this, EventActivity.class);
                            startActivity(intent);
                        }
                    });

                    Button button_grades = (Button) layout.findViewById(R.id.button_grades);
                    button_grades.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TimelineActivity.this, GradeActivity.class);
                            startActivity(intent);
                        }
                    });

                    Button button_files = (Button) layout.findViewById(R.id.button_files);
                    button_files.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TimelineActivity.this, FileActivity.class);
                            startActivity(intent);
                        }
                    });

                    isProfile[0] = 1;
                } else {
                    popup_profile.dismiss();
                    isProfile[0] = 0;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        preference.clear();
        preference.commit();

        Intent intent = new Intent(TimelineActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
