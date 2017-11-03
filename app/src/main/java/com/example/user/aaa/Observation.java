package com.example.user.aaa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Observation extends AppCompatActivity {

    private TextView Titletext ;
    private Button Go_To_NextBt,Close_App;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);
        init();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date NowDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
        String str = Titletext.getText().toString() + formatter.format(NowDate);
        Titletext.setText(str);

        Go_To_NextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Observation.this,StartReceiveActivity.class);
                startActivity(intent);
            }
        });

        Close_App.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void init(){
        setTitle("抓住你心情");
        Titletext = (TextView) findViewById(R.id.textView2);
        Go_To_NextBt = (Button) findViewById(R.id.go_to_look);
        Close_App = (Button) findViewById(R.id.finish_bt);
    }
}
