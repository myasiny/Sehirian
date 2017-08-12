package com.myasiny.sehirian;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class CalculatorActivity extends AppCompatActivity {
    private AdView mAdView;
    Button add, calculate;
    EditText gpa_completed, credit_completed;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();

        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add = (Button) findViewById(R.id.add);
        calculate = (Button) findViewById(R.id.calculate);
        gpa_completed = (EditText) findViewById(R.id.gpa_completed);
        credit_completed = (EditText) findViewById(R.id.credit_completed);

        final int[] no = {1};
        final RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);

        TextView title = new TextView(getApplicationContext());
        title.setMaxLines(1);
        title.setId(no[0]+100100100);
        title.setTextColor(Color.parseColor("#FDFDFD"));
        title.setBackgroundResource(R.drawable.title_event);
        title.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
        RelativeLayout.LayoutParams p_title = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        p_title.setMargins(40,40,40,0);
        p_title.addRule(RelativeLayout.BELOW, add.getId());
        title.setText("\tCourse\t1");
        layout.addView(title, p_title);

        final EditText credit = new EditText(getApplicationContext());
        credit.setId(no[0]+200100100);
        credit.setTextColor(Color.parseColor("#001B25"));
        credit.setHintTextColor(Color.parseColor("#B1B1B1"));
        credit.setBackground(getDrawable(android.R.drawable.edit_text));
        credit.setFocusable(true);
        credit.setGravity(Gravity.CENTER);
        credit.setInputType(InputType.TYPE_CLASS_NUMBER);
        RelativeLayout.LayoutParams p_credit = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        p_credit.setMargins(0,30,40,40);
        p_credit.addRule(RelativeLayout.BELOW, title.getId());
        p_credit.addRule(RelativeLayout.ALIGN_START, calculate.getId());
        credit.setHint("Credits");
        layout.addView(credit, p_credit);

        Spinner grade = new Spinner(getApplicationContext());
        grade.setId(no[0]+300200100);
        RelativeLayout.LayoutParams p_grade = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        p_grade.setMargins(40,0,40,40);
        p_grade.addRule(RelativeLayout.BELOW, title.getId());
        p_grade.addRule(RelativeLayout.LEFT_OF, credit.getId());
        ArrayList<String> grades = new ArrayList<>();
        grades.add("Grade");
        grades.add("A+");
        grades.add("A");
        grades.add("A-");
        grades.add("B+");
        grades.add("B");
        grades.add("B-");
        grades.add("C+");
        grades.add("C");
        grades.add("C-");
        grades.add("D+");
        grades.add("D");
        grades.add("D-");
        grades.add("F");
        final ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(CalculatorActivity.this, android.R.layout.simple_spinner_dropdown_item, grades);
        grade.setAdapter(gradeAdapter);
        layout.addView(grade, p_grade);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView title_new = new TextView(getApplicationContext());
                title_new.setMaxLines(1);
                title_new.setId(no[0]+400100100);
                title_new.setTextColor(Color.parseColor("#FDFDFD"));
                title_new.setBackgroundResource(R.drawable.title_event);
                title_new.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                RelativeLayout.LayoutParams p_title_new = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);

                final EditText credit_new = new EditText(getApplicationContext());
                credit_new.setId(no[0]+400200100);
                credit_new.setTextColor(Color.parseColor("#001B25"));
                credit_new.setHintTextColor(Color.parseColor("#B1B1B1"));
                credit_new.setBackground(getDrawable(android.R.drawable.edit_text));
                credit_new.setFocusable(true);
                credit_new.setGravity(Gravity.CENTER);
                credit_new.setInputType(InputType.TYPE_CLASS_NUMBER);
                RelativeLayout.LayoutParams p_credit_new = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);

                final Spinner grade_new = new Spinner(getApplicationContext());
                grade_new.setId(no[0]+400300100);
                RelativeLayout.LayoutParams p_grade_new = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);

                p_title_new.setMargins(40,0,40,0);
                if (no[0] == 1) {
                    p_title_new.addRule(RelativeLayout.BELOW, credit.getId());
                } else {
                    p_title_new.addRule(RelativeLayout.BELOW, credit_new.getId()-1);
                }
                no[0] += 1;
                title_new.setText("\tCourse\t" + no[0]);
                layout.addView(title_new, p_title_new);

                p_credit_new.setMargins(0,30,40,40);
                p_credit_new.addRule(RelativeLayout.BELOW, title_new.getId());
                p_credit_new.addRule(RelativeLayout.ALIGN_START, calculate.getId());
                credit_new.setHint("Credits");
                layout.addView(credit_new, p_credit_new);

                p_grade_new.setMargins(40,0,40,40);
                p_grade_new.addRule(RelativeLayout.BELOW, title_new.getId());
                p_grade_new.addRule(RelativeLayout.LEFT_OF, credit_new.getId());
                grade_new.setAdapter(gradeAdapter);
                layout.addView(grade_new, p_grade_new);

                final ImageView remove = new ImageView(getApplicationContext());
                remove.setId(no[0]+400400100);
                remove.setBackground(null);
                remove.setImageResource(R.drawable.icon_remove);
                int size_param_small = Math.round(getResources().getDimension(R.dimen.size_param_small));
                RelativeLayout.LayoutParams p_remove = new RelativeLayout.LayoutParams(size_param_small, size_param_small);
                p_remove.setMargins(0,12,40,0);
                p_remove.addRule(RelativeLayout.ALIGN_TOP, title_new.getId());
                p_remove.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                layout.addView(remove, p_remove);

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        title_new.setVisibility(View.GONE);
                        credit_new.setVisibility(View.GONE);
                        grade_new.setVisibility(View.GONE);
                        remove.setVisibility(View.GONE);
                    }
                });
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result;

                int empty = 0;
                double comp_grade = 0;
                double comp_credit = 0;
                ArrayList<Spinner> l_grade = new ArrayList<>();
                ArrayList<EditText> l_credit = new ArrayList<>();
                for(int c = 0; c < layout.getChildCount(); c++) {
                    if (layout.getChildAt(c) instanceof Spinner && layout.getChildAt(c).getVisibility() == View.VISIBLE) {
                        Spinner selected = (Spinner) layout.getChildAt(c);
                        if (!selected.getSelectedItem().toString().equals("Grade")) {
                            l_grade.add(selected);
                        } else {
                            empty += 1;
                        }
                    } else if (layout.getChildAt(c) instanceof EditText && layout.getChildAt(c).getVisibility() == View.VISIBLE) {
                        EditText input = (EditText) layout.getChildAt(c);
                        if (!TextUtils.isEmpty(input.getText().toString()) && input.getId() != gpa_completed.getId() && input.getId() != credit_completed.getId()) {
                            l_credit.add(input);
                        } else if (input.getId() == gpa_completed.getId() || input.getId() == credit_completed.getId()) {
                            if (!TextUtils.isEmpty(gpa_completed.getText().toString()) && !TextUtils.isEmpty(credit_completed.getText().toString())) {
                                comp_grade = Double.valueOf(gpa_completed.getText().toString());
                                comp_credit = Double.valueOf(credit_completed.getText().toString());
                            } else if (!TextUtils.isEmpty(gpa_completed.getText().toString()) && TextUtils.isEmpty(credit_completed.getText().toString())) {
                                empty += 1;
                            } else if (TextUtils.isEmpty(gpa_completed.getText().toString()) && !TextUtils.isEmpty(credit_completed.getText().toString())) {
                                empty += 1;
                            }
                        } else {
                            empty += 1;
                        }
                    }
                }

                AlertDialog.Builder popup = new AlertDialog.Builder(CalculatorActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                popup.setTitle(R.string.popup_title);
                popup.setIcon(R.drawable.icon_logo_white);
                popup.setPositiveButton(R.string.popup_done, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                });

                if (empty == 0) {
                    int index = 0;
                    double sum_grade = 0;
                    double sum_credit = 0;
                    for (Spinner s: l_grade) {
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
                        EditText weight = l_credit.get(index);
                        sum_grade += decimal * Double.valueOf(weight.getText().toString());
                        sum_credit += Double.valueOf(weight.getText().toString());
                        index += 1;
                    }
                    double gpa = (sum_grade + (comp_grade * comp_credit)) / (sum_credit + comp_credit);
                    result = getResources().getString(R.string.popup_predict) + "\t" + String.valueOf(new DecimalFormat("#.##").format(gpa));
                    popup.setMessage(result);
                } else {
                    popup.setMessage(R.string.popup_warning);
                }
                popup.show();
            }
        });
    }
}
