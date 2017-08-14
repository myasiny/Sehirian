package com.myasiny.sehirian;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Inbox2Activity extends AppCompatActivity {
    private AdView mAdView;
    ImageButton back;
    String usermail, userpass, url2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox2);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("E5BDC074A0A9B6C1B1EB67A1B076A50B").build();
        mAdView.loadAd(adRequest);

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
                res = Jsoup.connect(params[2]).cookies(res.cookies()).method(Connection.Method.GET).execute();
                doc = res.parse();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return doc;
        }

        @Override
        public void onPostExecute(Document result) {
            if (result != null) {
                RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);

                Elements chat = result.select("div.messages-area");

                final Elements from = chat.select("button[data-action=view-contact-profile]");
                TextView title = new TextView(getApplicationContext());
                title.setMaxLines(1);
                title.setId(1+100100100);
                title.setTextColor(Color.parseColor("#FDFDFD"));
                title.setBackgroundResource(R.drawable.title_event);
                title.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                RelativeLayout.LayoutParams p_title = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                p_title.setMargins(40,40,40,0);
                p_title.addRule(RelativeLayout.BELOW, back.getId());
                if (from.text().length() > 50) {
                    title.setText("\t" + from.text().substring(0, 50) + "...");
                } else {
                    title.setText("\t" + from.text());
                }
                layout.addView(title, p_title);

                final Element latest = chat.select("span[data-region=message-text]").last();
                TextView body = new TextView(getApplicationContext());
                body.setTextSize(15);
                body.setId(1+200100100);
                body.setTextColor(Color.parseColor("#001B25"));
                body.setGravity(Gravity.CENTER);
                body.setBackgroundResource(R.drawable.body_event);
                body.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()), 1.0f);
                RelativeLayout.LayoutParams p_body = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                p_body.setMargins(40,0,40,40);
                p_body.addRule(RelativeLayout.BELOW, title.getId());
                body.setText(latest.text());
                layout.addView(body, p_body);

                Button reply = new Button(getApplicationContext());
                reply.setId(1+300100100);
                reply.setTextColor(Color.parseColor("#FDFDFD"));
                reply.setBackgroundColor(Color.parseColor("#001B25"));
                RelativeLayout.LayoutParams p_reply = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                p_reply.setMargins(40,0,40,40);
                p_reply.addRule(RelativeLayout.BELOW, body.getId());
                reply.setText(R.string.button_reply);
                layout.addView(reply, p_reply);

                reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Reply via MySehir App");
                        intent.putExtra(Intent.EXTRA_TEXT, "Hi\t" + from.text() + ",\n\n...\n\n---\nSent from MySehir App in reply to:\n\n" + latest.text());
                        startActivity(intent);
                    }
                });

                ProgressBar spinner = (ProgressBar) findViewById(R.id.spinner);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(getApplicationContext(), R.string.popup_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
