package com.example.user.aaa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Question extends AppCompatActivity {

    TextView tv;
    RadioGroup rg1;
    RadioGroup rg2;
    RadioGroup rg3;
    RadioGroup rg4;
    RadioGroup rg5;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    RadioButton rb4;
    RadioButton rb5;
    RadioButton rb6;
    RadioButton rb7;
    RadioButton rb8;
    RadioButton rb9;
    RadioButton rb10;
    RadioButton rb11;
    RadioButton rb12;
    RadioButton rb13;
    RadioButton rb14;
    RadioButton rb15;
    RadioButton rb16;
    RadioButton rb17;
    RadioButton rb18;
    RadioButton rb19;
    RadioButton rb20;
    RadioButton rb21;
    RadioButton rb22;
    RadioButton rb23;
    RadioButton rb24;
    RadioButton rb25;
    Intent intent = new Intent();

    Button btcheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        init();

        btcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int re = 0, count = 0, text = 0;
                if (rb1.isChecked() == true) {
                    re = 0;
                    text++;
                    count += re;
                    intent.putExtra("失眠",String.valueOf(1));
                }
                if (rb6.isChecked() == true) {
                    re = 0;
                    text++;
                    count += re;
                    intent.putExtra("焦慮",String.valueOf(1));
                }
                if (rb11.isChecked() == true) {
                    re = 0;
                    text++;
                    count += re;
                    intent.putExtra("激動",String.valueOf(1));
                }
                if (rb16.isChecked() == true) {
                    re = 0;
                    text++;
                    count += re;
                    intent.putExtra("憂慮",String.valueOf(1));
                }
                if (rb21.isChecked() == true) {
                    re = 0;
                    text++;
                    count += re;

                }
                if (rb2.isChecked() == true) {
                    re = 1;
                    text++;
                    count += re;
                    intent.putExtra("失眠",String.valueOf(1));
                }
                if (rb7.isChecked() == true) {
                    re = 1;
                    text++;
                    count += re;
                    intent.putExtra("焦慮",String.valueOf(1));
                }
                if (rb12.isChecked() == true) {
                    re = 1;
                    text++;
                    count += re;
                    intent.putExtra("激動",String.valueOf(1));
                }
                if (rb17.isChecked() == true) {
                    re = 1;
                    text++;
                    count += re;
                    intent.putExtra("憂慮",String.valueOf(1));
                }
                if (rb22.isChecked() == true) {
                    re = 1;
                    text++;
                    count += re;
                }
                if (rb3.isChecked() == true) {
                    re = 2;
                    text++;
                    count += re;
                    intent.putExtra("失眠",String.valueOf(2));
                }
                if (rb8.isChecked() == true) {
                    re = 2;
                    text++;
                    count += re;
                    intent.putExtra("焦慮",String.valueOf(2));
                }
                if (rb13.isChecked() == true) {
                    re = 2;
                    text++;
                    count += re;
                    intent.putExtra("激動",String.valueOf(2));
                }
                if (rb18.isChecked() == true) {
                    re = 2;
                    text++;
                    count += re;
                    intent.putExtra("憂慮",String.valueOf(2));
                }
                if (rb23.isChecked() == true) {
                    re = 2;
                    text++;
                    count += re;
                }
                if (rb4.isChecked() == true) {
                    re = 3;
                    text++;
                    count += re;
                    intent.putExtra("失眠",String.valueOf(3));
                }
                if (rb9.isChecked() == true) {
                    re = 3;
                    text++;
                    count += re;
                    intent.putExtra("焦慮",String.valueOf(3));
                }
                if (rb14.isChecked() == true) {
                    re = 3;
                    text++;
                    count += re;
                    intent.putExtra("激動",String.valueOf(3));
                }
                if (rb19.isChecked() == true) {
                    re = 3;
                    text++;
                    count += re;
                    intent.putExtra("憂慮",String.valueOf(3));
                }
                if (rb24.isChecked() == true) {
                    re = 4;
                    text++;
                    count += re;
                }
                if (rb5.isChecked() == true) {
                    re = 4;
                    text++;
                    count += re;
                    intent.putExtra("失眠",String.valueOf(3));
                }
                if (rb10.isChecked() == true) {
                    re = 4;
                    text++;
                    count += re;
                    intent.putExtra("焦慮",String.valueOf(3));
                }
                if (rb15.isChecked() == true) {
                    re = 4;
                    text++;
                    count += re;
                    intent.putExtra("激動",String.valueOf(3));
                }
                if (rb20.isChecked() == true) {
                    re = 4;
                    text++;
                    count += re;
                    intent.putExtra("憂慮",String.valueOf(3));

                }
                if (rb25.isChecked() == true) {
                    re = 4;
                    text++;
                    count += re;
                }

                tv.setText(String.valueOf(count));

                if(text == 5) {

                    intent.setClass(Question.this, BluetoothAdapterDemoActivity.class);
                    intent.putExtra("count", String.valueOf(count));
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Question.this, "問卷還沒做完喔~", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void init(){
        tv = (TextView) findViewById(R.id.point);
        rg1 = (RadioGroup) findViewById(R.id.rg_1);
        rg2 = (RadioGroup) findViewById(R.id.rg_2);
        rg3 = (RadioGroup) findViewById(R.id.rg_3);
        rg4 = (RadioGroup) findViewById(R.id.rg_4);
        rg5 = (RadioGroup) findViewById(R.id.rg_5);
        rb1 = (RadioButton) findViewById(R.id.radioButton1);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);
        rb4 = (RadioButton) findViewById(R.id.radioButton4);
        rb5 = (RadioButton) findViewById(R.id.radioButton5);
        rb6 = (RadioButton) findViewById(R.id.radioButton6);
        rb7 = (RadioButton) findViewById(R.id.radioButton7);
        rb8 = (RadioButton) findViewById(R.id.radioButton8);
        rb9 = (RadioButton) findViewById(R.id.radioButton9);
        rb10 = (RadioButton) findViewById(R.id.radioButton10);
        rb11 = (RadioButton) findViewById(R.id.radioButton11);
        rb12 = (RadioButton) findViewById(R.id.radioButton12);
        rb13 = (RadioButton) findViewById(R.id.radioButton13);
        rb14 = (RadioButton) findViewById(R.id.radioButton14);
        rb15 = (RadioButton) findViewById(R.id.radioButton15);
        rb16 = (RadioButton) findViewById(R.id.radioButton16);
        rb17 = (RadioButton) findViewById(R.id.radioButton17);
        rb18 = (RadioButton) findViewById(R.id.radioButton18);
        rb19 = (RadioButton) findViewById(R.id.radioButton19);
        rb20 = (RadioButton) findViewById(R.id.radioButton20);
        rb21 = (RadioButton) findViewById(R.id.radioButton21);
        rb22 = (RadioButton) findViewById(R.id.radioButton22);
        rb23 = (RadioButton) findViewById(R.id.radioButton23);
        rb24 = (RadioButton) findViewById(R.id.radioButton24);
        rb25 = (RadioButton) findViewById(R.id.radioButton25);
        btcheck = (Button) findViewById(R.id.bt_check);
    }
}