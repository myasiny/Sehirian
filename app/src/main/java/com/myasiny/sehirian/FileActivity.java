package com.myasiny.sehirian;

import android.content.Intent;
import android.graphics.Color;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class FileActivity extends AppCompatActivity {
    private AdView mAdView;
    ImageButton back;
    String usermail, userpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("E5BDC074A0A9B6C1B1EB67A1B076A50B").build();
        mAdView.loadAd(adRequest);

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

                Elements course = result.select("div.col-lg-3");;
                for (Element c: course) {
                    Elements lecture = c.select("h4.h5").first().select("a");
                    TextView title = new TextView(getApplicationContext());
                    title.setMaxLines(1);
                    title.setTextSize(15);
                    title.setId(no+100100100);
                    title.setTextColor(Color.parseColor("#FDFDFD"));
                    title.setBackgroundResource(R.drawable.title_file);
                    RelativeLayout.LayoutParams p_title = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

                    ImageView go = new ImageView(getApplicationContext());
                    go.setId(no+200100100);
                    go.setBackground(null);
                    go.setImageResource(R.drawable.icon_go_dark);
                    int size_param = Math.round(getResources().getDimension(R.dimen.size_param));
                    RelativeLayout.LayoutParams p_button = new RelativeLayout.LayoutParams(size_param, size_param);
                    p_button.setMargins(0,0,40,0);
                    p_button.addRule(RelativeLayout.ALIGN_TOP, title.getId());
                    p_button.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    layout.addView(go, p_button);

                    p_title.addRule(RelativeLayout.LEFT_OF, go.getId());
                    if (no == 0) {
                        p_title.setMargins(40,40,0,40);
                        p_title.addRule(RelativeLayout.BELOW, back.getId());
                    } else {
                        p_title.setMargins(40,0,0,40);
                        p_title.addRule(RelativeLayout.BELOW, title.getId()-1);
                    }
                    if (lecture.text().replace("(16-3)", "").length() > 40) {
                        title.setText("\t" + lecture.text().replace("(16-3)", "").substring(0, 40) + "...");
                    } else {
                        title.setText("\t" + lecture.text().replace("(16-3)", ""));
                    }
                    layout.addView(title, p_title);

                    final String url2 = lecture.attr("href");
                    final Intent intent = new Intent(FileActivity.this, File2Activity.class);
                    intent.putExtra("usermail", usermail);
                    intent.putExtra("userpass", userpass);
                    intent.putExtra("url2", url2);
                    title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });
                    go.setOnClickListener(new View.OnClickListener() {
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
