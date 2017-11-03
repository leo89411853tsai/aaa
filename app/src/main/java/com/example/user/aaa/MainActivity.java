package com.example.user.aaa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public Button btmeasured;
    public Button btobservation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        btmeasured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Measured.class);
                startActivity(intent);
            }
        });

        btobservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Observation.class);
                startActivity(intent);
            }
        });
        Log.e("ccccccccccccc",getResources().getString(R.string.ip));
    }
    public void init(){
        btmeasured = (Button)findViewById(R.id.bt_Measured);
        btobservation = (Button)findViewById(R.id.bt_Observation);
    }
}
