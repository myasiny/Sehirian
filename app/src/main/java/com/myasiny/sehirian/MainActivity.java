package com.myasiny.sehirian;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainActivity extends AppCompatActivity {
    Button login, quit;
    CheckBox remember;
    EditText mail, pass;
    ImageView logo;
    String usermail, userpass;
    SharedPreferences preference;
    SharedPreferences.Editor preference_edit;
    TextView name, forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = (ImageView) findViewById(R.id.logo);
        Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_down);
        anim.setDuration(1000);
        logo.startAnimation(anim);

        name = (TextView) findViewById(R.id.name);
        name.setTypeface(Typeface.createFromAsset(getAssets(), "Bestlovers.ttf"));

        mail = (EditText) findViewById(R.id.mail);
        mail.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        pass = (EditText) findViewById(R.id.pass);
        pass.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mail.getText().toString().contains("sehir.edu.tr")) {
                    mail.append("@std.sehir.edu.tr");
                }
            }
        });

        forgot = (TextView) findViewById(R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nthing = new Intent();
                Intent rdirect = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lms.sehir.edu.tr/login/forgot_password.php"));
                NotificationCompat.Builder notfBuild = new NotificationCompat.Builder(getApplicationContext())
                        .setContentTitle(getResources().getString(R.string.notf_forgot_title))
                        .setContentText(getResources().getString(R.string.notf_forgot_body))
                        .setSmallIcon(R.drawable.icon_logo_white)
                        .setAutoCancel(true);
                PendingIntent pending = PendingIntent.getActivity(getApplicationContext(), 0, nthing, 0);
                notfBuild.setContentIntent(pending);
                NotificationManager notfManage = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notfManage.notify(0, notfBuild.build());
                startActivity(rdirect);
            }
        });

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), R.string.popup_connecting, Toast.LENGTH_SHORT).show();
                if (mail.getText().toString().matches("") || pass.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), R.string.popup_missing, Toast.LENGTH_SHORT).show();
                } else {
                    if (!mail.getText().toString().contains("sehir.edu.tr")) {
                        usermail = mail.getText().toString() + "@std.sehir.edu.tr";
                    } else {
                        usermail = mail.getText().toString();
                    }
                    userpass = pass.getText().toString();

                    preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    preference_edit = preference.edit();

                    remember = (CheckBox) findViewById(R.id.remember);
                    if (remember.isChecked()) {
                        preference_edit.putString("usermail", usermail);
                        preference_edit.putString("userpass", userpass);
                        preference_edit.putBoolean("userinmemory", true);
                    }

                    if (!preference.getAll().containsKey("useraccepted")) {
                        AlertDialog.Builder popup = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        popup.setTitle(R.string.agree_title);
                        popup.setMessage(R.string.agree_body);
                        popup.setIcon(R.drawable.icon_logo_white);
                        popup.setPositiveButton(R.string.agree_accept, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                preference_edit.putBoolean("useraccepted", true);
                                preference_edit.commit();

                                new ParseLMS().execute(usermail, userpass);
                            }
                        });
                        popup.setNegativeButton(R.string.agree_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), R.string.popup_agree, Toast.LENGTH_SHORT).show();}
                        });
                        popup.show();
                    } else {
                        preference_edit.commit();

                        new ParseLMS().execute(usermail, userpass);
                    }
                }
            }
        });

        quit = (Button) findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
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
                String title = result.title();
                if (title.equals("Dashboard")) {
                    Toast.makeText(getApplicationContext(), R.string.popup_success, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    intent.putExtra("usermail", usermail);
                    intent.putExtra("userpass", userpass);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.popup_invalid, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.popup_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
