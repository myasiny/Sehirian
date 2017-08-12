package com.myasiny.sehirian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class DashboardActivity extends AppCompatActivity {
    private AdView mAdView;
    Button events, inbox, grades, files, gpa;
    ImageButton logout, twit, tube, git;
    SharedPreferences preference;
    SharedPreferences.Editor preference_edit;
    String usermail, userpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        usermail = intent.getExtras().getString("usermail");
        userpass = intent.getExtras().getString("userpass");

        logout = (ImageButton) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), R.string.popup_logout, Toast.LENGTH_SHORT).show();

                preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                preference_edit = preference.edit();
                if (preference.getAll().containsKey("userinmemory")) {
                    preference_edit.clear();
                    preference_edit.putBoolean("useraccepted", true);
                    preference_edit.commit();
                }

                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        events = (Button) findViewById(R.id.events);
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EventActivity.class);
                intent.putExtra("usermail", usermail);
                intent.putExtra("userpass", userpass);
                startActivity(intent);
            }
        });

        inbox = (Button) findViewById(R.id.inbox);
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, InboxActivity.class);
                intent.putExtra("usermail", usermail);
                intent.putExtra("userpass", userpass);
                startActivity(intent);
            }
        });

        grades = (Button) findViewById(R.id.grades);
        grades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, GradeActivity.class);
                intent.putExtra("usermail", usermail);
                intent.putExtra("userpass", userpass);
                startActivity(intent);
            }
        });

        files = (Button) findViewById(R.id.files);
        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, FileActivity.class);
                intent.putExtra("usermail", usermail);
                intent.putExtra("userpass", userpass);
                startActivity(intent);
            }
        });

        gpa = (Button) findViewById(R.id.gpa);
        gpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CalculatorActivity.class);
                startActivity(intent);
            }
        });

        twit = (ImageButton) findViewById(R.id.twitter);
        twit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/myasiny13"));
                startActivity(intent);
            }
        });

        tube = (ImageButton) findViewById(R.id.youtube);
        tube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/channel/UChHZS2SEkcLgJehH8AHTUGA"));
                startActivity(intent);
            }
        });

        git = (ImageButton) findViewById(R.id.github);
        git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/myasiny"));
                startActivity(intent);
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
                TextView name = (TextView) findViewById(R.id.name);
                Elements n = result.select("h1");
                name.setText(n.text());

                ImageView pic = (ImageView) findViewById(R.id.pic);
                Element p = result.select("img").first();
                Picasso.with(DashboardActivity.this).load(p.attr("src")).fit().centerInside().placeholder(R.drawable.pic_avatar).into(pic);
            } else {
                Toast.makeText(getApplicationContext(), R.string.popup_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
