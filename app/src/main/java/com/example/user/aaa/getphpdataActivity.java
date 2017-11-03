package com.example.user.aaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class getphpdataActivity extends AppCompatActivity {
    private RequestQueue Queue,Queue1;
    private StringRequest getRequest,getRequest1;
    private Spinner spinner;
    public String mUrl;//getString(R.string.ip)
    public String mUrl1;//
    private Button GoToNext;
    private TextView Heart_tv;
    public String[] countstring;
    public String[] select;
    public ArrayAdapter<String> selectList;
    public Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getphpdata);

        init();

        Queue.add(getRequest);
        Queue1.add(getRequest1);
        GoToNext.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                Toast.makeText(getphpdataActivity.this,"送出", Toast.LENGTH_LONG).show();
                String g =spinner.getSelectedItem().toString();
                if(g.equals("傳生活照片")){

                    Intent intent = new Intent();
                    intent.setClass(getphpdataActivity.this,take_a_picture.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("count",countstring[1]);
                    Log.e("ccccc","耖"+countstring[1]);
                    intent.putExtras(bundle1);
                    startActivity(intent);

                }else if(g.equals("對他們說一些話")){
                    //
                    Intent intent = new Intent();
                    intent.setClass(getphpdataActivity.this,RecordActivity.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("data2",bundle.getString("data"));//將日期傳送給下一個Activity 做為檔案名稱
                    intent.putExtras(bundle2);
                    startActivity(intent);
                }
                else if(g.equals("傳YouTube")){

                    Intent intent = new Intent();
                    intent.setClass(getphpdataActivity.this,YouTu.class);
                    startActivity(intent);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {
                    //Toast.makeText(getphpdataActivity.this, "你選的是 : " + select[i], Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(getphpdataActivity.this,"請選擇正確選項", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    public void init(){
        bundle = this.getIntent().getExtras();
        spinner = (Spinner) findViewById(R.id.Menu);
        GoToNext = (Button) findViewById(R.id.To_RecordandUpload);
        Heart_tv = (TextView) findViewById(R.id.Heart_tv);
        Queue = Volley.newRequestQueue(this);
        Queue1 = Volley.newRequestQueue(this);
        select = new String[]{"你可以選擇", "傳生活照片", "對他們說一些話", "傳YouTube"};
        selectList = new ArrayAdapter<>(getphpdataActivity.this,android.R.layout.simple_spinner_dropdown_item, select);
        spinner.setAdapter(selectList);
        spinner.setSelection(0);
        mUrl1 = "http://" + getResources().getString(R.string.ip) + ":80/download.php";
        mUrl = "http://"+ getResources().getString(R.string.ip) +":80/mytext2.php";


        getRequest = new StringRequest(Request.Method.POST,mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String tv = bundle.getString("data") + "\n" +Heart_tv.getText() + s + "\t" + "\n";
                Heart_tv.setText(tv);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Heart_tv.setText("1");
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            Heart_tv.setText("2");
                        } else if (error instanceof ServerError) {
                            //TODO
                            Heart_tv.setText("3");
                        } else if (error instanceof NetworkError) {
                            //TODO
                            Heart_tv.setText("4");
                        } else if (error instanceof ParseError) {
                            //TODO
                            Heart_tv.setText("5");
                        }
                    }
                }){
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pw",bundle.getString("data"));//這邊input傳送給php的資料
                return params;
            }
        };

        getRequest1 = new StringRequest(Request.Method.POST,mUrl1, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                countstring = s.split("幹");
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }){
            public Map<String, String> getParams() throws AuthFailureError {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
                final Date curDate = new Date(System.currentTimeMillis());
                final String date = formatter.format(curDate);
                Map<String, String> params = new HashMap<String, String>();
                params.put("pw",date);
                return params;
            }
        };

    }
}