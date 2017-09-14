package com.myasiny.sehirian;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EventActivity extends AppCompatActivity {
    AdView mAdView;
    ImageButton button_back;
    SharedPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

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

        new ParseLMS().execute(preference.getString("usermail", ""), preference.getString("userpasw", ""));
    }

    private class ParseLMS extends AsyncTask<String,Integer,Document> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... params) {
            Document doc = null;
            try {
                Connection.Response res = Jsoup.connect("https://lms.sehir.edu.tr/login/index.php")
                        .data("username", params[0])
                        .data("password", params[1])
                        .method(Connection.Method.POST).followRedirects(true).execute();
                doc = res.parse();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return doc;
        }

        @Override
        public void onPostExecute(Document result) {
            if (result != null) {
                RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
                View divider = (View) findViewById(R.id.divider_top);
                View center = (View) findViewById(R.id.center);

                int dp_5 = Math.round(getApplicationContext().getResources().getDimension(R.dimen.dp_5));
                int dp_10 = Math.round(getApplicationContext().getResources().getDimension(R.dimen.dp_10));

                Elements event = result.select("aside[data-block=calendar_upcoming]").select("div.event");

                int no = 0;
                for (Element e: event) {
                    TextView txt_course = new TextView(getApplicationContext());
                    txt_course.setId(100100100 + no);
                    txt_course.setText(e.select("div.course").select("a").text());
                    txt_course.setTextSize(10);
                    txt_course.setTypeface(null, Typeface.BOLD);
                    txt_course.setTextColor(Color.parseColor("#FDFDFD"));
                    txt_course.setBackgroundResource(R.drawable.bg_body_g_d);
                    txt_course.setMaxLines(1);
                    txt_course.setPadding(dp_5, 0, 0, 0);
                    txt_course.setGravity(Gravity.CENTER);
                    RelativeLayout.LayoutParams params_course = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    TextView txt_event = new TextView(getApplicationContext());
                    txt_event.setId(200100100 + no);
                    txt_event.setText(e.select("a").first().text());
                    txt_event.setTextSize(10);
                    txt_event.setTypeface(null, Typeface.BOLD);
                    txt_event.setTextColor(Color.parseColor("#FDFDFD"));
                    txt_event.setBackgroundResource(R.drawable.bg_body_g_l);
                    txt_event.setMaxLines(1);
                    txt_event.setPadding(dp_5, 0, 0, 0);
                    txt_event.setGravity(Gravity.CENTER);
                    RelativeLayout.LayoutParams params_event = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    String day, hour;
                    String[] array_time = e.select("div.date").text().split(",");
                    if (array_time.length > 2) {
                        day = array_time[0] + ",\t" + array_time[1];
                        hour = array_time[2];
                    } else {
                        day = array_time[0];
                        hour = array_time[1];
                    }

                    TextView txt_day = new TextView(getApplicationContext());
                    txt_day.setId(300100100 + no);
                    txt_day.setText(day);
                    txt_day.setTextSize(15);
                    txt_day.setTextColor(Color.parseColor("#005050"));
                    txt_day.setBackgroundResource(R.drawable.bg_body_w_l);
                    txt_day.setMaxLines(1);
                    txt_day.setPadding(dp_5, 0, 0, 0);
                    txt_day.setGravity(Gravity.CENTER);
                    RelativeLayout.LayoutParams params_day = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    TextView txt_hour = new TextView(getApplicationContext());
                    txt_hour.setId(400100100 + no);
                    txt_hour.setText(hour);
                    txt_hour.setTextSize(15);
                    txt_hour.setTextColor(Color.parseColor("#005050"));
                    txt_hour.setBackgroundResource(R.drawable.bg_body_w_d);
                    txt_hour.setMaxLines(1);
                    txt_hour.setPadding(dp_5, 0, 0, 0);
                    txt_hour.setGravity(Gravity.CENTER);
                    RelativeLayout.LayoutParams params_hour = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    if (no == 0) {
                        params_course.addRule(RelativeLayout.BELOW, divider.getId());
                        params_event.addRule(RelativeLayout.BELOW, divider.getId());
                        params_course.setMargins(dp_10, dp_10, 0, 0);
                        params_event.setMargins(0, dp_10, dp_10, 0);
                    } else {
                        params_course.addRule(RelativeLayout.BELOW, txt_day.getId() - 1);
                        params_event.addRule(RelativeLayout.BELOW, txt_day.getId() - 1);
                        params_course.setMargins(dp_10, 0, 0, 0);
                        params_event.setMargins(0, 0, dp_10, 0);
                    }
                    params_course.addRule(RelativeLayout.LEFT_OF, center.getId());
                    layout.addView(txt_course, params_course);

                    params_event.addRule(RelativeLayout.RIGHT_OF, center.getId());
                    layout.addView(txt_event, params_event);

                    params_day.addRule(RelativeLayout.BELOW, txt_course.getId());
                    params_day.addRule(RelativeLayout.LEFT_OF, center.getId());
                    params_day.setMargins(dp_10, 0, 0, dp_10);
                    layout.addView(txt_day, params_day);

                    params_hour.addRule(RelativeLayout.BELOW, txt_course.getId());
                    params_hour.addRule(RelativeLayout.RIGHT_OF, center.getId());
                    params_hour.setMargins(0, 0, dp_10, dp_10);
                    layout.addView(txt_hour, params_hour);

                    no += 1;
                }

                ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
                progress.setVisibility(View.GONE);
            } else {
                Toast.makeText(getApplicationContext(), R.string.hint_error, Toast.LENGTH_LONG).show();
            }
        }
    }
}