package com.myasiny.sehirian;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.InterstitialAd;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GpaActivity extends AppCompatActivity {
    AdView mAdView;
    Button button_course, button_calculate;
    Drawable icon_entry;
    EditText edit_comp_gpa, edit_comp_credit, edit_credit;
    ImageButton button_back;
//    InterstitialAd ad_interstitial;
    RelativeLayout layout;
    View center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa);

        button_back = (ImageButton) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("E5BDC074A0A9B6C1B1EB67A1B076A50B").build();
        mAdView.loadAd(adRequest);

//        ad_interstitial = new InterstitialAd(this);
//        ad_interstitial.setAdUnitId("ca-app-pub-5486013930231502/5819817176");
//        ad_interstitial.loadAd(new AdRequest.Builder().addTestDevice("E5BDC074A0A9B6C1B1EB67A1B076A50B").build());
//        ad_interstitial.setAdListener(new AdListener(){
//            public void onAdLoaded(){
//                ad_interstitial.show();
//            }
//        });

        final int dp_5 = Math.round(getApplicationContext().getResources().getDimension(R.dimen.dp_5));
        final int dp_10 = Math.round(getApplicationContext().getResources().getDimension(R.dimen.dp_10));
        final int dp_20 = Math.round(getApplicationContext().getResources().getDimension(R.dimen.dp_20));

        icon_entry = getApplicationContext().getResources().getDrawable(R.drawable.icon_entry);
        icon_entry.setBounds(0, 0, dp_20, dp_20);

        edit_comp_gpa = (EditText) findViewById(R.id.edit_comp_gpa);
        edit_comp_gpa.setCompoundDrawables(icon_entry, null, null, null);
        edit_comp_gpa.setCompoundDrawablePadding(dp_5);

        edit_comp_credit = (EditText) findViewById(R.id.edit_comp_credit);
        edit_comp_credit.setCompoundDrawables(icon_entry, null, null, null);
        edit_comp_credit.setCompoundDrawablePadding(dp_5);

        final int[] no = {2};

        layout = (RelativeLayout) findViewById(R.id.layout);
        center = (View) findViewById(R.id.center);

        edit_credit = (EditText) findViewById(R.id.edit_credit);
        edit_credit.setCompoundDrawables(icon_entry, null, null, null);
        edit_credit.setCompoundDrawablePadding(dp_5);

        button_course = (Button) findViewById(R.id.button_course);
        button_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView txt_title_new = new TextView(getApplicationContext());
                txt_title_new.setId(100100100 + no[0]);
                txt_title_new.setText(getString(R.string.hint_course) + "\t" + String.valueOf(no[0]));
                txt_title_new.setTextSize(10);
                txt_title_new.setTypeface(null, Typeface.BOLD);
                txt_title_new.setTextColor(Color.parseColor("#FDFDFD"));
                txt_title_new.setBackgroundResource(R.drawable.bg_body_g_d);
                txt_title_new.setPadding(dp_5, 0, 0, 0);
                txt_title_new.setGravity(Gravity.CENTER_VERTICAL);
                RelativeLayout.LayoutParams params_title_new = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );

                final EditText edit_credit_new = new EditText(getApplicationContext());
                edit_credit_new.setId(200100100 + no[0]);
                edit_credit_new.setHint(getString(R.string.hint_credit));
                edit_credit_new.setTextColor(Color.parseColor("#005050"));
                edit_credit_new.setHintTextColor(Color.parseColor("#196161"));
                edit_credit_new.setBackgroundResource(android.R.drawable.edit_text);
                edit_credit_new.setInputType(InputType.TYPE_CLASS_NUMBER);
                edit_credit_new.setCompoundDrawables(icon_entry, null, null, null);
                edit_credit_new.setCompoundDrawablePadding(dp_5);
                RelativeLayout.LayoutParams params_credit_new = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );

                final Spinner check_grade_new = new Spinner(getApplicationContext());
                check_grade_new.setId(300100100 + no[0]);
                check_grade_new.setPrompt(getString(R.string.array_prompt));
                ArrayAdapter<String> array_grades = new ArrayAdapter<>(GpaActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.array_grades));
                check_grade_new.setAdapter(array_grades);
                RelativeLayout.LayoutParams params_grade_new = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );

                if (no[0] == 2) {
                    params_title_new.addRule(RelativeLayout.BELOW, mAdView.getId());
                } else {
                    params_title_new.addRule(RelativeLayout.BELOW, edit_credit_new.getId() - 1);
                }
                params_title_new.setMargins(dp_10, 0, dp_10, 0);
                layout.addView(txt_title_new, params_title_new);

                params_credit_new.addRule(RelativeLayout.BELOW, txt_title_new.getId());
                params_credit_new.addRule(RelativeLayout.RIGHT_OF, center.getId());
                params_credit_new.setMargins(dp_10, dp_10, dp_10, dp_10);
                layout.addView(edit_credit_new, params_credit_new);

                params_grade_new.addRule(RelativeLayout.BELOW, txt_title_new.getId());
                params_grade_new.addRule(RelativeLayout.LEFT_OF, center.getId());
                params_grade_new.addRule(RelativeLayout.ALIGN_BOTTOM, edit_credit_new.getId());
                params_grade_new.setMargins(dp_10, dp_10, dp_10, 0);
                layout.addView(check_grade_new, params_grade_new);

                final ImageView icon_remove = new ImageView(getApplicationContext());
                icon_remove.setId(400100100 + no[0]);
                icon_remove.setImageResource(R.drawable.icon_fail);
                RelativeLayout.LayoutParams params_remove = new RelativeLayout.LayoutParams(dp_20, dp_20);
                params_remove.addRule(RelativeLayout.ALIGN_TOP, txt_title_new.getId());
                params_remove.addRule(RelativeLayout.ALIGN_RIGHT, txt_title_new.getId());
                params_remove.setMargins(0, dp_5, dp_5, 0);
                layout.addView(icon_remove, params_remove);

                icon_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txt_title_new.setVisibility(View.GONE);
                        edit_credit_new.setVisibility(View.GONE);
                        check_grade_new.setVisibility(View.GONE);
                        icon_remove.setVisibility(View.GONE);
                    }
                });

                no[0] = no[0] + 1;
            }
        });

        button_calculate = (Button) findViewById(R.id.button_calculate);
        button_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double comp_gpa = 0;
                double comp_credit = 0;

                int isEmpty = 0;
                ArrayList<Spinner> list_gpa = new ArrayList<>();
                ArrayList<EditText> list_credit = new ArrayList<>();
                for (int c = 0; c < layout.getChildCount(); c++) {
                    if (layout.getChildAt(c) instanceof Spinner && layout.getChildAt(c).getVisibility() == View.VISIBLE) {
                        Spinner selected_grade = (Spinner) layout.getChildAt(c);
                        list_gpa.add(selected_grade);
                    } else if (layout.getChildAt(c) instanceof EditText && layout.getChildAt(c).getVisibility() == View.VISIBLE) {
                        EditText selected_credit = (EditText) layout.getChildAt(c);
                        if (!TextUtils.isEmpty(selected_credit.getText().toString()) && selected_credit.getId() != edit_comp_gpa.getId() && selected_credit.getId() != edit_comp_credit.getId()) {
                            list_credit.add(selected_credit);
                        } else if (selected_credit.getId() == edit_comp_gpa.getId() || selected_credit.getId() == edit_comp_credit.getId()) {
                            if (!TextUtils.isEmpty(edit_comp_gpa.getText().toString()) && !TextUtils.isEmpty(edit_comp_credit.getText().toString())) {
                                comp_gpa = Double.valueOf(edit_comp_gpa.getText().toString());
                                comp_credit = Double.valueOf(edit_comp_credit.getText().toString());
                            } else if (!TextUtils.isEmpty(edit_comp_gpa.getText().toString()) && TextUtils.isEmpty(edit_comp_credit.getText().toString())) {
                                isEmpty += 1;
                            } else if (TextUtils.isEmpty(edit_comp_gpa.getText().toString()) && !TextUtils.isEmpty(edit_comp_credit.getText().toString())) {
                                isEmpty += 1;
                            }
                        } else {
                            isEmpty += 1;
                        }
                    }
                }

                AlertDialog.Builder popup = new AlertDialog.Builder(GpaActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                popup.setIcon(R.drawable.icon_logo);
                popup.setTitle(getString(R.string.app_name) + ":\t" + getString(R.string.popup_gpa));
                popup.setPositiveButton(R.string.button_okay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                });

                String result = getString(R.string.hint_warning);

                if (isEmpty == 0) {
                    double sum_gpa = 0;
                    double sum_credit = 0;

                    int isAt = 0;
                    for (Spinner s: list_gpa) {
                        double decimal;

                        if (s.getSelectedItem().toString().equals("A+")) {
                            decimal = 4.10;
                        } else if (s.getSelectedItem().toString().equals("A")) {
                            decimal = 4.00;
                        } else if (s.getSelectedItem().toString().equals("A-")) {
                            decimal = 3.70;
                        } else if (s.getSelectedItem().toString().equals("B+")) {
                            decimal = 3.30;
                        } else if (s.getSelectedItem().toString().equals("B")) {
                            decimal = 3.00;
                        } else if (s.getSelectedItem().toString().equals("B-")) {
                            decimal = 2.70;
                        } else if (s.getSelectedItem().toString().equals("C+")) {
                            decimal = 2.30;
                        } else if (s.getSelectedItem().toString().equals("C")) {
                            decimal = 2.00;
                        } else if (s.getSelectedItem().toString().equals("C-")) {
                            decimal = 1.70;
                        } else if (s.getSelectedItem().toString().equals("D+")) {
                            decimal = 1.30;
                        } else if (s.getSelectedItem().toString().equals("D")) {
                            decimal = 1.00;
                        } else if (s.getSelectedItem().toString().equals("D-")) {
                            decimal = 0.50;
                        } else {
                            decimal = 0.00;
                        }

                        EditText c = list_credit.get(isAt);
                        sum_gpa += decimal * Double.valueOf(c.getText().toString());
                        sum_credit += Double.valueOf(c.getText().toString());

                        isAt += 1;
                    }

                    double gpa = (sum_gpa + (comp_gpa * comp_credit)) / (sum_credit + comp_credit);
                    result = getString(R.string.hint_predict) + "\t" + String.valueOf(new DecimalFormat("#.##").format(gpa));
                }

                popup.setMessage(result);
                popup.show();
            }
        });
    }
}