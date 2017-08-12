package com.myasiny.sehirian;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class InboxActivity extends AppCompatActivity {
    ImageButton back;
    String usermail, userpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

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
        final String url2 = "https://lms.sehir.edu.tr/message/index.php";

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

                Elements id = result.select("div.popover-region.collapsed.popover-region-messages");
                final String id_to = id.attr("data-userid");

                Element inbox = result.select("div.contacts").first();
                Elements chat = inbox.select("div[data-region=contact]");
                for (Element c: chat) {
                    Elements from = c.select("div.name");
                    TextView title = new TextView(getApplicationContext());
                    title.setMaxLines(1);
                    title.setId(no+100100100);
                    title.setTextColor(Color.parseColor("#FDFDFD"));
                    title.setBackgroundResource(R.drawable.title_event);
                    title.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                    RelativeLayout.LayoutParams p_title = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

                    Elements message = c.select("span[data-region=last-message-text]");
                    TextView body = new TextView(getApplicationContext());
                    body.setMaxLines(1);
                    body.setTextSize(15);
                    body.setId(no+200100100);
                    body.setTextColor(Color.parseColor("#001B25"));
                    body.setBackgroundResource(R.drawable.body_event);
                    RelativeLayout.LayoutParams p_body = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

                    ImageView read = new ImageView(getApplicationContext());
                    read.setId(no+300100100);
                    read.setBackground(null);
                    read.setImageResource(R.drawable.icon_go);
                    int size_param = Math.round(getResources().getDimension(R.dimen.size_param));
                    RelativeLayout.LayoutParams p_button = new RelativeLayout.LayoutParams(size_param, size_param);
                    p_button.addRule(RelativeLayout.BELOW, title.getId());
                    p_button.addRule(RelativeLayout.ALIGN_RIGHT, body.getId());
                    layout.addView(read, p_button);

                    if (no == 0) {
                        p_title.setMargins(40,40,40,0);
                        p_title.addRule(RelativeLayout.BELOW, back.getId());
                    } else {
                        p_title.setMargins(40,0,40,0);
                        p_title.addRule(RelativeLayout.BELOW, body.getId()-1);
                    }
                    if (from.text().length() > 50) {
                        title.setText("\t" + from.text().substring(0, 50) + "...");
                    } else {
                        title.setText("\t" + from.text());
                    }
                    layout.addView(title, p_title);

                    p_body.setMargins(40,0,40,40);
                    p_body.addRule(RelativeLayout.BELOW, title.getId());
                    if (message.text().length() > 40) {
                        body.setText("\t" + message.text().substring(0, 40) + "...");
                    } else {
                        body.setText("\t" + message.text());
                    }
                    layout.addView(body, p_body);

                    final String id_from = c.attr("data-userid");
                    final String url2 = "https://lms.sehir.edu.tr/message/index.php?user=" + id_to + "&id=" + id_from;
                    final Intent intent = new Intent(InboxActivity.this, Inbox2Activity.class);
                    intent.putExtra("usermail", usermail);
                    intent.putExtra("userpass", userpass);
                    intent.putExtra("url2", url2);
                    title.setOnClickListener(new View.OnClickListener() {
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
