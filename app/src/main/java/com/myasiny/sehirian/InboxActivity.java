package com.myasiny.sehirian;

import android.content.Intent;
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

public class InboxActivity extends AppCompatActivity {
    AdView mAdView;
    ImageButton button_back;
    SharedPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

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
                res = Jsoup.connect("https://lms.sehir.edu.tr/message/index.php")
                        .cookies(res.cookies())
                        .method(Connection.Method.GET).execute();
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

                String userid = result.select("div.popover-region.collapsed.popover-region-messages").attr("data-userid");
                Elements inbox = result.select("div.contacts").first().select("div[data-region=contact]");

                int no = 0;
                for (Element i: inbox) {
                    ImageView check_detail = new ImageView(getApplicationContext());
                    check_detail.setId(100100100 + no);
                    check_detail.setImageResource(R.drawable.icon_detail);
                    check_detail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    check_detail.setAdjustViewBounds(true);
                    RelativeLayout.LayoutParams params_detail = new RelativeLayout.LayoutParams(dp_20, dp_20);

                    TextView txt_title = new TextView(getApplicationContext());
                    txt_title.setId(200100100 + no);
                    txt_title.setText(i.select("div.name").text());
                    txt_title.setTextSize(10);
                    txt_title.setTypeface(null, Typeface.BOLD);
                    txt_title.setTextColor(Color.parseColor("#FDFDFD"));
                    txt_title.setBackgroundResource(R.drawable.bg_body_g_d);
                    txt_title.setMaxLines(1);
                    txt_title.setPadding(dp_5, 0, 0, 0);
                    txt_title.setGravity(Gravity.CENTER_VERTICAL);
                    RelativeLayout.LayoutParams params_title = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT
                    );

                    TextView txt_body = new TextView(getApplicationContext());
                    txt_body.setId(300100100 + no);
                    txt_body.setText(i.select("span[data-region=last-message-text]").text());
                    txt_body.setTextSize(15);
                    txt_body.setTextColor(Color.parseColor("#005050"));
                    txt_body.setBackgroundResource(R.drawable.bg_body_w_l);
                    txt_body.setMaxLines(1);
                    txt_body.setPadding(dp_5, 0, 0, 0);
                    txt_body.setGravity(Gravity.CENTER_VERTICAL);
                    RelativeLayout.LayoutParams params_body = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    if (no == 0) {
                        params_detail.addRule(RelativeLayout.BELOW, divider.getId());
                        params_title.addRule(RelativeLayout.BELOW, divider.getId());
                        params_detail.setMargins(0, dp_10, dp_10, 0);
                        params_title.setMargins(dp_10, dp_10, 0, 0);
                    } else {
                        params_detail.addRule(RelativeLayout.BELOW, txt_body.getId() - 1);
                        params_title.addRule(RelativeLayout.BELOW, txt_body.getId() - 1);
                        params_detail.setMargins(0, 0, dp_10, 0);
                        params_title.setMargins(dp_10, 0, 0, 0);
                    }
                    params_detail.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    layout.addView(check_detail, params_detail);

                    params_title.addRule(RelativeLayout.LEFT_OF, check_detail.getId());
                    params_title.addRule(RelativeLayout.ALIGN_BOTTOM, check_detail.getId());
                    layout.addView(txt_title, params_title);

                    params_body.addRule(RelativeLayout.BELOW, txt_title.getId());
                    params_body.setMargins(dp_10, 0, dp_10, dp_10);
                    layout.addView(txt_body, params_body);

                    final Intent intent = new Intent(InboxActivity.this, MessageActivity.class);
                    intent.putExtra("toURL", "https://lms.sehir.edu.tr/message/index.php?user=" + userid + "&id=" + i.attr("data-userid"));

                    check_detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });

                    txt_title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });

                    txt_body.setOnClickListener(new View.OnClickListener() {
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
