package com.example.user.aaa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Measured extends AppCompatActivity {

    public Button btmeasured;
    public Button btchange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measured);
        init();

        btchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Measured.this, Messenger.class);
                startActivity(intent);
            }
        });

        btmeasured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Measured.this,Question.class);
                startActivity(intent);
            }
        });
    }
    public  void init(){
        btmeasured = (Button)findViewById(R.id.bt_measured);
        btchange = (Button)findViewById(R.id.bt_change);
    }
}
