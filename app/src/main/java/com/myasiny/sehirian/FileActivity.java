package com.myasiny.sehirian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class FileActivity extends AppCompatActivity {
    AdView mAdView;
    ImageButton button_back;
    SharedPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

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

                int dp_5 = Math.round(getApplicationContext().getResources().getDimension(R.dimen.dp_5));
                int dp_10 = Math.round(getApplicationContext().getResources().getDimension(R.dimen.dp_10));
                int dp_20 = Math.round(getApplicationContext().getResources().getDimension(R.dimen.dp_20));

                Elements file = result.select("div.col-lg-6");

                int no = 0;
                for (Element f: file) {
                    ImageView check_detail = new ImageView(getApplicationContext());
                    check_detail.setId(100100100 + no);
                    check_detail.setImageResource(R.drawable.icon_detail);
                    check_detail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    check_detail.setAdjustViewBounds(true);
                    RelativeLayout.LayoutParams params_detail = new RelativeLayout.LayoutParams(dp_20, dp_20);

                    TextView txt_course = new TextView(getApplicationContext());
                    txt_course.setId(200100100 + no);
                    txt_course.setText(f.select("h4.h5").select("a").text());
                    txt_course.setTextSize(15);
                    txt_course.setTextColor(Color.parseColor("#FDFDFD"));
                    txt_course.setMaxLines(1);
                    txt_course.setPadding(dp_5, 0, 0, 0);
                    txt_course.setGravity(Gravity.CENTER_VERTICAL);
                    RelativeLayout.LayoutParams params_course = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT
                    );

                    if (no % 2 == 0) {
                        txt_course.setBackgroundResource(R.drawable.bg_body_g_d);
                    } else {
                        txt_course.setBackgroundResource(R.drawable.bg_body_g_l);
                    }

                    if (no == 0) {
                        params_detail.addRule(RelativeLayout.BELOW, divider.getId());
                        params_course.addRule(RelativeLayout.BELOW, divider.getId());
                    } else {
                        params_detail.addRule(RelativeLayout.BELOW, txt_course.getId() - 1);
                        params_course.addRule(RelativeLayout.BELOW, txt_course.getId() - 1);
                    }
                    params_detail.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    params_detail.setMargins(0, dp_10, dp_10, 0);
                    layout.addView(check_detail, params_detail);

                    params_course.addRule(RelativeLayout.LEFT_OF, check_detail.getId());
                    params_course.addRule(RelativeLayout.ALIGN_BOTTOM, check_detail.getId());
                    params_course.setMargins(dp_10, dp_10, 0, 0);
                    layout.addView(txt_course, params_course);

                    final Intent intent = new Intent(FileActivity.this, DocsActivity.class);
                    intent.putExtra("toURL", f.select("h4.h5").select("a").attr("href"));

                    check_detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });

                    txt_course.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });

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
