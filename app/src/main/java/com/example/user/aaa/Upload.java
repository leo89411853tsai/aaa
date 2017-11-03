package com.example.user.aaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Upload extends AppCompatActivity {

    TextView tva,tvb,tvc,tvd,tve,tvf,tvg,tvh;
    Button btok,go_back;
    private RequestQueue Queue;
    private StringRequest getRequest;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        init();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());
        final String date = formatter.format(curDate);
        Intent intent = this.getIntent();

        final String attention = intent.getStringExtra("a");
        final String miditation = intent.getStringExtra("b");
        final String alpha = intent.getStringExtra("c");
        final String beta = intent.getStringExtra("d");
        final String gamma = intent.getStringExtra("e");
        final String delta = intent.getStringExtra("f");
        final String theta = intent.getStringExtra("g");
        final String count = intent.getStringExtra("h");
        final String insomnia = intent.getStringExtra("失眠");
        final String anxiety = intent.getStringExtra("焦慮");
        final String excitement = intent.getStringExtra("激動");
        final String concern = intent.getStringExtra("憂慮");


        int at = 0,mi = 0;
        int attentionn = Integer.parseInt(attention);
        int miditationn = Integer.parseInt(miditation);
        int insomniaa = Integer.parseInt(insomnia);
        int anxietyy = Integer.parseInt(anxiety);
        int excitementt = Integer.parseInt(excitement);
        int concernn = Integer.parseInt(concern);

        if(attentionn >= 40){
            at = 1;
        }
        if(attentionn > 20 && attentionn < 40){
            at = 2;
        }
        if(attentionn > 1 && attentionn <= 20){
            at = 3;
        }
        if(miditationn >= 40){
            mi = 1;
        }
        if(miditationn > 20 && miditationn < 40){
            mi = 2;
        }
        if(miditationn > 1 && miditationn <= 20){
            mi = 3;
        }
        if (anxietyy == 1 && anxietyy == at && anxietyy == mi){
            tva.setText("沒有焦慮");
        }
            else if (anxietyy == 2 && anxietyy == at && anxietyy == mi){
                tva.setText("輕微焦慮");
            }
            else if (anxietyy == 3 && anxietyy == at && anxietyy == mi){
                tva.setText("嚴重焦慮");
            }
        else if (anxietyy != at || anxietyy != mi){
            int anx = (anxietyy + at + mi) / 3;
            if(anx == 1){
                tva.setText("沒有焦慮");
            }
            if(anx == 2){
                tva.setText("輕微焦慮");
            }
            if(anx == 3){
                tva.setText("嚴重焦慮");
            }
            }
        if (excitementt == 1 && excitementt == mi){
            tvb.setText("正常");
        }
            else if (excitementt == 2 && excitementt == mi){
                tvb.setText("輕微激動");
            }
            else if (excitementt == 3 && excitementt == mi){
                tvb.setText("嚴重激動");
            }
        else if (excitementt != mi){
            int exc = (excitementt + mi) / 2;
            if(exc == 1){
                tvb.setText("正常");
            }
            if(exc == 2){
                tvb.setText("輕微激動");
            }
            if(exc == 3){
                tvb.setText("嚴重激動");
            }
        }
        if (concernn == 1 && concernn == at && concernn == mi){
            tvc.setText("沒有憂慮");
        }
            else if (concernn == 2 && concernn == at && concernn == mi){
                tvc.setText("輕微憂慮");
            }
            else if (concernn == 3 && concernn == at && concernn == mi){
                tvc.setText("嚴重憂慮");
            }
        else if (concernn != at || concernn != mi){
            int con = (concernn + at + mi) / 3;
            if(con == 1){
                tvc.setText("沒有憂慮");
            }
            if(con == 2){
                tvc.setText("輕微憂慮");
            }
            if(con == 3){
                tvc.setText("嚴重憂慮");
            }
        }
        if(at == 1 && mi == 2 || mi == 3){
            tvd.setText("注意力不集中");
        }
        tve.setText(attention);
        tvf.setText(miditation);
        //tvf.setText(date);

        getRequest = new StringRequest(Request.Method.POST,mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }

                }){
            public Map<String, String> getParams()
                    throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                int mood = Integer.parseInt(count);
                params.put("date",date);
                params.put("attention",attention);
                params.put("miditation",miditation);
                params.put("theta",theta);
                params.put("delta",delta);
                params.put("alpha",alpha);
                params.put("beta",beta);
                params.put("gamma",gamma);
                params.put("count",count);

                if(mood <= 5) {
                    params.put("mood","正常");
                }
                if(mood >=6 && mood<=9) {
                    params.put("mood","輕度情緒困擾");
                }
                if(mood >= 10 && mood <= 14) {
                    params.put("mood","中度情緒困擾");
                }
                if(mood >= 15) {
                    params.put("mood","重度情緒困擾");
                }
                return params;
            }
        };
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Upload.this,Measured.class);
                startActivity(intent1);
            }
        });
        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Queue.add(getRequest);
                Toast.makeText(Upload.this,"資料上傳完畢",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void init(){
        mUrl = "http://" + getResources().getString(R.string.ip) + "/mytext.php";
        tva = (TextView)findViewById(R.id.tva);
        tvb = (TextView)findViewById(R.id.tvb);
        tvc = (TextView)findViewById(R.id.tvc);
        tvd = (TextView)findViewById(R.id.tvd);
        tve = (TextView)findViewById(R.id.tve);
        tvf = (TextView)findViewById(R.id.tvf);
        tvg = (TextView)findViewById(R.id.tvg);
        tvh = (TextView)findViewById(R.id.tvh);
        btok = (Button)findViewById(R.id.bt_ok);
        go_back = (Button) findViewById(R.id.go_back);
        Queue = Volley.newRequestQueue(this);
    }

}
