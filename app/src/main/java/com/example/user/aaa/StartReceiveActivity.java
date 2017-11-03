package com.example.user.aaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartReceiveActivity extends AppCompatActivity {
    private EditText ed_year,ed_m,ed_date;
    private Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_receive);
        init();
        final Bundle bundle = new Bundle();
        final Intent intent = new Intent();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(StartReceiveActivity.this , ed_year.getText().toString() + "/" + ed_date.getText().toString() + "/" + ed_m.getText().toString() , Toast.LENGTH_SHORT).show();
                intent.setClass(StartReceiveActivity.this,getphpdataActivity.class);
                bundle.putString("data",ed_year.getText().toString() + "年" + ed_m.getText().toString() + "月" + ed_date.getText().toString() +"日" );
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    public void init(){
        ed_year = (EditText) findViewById(R.id.year_ed);
        ed_m = (EditText) findViewById(R.id.m_ed);
        ed_date = (EditText) findViewById(R.id.date_ed);
        bt = (Button) findViewById(R.id.go_inquiry);
    }
}
