package com.example.user.aaa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.session.AppKeyPair;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Messenger extends AppCompatActivity {
    private RequestQueue Queue;
    private StringRequest getRequest;
    private  String mUrl;//
    final static private String APP_KEY = "3phrz1d07hmw22y";
    final static private String APP_SECRET = "xtpfwn9t8u7qize";
    private static final String ACCESSTOKEN = "tf9GQuzQKkAAAAAAAAAACZjNr_uHqfrbRRVl9ZtXFy0BK1_Miy4QHWzf9kOdEGsI";//允許訪問的金鑰
    static DropboxAPI<AndroidAuthSession> dropboxAPI;
    public static String DropboxDownloadPathTo = "";
    public static String DropboxDownloadPathFrom = "";
    public String path =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/";
    Button btmeasured,gotonext;
    private Spinner spinner;
    public int count;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
    Date curDate = new Date(System.currentTimeMillis());
    final String date = formatter.format(curDate);
    public String[] select;
    public ArrayAdapter<String> selectList;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        init();
        Queue.add(getRequest);
//        AlertDialog.Builder dialog = new AlertDialog.Builder(Messenger.this);
        //


//        if (cheak1 == 0){
//            dialog.setTitle("真可惜呢~");
//            dialog.setMessage("目前還沒有收到新訊息唷~");
//            dialog.setNegativeButton("稍後再看!",new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface arg0, int arg1) {
//
//                }
//            });
//            dialog.show();
//        }
//        else if (cheak1 == 1){
//            cheak1 = 2 ;
//            dialog.setTitle("驚喜");
//            dialog.setMessage("您的家人對您說了些話唷~");
//            dialog.setNegativeButton("點開來看!",new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface arg0, int arg1) {
//
//                    Intent intent = new Intent(Messenger.this,getRecord.class);
//                    startActivity(intent);
//                }
//            });
//            dialog.show();
//        }
        gotonext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Messenger.this,"送出", Toast.LENGTH_LONG).show();
                String g =spinner.getSelectedItem().toString();
                if (g.equals("看影片")){
                    Intent intent = new Intent(Messenger.this,YouTubePlay.class);
                    startActivity(intent);
                }
                if (g.equals("看圖片")){
                    Intent intent = new Intent(Messenger.this,Watch_picture.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("count", String.valueOf(count));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                if (g.equals("聽音檔")){
                    Intent intent = new Intent(Messenger.this,getRecord.class);
                    startActivity(intent);
                }
                if (g.equals("聽音檔和看圖片")){
                    Intent intent = new Intent(Messenger.this,picture_and_record.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("count", String.valueOf(count));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        btmeasured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Messenger.this,Question.class);
                startActivity(intent);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0)
                    Toast.makeText(Messenger.this, "你選的是 : " + select[i], Toast.LENGTH_SHORT).show();
                else{
                    Toast.makeText(Messenger.this,"請選擇正確選項", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    private void DownloadFromDropboxFromPath (String downloadPathTo, String downloadPathFrom) {//從dropbox下載檔案的方法
        DropboxDownloadPathTo = downloadPathTo;
        DropboxDownloadPathFrom = downloadPathFrom;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "正在下載 ...", Toast.LENGTH_SHORT).show();
                Thread th = new Thread(new Runnable() {
                    public void run() {
                        File file = new File(DropboxDownloadPathTo + DropboxDownloadPathFrom.substring(DropboxDownloadPathFrom.lastIndexOf('.')));
                        if (file.exists()) file.delete();
                        try {
                            FileOutputStream outputStream = new FileOutputStream(file);
                            dropboxAPI.getFile(DropboxDownloadPathFrom,null,outputStream,null);
                            getMain().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "下載完成.", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }catch (DropboxServerException e){
                            e.printStackTrace();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                th.start();
            }
        });
    }
    private AndroidAuthSession buildSession() {//把dropbox的app_key和 app_secret 實作成物件 並回傳
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        session.setOAuth2AccessToken(ACCESSTOKEN);
        return session;
    }
    public Messenger getMain() {//取得此Activity
        return this;
    }
    public void init(){
        mUrl = "http://" + getResources().getString(R.string.ip) + ":80/download.php";
        btmeasured = (Button)findViewById(R.id.bt_measured);
        spinner = (Spinner) findViewById(R.id.Menu);
        gotonext = (Button) findViewById(R.id.gotonext);
        tv = (TextView) findViewById(R.id.testtv);
        AndroidAuthSession session = buildSession();
        dropboxAPI = new DropboxAPI<AndroidAuthSession>(session);

        select = new String[]{"你可以選擇", "看影片", "看圖片", "聽音檔", "聽音檔和看圖片"};
        selectList = new ArrayAdapter<>(Messenger.this,android.R.layout.simple_spinner_dropdown_item, select);
        spinner.setAdapter(selectList);
        spinner.setSelection(0);


        DownloadFromDropboxFromPath(path + "fromdropbox_record","dropboxtext/" + date +".amr");

        getRequest = new StringRequest(Request.Method.POST,mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //
                count = Integer.parseInt(s.split("幹")[1].trim());
                tv.setText(s);
                for (int c = 0 ;c <= count ; c++) {
                    String ss = "dropboxtext/" + date + "_" + String.valueOf(c) + ".jpg";
                    String ss1 = path + date + "_" + String.valueOf(c);
                    DownloadFromDropboxFromPath(ss1, ss);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("111111111","cccccc"+volleyError);
                    }
                }){
            public Map<String, String> getParams() throws AuthFailureError {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
                final Date curDate = new Date(System.currentTimeMillis());
                String date = formatter.format(curDate);

                Map<String, String> params = new HashMap<String, String>();
                params.put("pw",date);
                return params;
            }
        };
        Queue = Volley.newRequestQueue(this);
    }
}
