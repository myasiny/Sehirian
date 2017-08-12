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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GradeActivity extends AppCompatActivity {
    ImageButton back;
    String usermail, userpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        Intent intent = getIntent();
        usermail = intent.getExtras().getString("usermail");
        userpass = intent.getExtras().getString("userpass");

        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new ParseLMS().execute(usermail, userpass);
    }

    private class ParseLMS extends AsyncTask<String,Integer,Document> {
        final String url = "https://lms.sehir.edu.tr/login/index.php";
        final String url2 = "https://lms.sehir.edu.tr/grade/report/overview/index.php";

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

                Elements grade = result.select("table[id=overview-grade]").select("tr[class~=^$]");
                for (Element g: grade) {
                    Elements lecture = g.select("td.cell.c0").select("a");
                    TextView title = new TextView(getApplicationContext());
                    title.setMaxLines(1);
                    title.setId(no+100100100);
                    title.setTextColor(Color.parseColor("#FDFDFD"));
                    title.setBackgroundResource(R.drawable.title_event);
                    title.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                    RelativeLayout.LayoutParams p_title = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

                    TextView type = new TextView(getApplicationContext());
                    type.setMaxLines(1);
                    type.setTextSize(15);
                    type.setId(no+200100100);
                    type.setTextColor(Color.parseColor("#FDFDFD"));
                    type.setGravity(Gravity.CENTER);
                    type.setBackgroundResource(R.drawable.body_type);
                    RelativeLayout.LayoutParams p_type = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    Elements average = g.select("td.cell.c1");
                    TextView body = new TextView(getApplicationContext());
                    body.setMaxLines(1);
                    body.setTextSize(15);
                    body.setId(no+300100100);
                    body.setTextColor(Color.parseColor("#001B25"));
                    body.setBackgroundResource(R.drawable.body_event);
                    RelativeLayout.LayoutParams p_body = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

                    ImageView check = new ImageView(getApplicationContext());
                    check.setId(no+400100100);
                    check.setBackground(null);
                    check.setImageResource(R.drawable.icon_go);
                    int size_param = Math.round(getResources().getDimension(R.dimen.size_param));
                    RelativeLayout.LayoutParams p_button = new RelativeLayout.LayoutParams(size_param, size_param);
                    p_button.addRule(RelativeLayout.BELOW, title.getId());
                    p_button.addRule(RelativeLayout.ALIGN_RIGHT, body.getId());
                    layout.addView(check, p_button);

                    if (no == 0) {
                        p_title.setMargins(40,40,40,0);
                        p_title.addRule(RelativeLayout.BELOW, back.getId());
                    } else {
                        p_title.setMargins(40,0,40,0);
                        p_title.addRule(RelativeLayout.BELOW, body.getId()-1);
                    }
                    if (lecture.text().replace("(16-3)", "").length() > 50) {
                        title.setText("\t" + lecture.text().replace("(16-3)", "").substring(0, 50) + "...");
                    } else {
                        title.setText("\t" + lecture.text().replace("(16-3)", ""));
                    }
                    layout.addView(title, p_title);

                    p_type.setMargins(40,0,0,40);
                    p_type.addRule(RelativeLayout.BELOW, title.getId());
                    p_type.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    type.setText("\tAverage:\t");
                    layout.addView(type, p_type);

                    p_body.setMargins(40,0,40,40);
                    p_body.addRule(RelativeLayout.BELOW, title.getId());
                    p_body.addRule(RelativeLayout.RIGHT_OF, type.getId());
                    body.setText("\t" + average.text());
                    layout.addView(body, p_body);

                    final String url2 = lecture.attr("href");
                    final Intent intent = new Intent(GradeActivity.this, Grade2Activity.class);
                    intent.putExtra("usermail", usermail);
                    intent.putExtra("userpass", userpass);
                    intent.putExtra("url2", url2);
                    title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });
                    type.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });
                    body.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });

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
