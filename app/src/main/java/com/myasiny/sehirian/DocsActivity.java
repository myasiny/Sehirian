package com.myasiny.sehirian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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

public class DocsActivity extends AppCompatActivity {
    AdView mAdView;
    ImageButton button_back;
    SharedPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docs);

        button_back = (ImageButton) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();

        preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("E5BDC074A0A9B6C1B1EB67A1B076A50B").build();
        mAdView.loadAd(adRequest);

        new ParseLMS().execute(preference.getString("usermail", ""), preference.getString("userpasw", ""), intent.getExtras().getString("toURL"));
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
                res = Jsoup.connect(params[2])
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

                Elements file = result.select("a[class~=^$]");

                int no = 0;
                for (Element f: file) {
                    ImageView check_download = new ImageView(getApplicationContext());
                    check_download.setId(100100100 + no);
                    check_download.setImageResource(R.drawable.icon_download);
                    check_download.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    check_download.setAdjustViewBounds(true);
                    RelativeLayout.LayoutParams params_download = new RelativeLayout.LayoutParams(dp_20, dp_20);

                    TextView txt_file = new TextView(getApplicationContext());
                    txt_file.setId(200100100 + no);
                    txt_file.setText(f.select("span.instancename").text());
                    txt_file.setTextSize(15);
                    txt_file.setTextColor(Color.parseColor("#005050"));
                    txt_file.setMaxLines(1);
                    txt_file.setPadding(dp_5, 0, 0, 0);
                    txt_file.setGravity(Gravity.CENTER_VERTICAL);
                    RelativeLayout.LayoutParams params_file = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT
                    );

                    if (no % 2 == 0) {
                        txt_file.setBackgroundResource(R.drawable.bg_body_w_d);
                    } else {
                        txt_file.setBackgroundResource(R.drawable.bg_body_w_l);
                    }

                    if (no == 0) {
                        params_download.addRule(RelativeLayout.BELOW, divider.getId());
                        params_file.addRule(RelativeLayout.BELOW, divider.getId());
                    } else {
                        params_download.addRule(RelativeLayout.BELOW, check_download.getId() - 1);
                        params_file.addRule(RelativeLayout.BELOW, check_download.getId() - 1);
                    }
                    params_download.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    params_download.setMargins(0, dp_10, dp_10, 0);
                    layout.addView(check_download, params_download);

                    params_file.addRule(RelativeLayout.LEFT_OF, check_download.getId());
                    params_file.addRule(RelativeLayout.ALIGN_BOTTOM, check_download.getId());
                    params_file.setMargins(dp_10, dp_10, 0, 0);
                    layout.addView(txt_file, params_file);

                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(f.attr("href")));

                    check_download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });

                    txt_file.setOnClickListener(new View.OnClickListener() {
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
