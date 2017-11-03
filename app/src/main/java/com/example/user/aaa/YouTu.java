package com.example.user.aaa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class YouTu extends AppCompatActivity {

    EditText movie,album;
    Button yutu;
    private RequestQueue Queue;
    private StringRequest getRequest;
    private String mUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tu);
        init();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());
        final String date = formatter.format(curDate);

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
                String mo = movie.getText().toString();
                params.put("date",date);
                if(!mo.equals("影片")) {
                        params.put("movie", mo);
                }
                /*if(!al.equals("影集")) {
                    String[] afteral = al.split("list=");
                    for (int i = 0; i < afteral.length; i++) {
                        params.put("album", afteral[i]);
                    }
                }*/
                return params;
            }
        };

        yutu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Queue.add(getRequest);
                Toast.makeText(YouTu.this,"資料上傳完畢",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void init(){
        mUrl = "http://" + getResources().getString(R.string.ip) +":80/upyoutu.php";
        movie = (EditText)findViewById(R.id.et_movie);
        album = (EditText)findViewById(R.id.et_album);
        yutu = (Button)findViewById(R.id.bt_yutu);
        Queue = Volley.newRequestQueue(this);
    }
}
