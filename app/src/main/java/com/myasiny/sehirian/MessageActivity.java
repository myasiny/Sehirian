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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MessageActivity extends AppCompatActivity {
    AdView mAdView;
    ImageButton button_back;
    SharedPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

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
                final TextView txt_from = (TextView) findViewById(R.id.txt_from);
                txt_from.setText(result.select("div.messages-area").select("button[data-action=view-contact-profile]").text());

                final TextView txt_content = (TextView) findViewById(R.id.txt_content);
                txt_content.setText(result.select("div.messages-area").select("span[data-region=message-text]").last().text());

                Button button_reply = (Button) findViewById(R.id.button_reply);
                button_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Reply via SEHIRIAN");
                        intent.putExtra(Intent.EXTRA_TEXT, "Hi\t" + txt_from.getText().toString() + ",\n\n\n---\tSent from SEHIRIAN in reply to:\n" + txt_content.getText().toString());
                        startActivity(intent);
                    }
                });

                ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
                progress.setVisibility(View.GONE);
            } else {
                Toast.makeText(getApplicationContext(), R.string.hint_error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
