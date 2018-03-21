package com.myasiny.sehirian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.InterstitialAd;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class LoginActivity extends AppCompatActivity {
    Button button_login;
    CheckBox check_remember;
    Drawable icon_mail, icon_pasw, icon_reset;
    EditText edit_mail, edit_pasw;
    ImageView login_success, login_fail, login_connection;
//    InterstitialAd ad_interstitial;
    ProgressBar login_wait;
    SharedPreferences.Editor preference;
    String usermail, userpasw;
    TextView txt_title, check_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        ad_interstitial = new InterstitialAd(this);
//        ad_interstitial.setAdUnitId("ca-app-pub-5486013930231502/3037879818");
//        ad_interstitial.loadAd(new AdRequest.Builder().addTestDevice("E5BDC074A0A9B6C1B1EB67A1B076A50B").build());
//        ad_interstitial.setAdListener(new AdListener(){
//            public void onAdLoaded(){
//                ad_interstitial.show();
//            }
//        });

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setTypeface(Typeface.createFromAsset(getAssets(), "Bestlovers.ttf"));

        int dp_5 = Math.round(getApplicationContext().getResources().getDimension(R.dimen.dp_5));
        int dp_20 = Math.round(getApplicationContext().getResources().getDimension(R.dimen.dp_20));

        icon_mail = getApplicationContext().getResources().getDrawable(R.drawable.icon_mail);
        icon_mail.setBounds(0, 0, dp_20, dp_20);
        edit_mail = (EditText) findViewById(R.id.edit_mail);
        edit_mail.setCompoundDrawables(icon_mail, null, null, null);
        edit_mail.setCompoundDrawablePadding(dp_5);

        icon_pasw = getApplicationContext().getResources().getDrawable(R.drawable.icon_pasw);
        icon_pasw.setBounds(0, 0, dp_20, dp_20);
        edit_pasw = (EditText) findViewById(R.id.edit_pasw);
        edit_pasw.setCompoundDrawables(icon_pasw, null, null, null);
        edit_pasw.setCompoundDrawablePadding(dp_5);

        icon_reset = getApplicationContext().getResources().getDrawable(R.drawable.icon_reset);
        icon_reset.setBounds(0, 0, dp_20, dp_20);
        check_reset = (TextView) findViewById(R.id.check_reset);
        check_reset.setCompoundDrawables(null, null, icon_reset, null);
        check_reset.setCompoundDrawablePadding(dp_5);
        check_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lms.sehir.edu.tr/login/forgot_password.php"));
                startActivity(intent);
            }
        });

        preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        button_login = (Button) findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_wait = (ProgressBar) findViewById(R.id.login_wait);
                login_wait.setVisibility(View.VISIBLE);

                login_success = (ImageView) findViewById(R.id.login_success);
                login_success.setVisibility(View.GONE);

                login_fail = (ImageView) findViewById(R.id.login_fail);
                login_fail.setVisibility(View.GONE);

                login_connection = (ImageView) findViewById(R.id.login_connection);
                login_connection.setVisibility(View.GONE);

                if (!edit_mail.getText().toString().matches("") && !edit_pasw.getText().toString().matches("")) {
                    if (!edit_mail.getText().toString().contains("@")) {
                        edit_mail.setText(edit_mail.getText().toString() + "@std.sehir.edu.tr");
                    }

                    usermail = edit_mail.getText().toString();
                    userpasw = edit_pasw.getText().toString();

                    check_remember = (CheckBox) findViewById(R.id.check_remember);
                    if (check_remember.isChecked()) {
                        preference.putBoolean("isChecked", true);
                    }

                    new ParseLMS().execute(usermail, userpasw);
                } else {
                    login_wait.setVisibility(View.GONE);
                    login_fail.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
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
                doc = res.parse();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return doc;
        }

        @Override
        public void onPostExecute(Document result) {
            if (result != null) {
                String title = result.title();
                if (title.equals("Dashboard") || title.equals("Kontrol paneli")) {
                    login_wait.setVisibility(View.GONE);
                    login_success.setVisibility(View.VISIBLE);

                    preference.putString("usermail", usermail);
                    preference.putString("userpasw", userpasw);
                    preference.putString("username", result.select("h1").text());
                    preference.putString("userpict", result.select("img").first().attr("src"));
                    preference.commit();

                    Intent intent = new Intent(LoginActivity.this, TimelineActivity.class);
                    startActivity(intent);
                } else {
                    login_wait.setVisibility(View.GONE);
                    login_fail.setVisibility(View.VISIBLE);
                }
            } else {
                login_wait.setVisibility(View.GONE);
                login_connection.setVisibility(View.VISIBLE);
            }
        }
    }
}