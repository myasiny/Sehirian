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

public class ExamsActivity extends AppCompatActivity {
    AdView mAdView;
    ImageButton button_back;
    SharedPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);

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
                View center = (View) findViewById(R.id.center);

                int dp_5 = Math.round(getApplicationContext().getResources().getDimension(R.dimen.dp_5));
                int dp_10 = Math.round(getApplicationContext().getResources().getDimension(R.dimen.dp_10));

                Elements exam = result.select("th.level2.leveleven.item.b1b.column-itemname");

                int no = 0;
                for (Element e: exam) {
                    TextView txt_exam = new TextView(getApplicationContext());
                    txt_exam.setId(100100100 + no);
                    txt_exam.setText(e.text());
                    txt_exam.setTextSize(10);
                    txt_exam.setTypeface(null, Typeface.BOLD);
                    txt_exam.setTextColor(Color.parseColor("#FDFDFD"));
                    txt_exam.setBackgroundResource(R.drawable.bg_body_g_d);
                    txt_exam.setMaxLines(1);
                    txt_exam.setPadding(dp_5, 0, 0, 0);
                    txt_exam.setGravity(Gravity.CENTER);
                    RelativeLayout.LayoutParams params_exam = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    TextView txt_type_g = new TextView(getApplicationContext());
                    txt_type_g.setId(200100100 + no);
                    txt_type_g.setText(getString(R.string.hint_grade));
                    txt_type_g.setTextSize(15);
                    txt_type_g.setTextColor(Color.parseColor("#005050"));
                    txt_type_g.setBackgroundResource(R.drawable.bg_body_w_l);
                    txt_type_g.setMaxLines(1);
                    txt_type_g.setPadding(dp_5, 0, 0, 0);
                    txt_type_g.setGravity(Gravity.CENTER);
                    RelativeLayout.LayoutParams params_type_g = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    TextView txt_grade = new TextView(getApplicationContext());
                    txt_grade.setId(300100100 + no);
                    txt_grade.setText(result.select("table.boxaligncenter.generaltable.user-grade").select("td.level2.leveleven.item.b1b.itemcenter.column-grade").get(no).text());
                    txt_grade.setTextSize(15);
                    txt_grade.setTextColor(Color.parseColor("#005050"));
                    txt_grade.setBackgroundResource(R.drawable.bg_body_w_d);
                    txt_grade.setMaxLines(1);
                    txt_grade.setPadding(dp_5, 0, 0, 0);
                    txt_grade.setGravity(Gravity.CENTER);
                    RelativeLayout.LayoutParams params_grade = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    TextView txt_type_l = new TextView(getApplicationContext());
                    txt_type_l.setId(400100100 + no);
                    txt_type_l.setText(getString(R.string.hint_letter));
                    txt_type_l.setTextSize(15);
                    txt_type_l.setTextColor(Color.parseColor("#005050"));
                    txt_type_l.setBackgroundResource(R.drawable.bg_body_w_d);
                    txt_type_l.setMaxLines(1);
                    txt_type_l.setPadding(dp_5, 0, 0, 0);
                    txt_type_l.setGravity(Gravity.CENTER);
                    RelativeLayout.LayoutParams params_type_l = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    TextView txt_letter = new TextView(getApplicationContext());
                    txt_letter.setId(500100100 + no);
                    txt_letter.setText(result.select("table.boxaligncenter.generaltable.user-grade").select("td.level2.leveleven.item.b1b.itemcenter.column-lettergrade").get(no).text());
                    txt_letter.setTextSize(15);
                    txt_letter.setTextColor(Color.parseColor("#005050"));
                    txt_letter.setBackgroundResource(R.drawable.bg_body_w_l);
                    txt_letter.setMaxLines(1);
                    txt_letter.setPadding(dp_5, 0, 0, 0);
                    txt_letter.setGravity(Gravity.CENTER);
                    RelativeLayout.LayoutParams params_letter = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    TextView txt_type_w = new TextView(getApplicationContext());
                    txt_type_w.setId(600100100 + no);
                    txt_type_w.setText(getString(R.string.hint_weight));
                    txt_type_w.setTextSize(15);
                    txt_type_w.setTextColor(Color.parseColor("#005050"));
                    txt_type_w.setBackgroundResource(R.drawable.bg_body_w_l);
                    txt_type_w.setMaxLines(1);
                    txt_type_w.setPadding(dp_5, 0, 0, 0);
                    txt_type_w.setGravity(Gravity.CENTER);
                    RelativeLayout.LayoutParams params_type_w = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    TextView txt_weight = new TextView(getApplicationContext());
                    txt_weight.setId(700100100 + no);
                    txt_weight.setText(result.select("table.boxaligncenter.generaltable.user-grade").select("td.level2.leveleven.item.b1b.itemcenter.column-weight").get(no).text());
                    txt_weight.setTextSize(15);
                    txt_weight.setTextColor(Color.parseColor("#005050"));
                    txt_weight.setBackgroundResource(R.drawable.bg_body_w_d);
                    txt_weight.setMaxLines(1);
                    txt_weight.setPadding(dp_5, 0, 0, 0);
                    txt_weight.setGravity(Gravity.CENTER);
                    RelativeLayout.LayoutParams params_weight = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    if (no == 0) {
                        params_exam.addRule(RelativeLayout.BELOW, divider.getId());
                        params_exam.setMargins(dp_10, dp_10, dp_10, 0);
                    } else {
                        params_exam.addRule(RelativeLayout.BELOW, txt_weight.getId() - 1);
                        params_exam.setMargins(dp_10, 0, dp_10, 0);
                    }
                    layout.addView(txt_exam, params_exam);

                    params_type_g.addRule(RelativeLayout.BELOW, txt_exam.getId());
                    params_type_g.addRule(RelativeLayout.LEFT_OF, center.getId());
                    params_type_g.setMargins(dp_10, 0, 0, 0);
                    layout.addView(txt_type_g, params_type_g);

                    params_grade.addRule(RelativeLayout.BELOW, txt_exam.getId());
                    params_grade.addRule(RelativeLayout.RIGHT_OF, center.getId());
                    params_grade.setMargins(0, 0, dp_10, 0);
                    layout.addView(txt_grade, params_grade);

                    params_type_l.addRule(RelativeLayout.BELOW, txt_grade.getId());
                    params_type_l.addRule(RelativeLayout.LEFT_OF, center.getId());
                    params_type_l.setMargins(dp_10, 0, 0, 0);
                    layout.addView(txt_type_l, params_type_l);

                    params_letter.addRule(RelativeLayout.BELOW, txt_grade.getId());
                    params_letter.addRule(RelativeLayout.RIGHT_OF, center.getId());
                    params_letter.setMargins(0, 0, dp_10, 0);
                    layout.addView(txt_letter, params_letter);

                    params_type_w.addRule(RelativeLayout.BELOW, txt_letter.getId());
                    params_type_w.addRule(RelativeLayout.LEFT_OF, center.getId());
                    params_type_w.setMargins(dp_10, 0, 0, dp_10);
                    layout.addView(txt_type_w, params_type_w);

                    params_weight.addRule(RelativeLayout.BELOW, txt_letter.getId());
                    params_weight.addRule(RelativeLayout.RIGHT_OF, center.getId());
                    params_weight.setMargins(0, 0, dp_10, dp_10);
                    layout.addView(txt_weight, params_weight);

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
