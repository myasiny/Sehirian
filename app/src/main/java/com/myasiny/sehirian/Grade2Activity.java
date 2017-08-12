package com.myasiny.sehirian;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Grade2Activity extends AppCompatActivity {
    ImageButton back;
    String usermail, userpass, url2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade2);

        Intent intent = getIntent();
        usermail = intent.getExtras().getString("usermail");
        userpass = intent.getExtras().getString("userpass");
        url2 = intent.getExtras().getString("url2");

        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new ParseLMS().execute(usermail, userpass, url2);
    }

    private class ParseLMS extends AsyncTask<String,Integer,Document> {
        final String url = "https://lms.sehir.edu.tr/login/index.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... params) {
            Document doc = null;
            try {
                Connection.Response res = Jsoup.connect(url)
                        .data("username", params[0])
                        .data("password", params[1])
                        .method(Connection.Method.POST).followRedirects(true).execute();
                res = Jsoup.connect(url2).cookies(res.cookies()).method(Connection.Method.GET).execute();
                doc = res.parse();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return doc;
        }

        @Override
        public void onPostExecute(Document result) {
            if (result != null) {
                int no = 0;
                RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);

                Elements grade = result.select("table.boxaligncenter.generaltable.user-grade");
                Elements exam = result.select("th.level2.leveleven.item.b1b.column-itemname");
                for (Element e: exam) {
                    TextView title = new TextView(getApplicationContext());
                    title.setMaxLines(1);
                    title.setId(no+100100100);
                    title.setTextColor(Color.parseColor("#FDFDFD"));
                    title.setBackgroundResource(R.drawable.title_event);
                    title.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                    RelativeLayout.LayoutParams p_title = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

                    TextView type1 = new TextView(getApplicationContext());
                    type1.setMaxLines(1);
                    type1.setTextSize(15);
                    type1.setId(no+200100100);
                    type1.setTextColor(Color.parseColor("#FDFDFD"));
                    type1.setGravity(Gravity.CENTER);
                    type1.setBackgroundResource(R.drawable.body_type);
                    RelativeLayout.LayoutParams p_type1 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    Elements point = grade.select("td.level2.leveleven.item.b1b.itemcenter.column-grade");
                    TextView body1 = new TextView(getApplicationContext());
                    body1.setMaxLines(1);
                    body1.setTextSize(15);
                    body1.setId(no+300100100);
                    body1.setTextColor(Color.parseColor("#001B25"));
                    body1.setBackgroundResource(R.drawable.body_event);
                    RelativeLayout.LayoutParams p_body1 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

                    TextView type2 = new TextView(getApplicationContext());
                    type2.setMaxLines(1);
                    type2.setTextSize(15);
                    type2.setId(no+200200100);
                    type2.setTextColor(Color.parseColor("#FDFDFD"));
                    type2.setGravity(Gravity.CENTER);
                    type2.setBackgroundResource(R.drawable.body_detail);
                    RelativeLayout.LayoutParams p_type2 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    Elements letter = grade.select("td.level2.leveleven.item.b1b.itemcenter.column-lettergrade");
                    TextView body2 = new TextView(getApplicationContext());
                    body2.setMaxLines(1);
                    body2.setTextSize(15);
                    body2.setId(no+300200100);
                    body2.setTextColor(Color.parseColor("#001B25"));
                    body2.setBackgroundResource(R.drawable.body_alternative);
                    RelativeLayout.LayoutParams p_body2 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

                    TextView type3 = new TextView(getApplicationContext());
                    type3.setMaxLines(1);
                    type3.setTextSize(15);
                    type3.setId(no+200300100);
                    type3.setTextColor(Color.parseColor("#FDFDFD"));
                    type3.setGravity(Gravity.CENTER);
                    type3.setBackgroundResource(R.drawable.body_type);
                    RelativeLayout.LayoutParams p_type3 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    Elements weight = grade.select("td.level2.leveleven.item.b1b.itemcenter.column-weight");
                    TextView body3 = new TextView(getApplicationContext());
                    body3.setMaxLines(1);
                    body3.setTextSize(15);
                    body3.setId(no+300300100);
                    body3.setTextColor(Color.parseColor("#001B25"));
                    body3.setBackgroundResource(R.drawable.body_event);
                    RelativeLayout.LayoutParams p_body3 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

                    if (no == 0) {
                        p_title.setMargins(40,40,40,0);
                        p_title.addRule(RelativeLayout.BELOW, back.getId());
                    } else {
                        p_title.setMargins(40,0,40,0);
                        p_title.addRule(RelativeLayout.BELOW, body3.getId()-1);
                    }
                    if (e.text().length() > 50) {
                        title.setText("\t" + e.text().substring(0, 50) + "...");
                    } else {
                        title.setText("\t" + e.text());
                    }
                    layout.addView(title, p_title);

                    p_type1.setMargins(40,0,0,0);
                    p_type1.addRule(RelativeLayout.BELOW, title.getId());
                    p_type1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    type1.setText("\tGrade:\t");
                    layout.addView(type1, p_type1);

                    p_body1.setMargins(40,0,40,0);
                    p_body1.addRule(RelativeLayout.BELOW, title.getId());
                    p_body1.addRule(RelativeLayout.RIGHT_OF, type1.getId());
                    body1.setText("\t" + point.get(no).text() + "\t");
                    layout.addView(body1, p_body1);

                    p_type2.setMargins(40,0,0,0);
                    p_type2.addRule(RelativeLayout.BELOW, type1.getId());
                    p_type2.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    type2.setText("\tLetter:\t");
                    layout.addView(type2, p_type2);

                    p_body2.setMargins(40,0,40,0);
                    p_body2.addRule(RelativeLayout.BELOW, type1.getId());
                    p_body2.addRule(RelativeLayout.RIGHT_OF, type2.getId());
                    body2.setText("\t" + letter.get(no).text() + "\t");
                    layout.addView(body2, p_body2);

                    p_type3.setMargins(40,0,0,40);
                    p_type3.addRule(RelativeLayout.BELOW, type2.getId());
                    p_type3.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    type3.setText("\tWeight:\t");
                    layout.addView(type3, p_type3);

                    p_body3.setMargins(40,0,40,40);
                    p_body3.addRule(RelativeLayout.BELOW, type2.getId());
                    p_body3.addRule(RelativeLayout.RIGHT_OF, type3.getId());
                    body3.setText("\t" + weight.get(no).text() + "\t");
                    layout.addView(body3, p_body3);

                    no += 1;
                }

                ProgressBar spinner = (ProgressBar) findViewById(R.id.spinner);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(getApplicationContext(), R.string.popup_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
