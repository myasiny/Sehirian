package com.myasiny.sehirian;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class EventActivity extends AppCompatActivity {
    ImageButton back;
    String usermail, userpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

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

                Elements event1 = result.select("aside[data-block=calendar_upcoming]");
                Elements event2 = event1.select("div.event");
                for (Element e: event2) {
                    Element type = e.select("a").first();
                    Elements lecture = e.select("div.course").select("a");
                    TextView title = new TextView(getApplicationContext());
                    title.setMaxLines(1);
                    title.setId(no+100100100);
                    title.setTextColor(Color.parseColor("#FDFDFD"));
                    title.setBackgroundResource(R.drawable.title_event);
                    title.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                    RelativeLayout.LayoutParams p_title = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

                    Elements time = e.select("div.date");
                    TextView body = new TextView(getApplicationContext());
                    body.setMaxLines(1);
                    body.setTextSize(15);
                    body.setId(no+200100100);
                    body.setTextColor(Color.parseColor("#001B25"));
                    body.setBackgroundResource(R.drawable.body_event);
                    RelativeLayout.LayoutParams p_body = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

                    if (no == 0) {
                        p_title.setMargins(40,40,40,0);
                        p_title.addRule(RelativeLayout.BELOW, back.getId());
                    } else {
                        p_title.setMargins(40,0,40,0);
                        p_title.addRule(RelativeLayout.BELOW, body.getId()-1);
                    }
                    if (type.text().length() + lecture.text().length() > 50) {
                        title.setText("\t" + lecture.text().substring(0, 50-type.text().length()) + "..." + "\t-\t" + type.text());
                    } else {
                        title.setText("\t" + lecture.text() + "\t-\t" + type.text());
                    }
                    layout.addView(title, p_title);

                    p_body.setMargins(40,0,40,40);
                    p_body.addRule(RelativeLayout.BELOW, title.getId());
                    if (time.text().length() > 40) {
                        body.setText("\t" + time.text().substring(0, 40) + "...");
                    } else {
                        body.setText("\t" + time.text());
                    }
                    layout.addView(body, p_body);

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
