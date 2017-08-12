package com.myasiny.sehirian;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
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

public class File2Activity extends AppCompatActivity {
    ImageButton back;
    String usermail, userpass, url2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file2);

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
                int no = 0;
                RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);

                Elements link = result.select("a[class~=^$]");
                for (Element l: link) {
                    Elements name = l.select("span.instancename");
                    TextView title = new TextView(getApplicationContext());
                    title.setMaxLines(1);
                    title.setTextSize(15);
                    title.setId(no+100100100);
                    title.setTextColor(Color.parseColor("#FDFDFD"));
                    title.setBackgroundResource(R.drawable.title_file);
                    RelativeLayout.LayoutParams p_title = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

                    ImageView download = new ImageView(getApplicationContext());
                    download.setId(no+200100100);
                    download.setBackground(null);
                    download.setImageResource(R.drawable.icon_download);
                    int size_param = Math.round(getResources().getDimension(R.dimen.size_param));
                    RelativeLayout.LayoutParams p_button = new RelativeLayout.LayoutParams(size_param, size_param);
                    p_button.setMargins(0,0,40,0);
                    p_button.addRule(RelativeLayout.ALIGN_TOP, title.getId());
                    p_button.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    layout.addView(download, p_button);

                    p_title.addRule(RelativeLayout.LEFT_OF, download.getId());
                    if (no == 0) {
                        p_title.setMargins(40,40,10,40);
                        p_title.addRule(RelativeLayout.BELOW, back.getId());
                    } else {
                        p_title.setMargins(40,0,10,40);
                        p_title.addRule(RelativeLayout.BELOW, title.getId()-1);
                    }
                    if (name.text().length() > 40) {
                        title.setText("\t" + name.text().substring(0, 40) + "...");
                    } else {
                        title.setText("\t" + name.text());
                    }
                    layout.addView(title, p_title);

                    final String url2 = l.attr("href");
                    final Intent nthing = new Intent();
                    final Intent rdirect = new Intent(Intent.ACTION_VIEW, Uri.parse(url2));
                    final NotificationCompat.Builder notfBuild = new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle(getResources().getString(R.string.notf_forgot_title_2))
                            .setContentText(getResources().getString(R.string.notf_forgot_body))
                            .setSmallIcon(R.drawable.icon_logo_white)
                            .setAutoCancel(true);
                    final NotificationManager notfManage = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    PendingIntent pending = PendingIntent.getActivity(getApplicationContext(), 0, nthing, 0);
                    notfBuild.setContentIntent(pending);
                    title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            notfManage.notify(0, notfBuild.build());
                            startActivity(rdirect);
                        }
                    });
                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            notfManage.notify(0, notfBuild.build());
                            startActivity(rdirect);
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
