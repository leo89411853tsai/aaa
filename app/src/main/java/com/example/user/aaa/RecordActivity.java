package com.example.user.aaa;


import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordActivity extends AppCompatActivity {
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToWriteAccepted = false;
    private String[] permissions = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private Button recordButn;
    private Button stopButn;
    private Button Upload;
    public TextView tvtvv;
    private MediaRecorder mediaRecorder = null;
    final static private String APP_KEY = "3phrz1d07hmw22y";
    final static private String APP_SECRET = "xtpfwn9t8u7qize";
    private static final String ACCESSTOKEN = "tf9GQuzQKkAAAAAAAAAACZjNr_uHqfrbRRVl9ZtXFy0BK1_Miy4QHWzf9kOdEGsI";//允許訪問的金鑰
    static DropboxAPI<AndroidAuthSession> dropboxAPI;
    public String path =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/";
    boolean check = true;
    public String fileName = "";//設定錄音檔名
    public Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        int requestCode = 200;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,requestCode);
        }
        init();

        fileName = bundle.getString("data2");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());
        final String date = formatter.format(curDate);
        tvtvv.setText(path);

        recordButn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    File SDCardpath = Environment.getExternalStorageDirectory();
                    File myDataPath = new File( SDCardpath.getAbsolutePath() + "/Download" );
                    tvtvv.setText(path + SDCardpath.getAbsolutePath());

                    if( !myDataPath.exists() ) myDataPath.mkdirs();//如果檔案路徑不存在->建立一個
                    final String time = null;
                    File recodeFile = new File(SDCardpath.getAbsolutePath()+"/Download/" + date +".amr");//設置檔案存放路徑
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//設定音源
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//設定輸出檔案的格式(副檔名)
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//設定編碼格式
                    mediaRecorder.setOutputFile(recodeFile.getAbsolutePath());//設定錄音檔位置
                    mediaRecorder.prepare();//準備
                    mediaRecorder.start();//開始錄音囉~
               }
            catch (IOException e) {
                    e.printStackTrace();
                }
                check = false ;
            }
        });
        stopButn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mediaRecorder != null) {
                    mediaRecorder.stop();//暫停
                    mediaRecorder.release();// 發布
                    mediaRecorder = null;
                    check = true ;
                }
            }
        });

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadToDropboxFromPath(path + date +".amr", "dropboxtext/" + date+".amr");//上傳
            }
        });
    }
    public void init(){
        bundle = this.getIntent().getExtras();
        AndroidAuthSession session = buildSession();
        dropboxAPI = new DropboxAPI<AndroidAuthSession>(session);
        recordButn = (Button) findViewById(R.id.StartRecord);
        stopButn = (Button) findViewById(R.id.Stop_Record);
        Upload = (Button) findViewById(R.id.UploadFile);
        tvtvv = (TextView)findViewById(R.id.TVTVV);
    }
    private void UploadToDropboxFromPath (String uploadPathFrom, String uploadPathTo) {//手機上傳檔案到dropbox 的方法(手機檔案的位置，上傳到dropbox的位置(可新增目錄))
        Toast.makeText(getApplicationContext(), "上傳檔案中....", Toast.LENGTH_SHORT).show();
        final String uploadPathF = uploadPathFrom;
        final String uploadPathT = uploadPathTo;
        Thread th = new Thread(new Runnable() {
            public void run() {
                File tmpFile = null;
                try {
                    tmpFile = new File(uploadPathF);//由File方法 將檔案path 物件化
                }
                catch (Exception e) {e.printStackTrace();}
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(tmpFile);//做檔案位元輸入
                }
                catch (FileNotFoundException e) {e.printStackTrace();}
                try {
                    dropboxAPI.putFileOverwrite(uploadPathT, fis, tmpFile.length(), null);
                }
                catch (Exception e) {}
                getMain().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "成功上傳.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        th.start();
    }

    private AndroidAuthSession buildSession() {//把dropbox的app_key和 app_secret 實作成物件 並回傳
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        session.setOAuth2AccessToken(ACCESSTOKEN);
        return session;
    }

    public RecordActivity getMain() {//取得此Activity
        return this;
    }

    public void onRequestPermissionsResult(int requestCode , @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch(requestCode){
            case 200:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToWriteAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break ;
        }
        if(!permissionToRecordAccepted)RecordActivity.this.finish();
        if (!permissionToWriteAccepted)RecordActivity.this.finish();
    }

}
