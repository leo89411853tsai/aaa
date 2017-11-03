package com.example.user.aaa;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class take_a_picture extends AppCompatActivity {
    private RequestQueue Queue;
    private StringRequest getRequest;
    private String mUrl;//
    public List<String> list;
    public TestAdapter mAdapter;
    public ViewPager mViewPager;
    private ImageView[] imageViews;
    private ImageView imageView;
    private ViewGroup group;
    public int count,dcount;
    private Button Start_p_bt,OK_bt,back,fromalbm;
    private Boolean check = true;
    private Context context=this.getMain();
    public String path =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/";
    final static private String APP_KEY = "3phrz1d07hmw22y";
    final static private String APP_SECRET = "xtpfwn9t8u7qize";
    private static final String ACCESSTOKEN = "tf9GQuzQKkAAAAAAAAAACZjNr_uHqfrbRRVl9ZtXFy0BK1_Miy4QHWzf9kOdEGsI";//允許訪問的金鑰
    public Bundle bundle;
    static DropboxAPI<AndroidAuthSession> dropboxAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_a_picture);
        init();
        if(Integer.parseInt(bundle.getString("count"))==0) {
            count = 0;
        }
        else {
            String h = bundle.getString("count");
            dcount = Integer.parseInt(String.valueOf(h));
            count = dcount;
        }

        Start_p_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(it,MainActivity.DEFAULT_KEYS_DIALER);
                if(check){
                    Start_p_bt.setText("重新拍照");
                    check = false;
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Queue.add(getRequest);
                Intent intent = new Intent();
                intent.setClass(take_a_picture.this,getphpdataActivity.class);
                startActivity(intent);
                finish();
            }
        });

        fromalbm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/media/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 66);

            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if ((resultCode == RESULT_OK || requestCode==66 )&& data != null) {

            //當使用者按下確定後  when user click the button of OK
            final Uri uri = data.getData();//get path of pictrue

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
            final Date curDate = new Date(System.currentTimeMillis());
            final String date = formatter.format(curDate);


            File file = new File(Uri_to_Path(uri,null,requestCode));
            mAdapter.change(getList(file.getPath()));
            mViewPager.setAdapter(mAdapter);
            initPointer();
            mViewPager.addOnPageChangeListener(new GuidePageChangeListener());

            OK_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                UploadToDropboxFromPath(list.get(mViewPager.getCurrentItem()), "dropboxtext/"+date+"_" + count +".jpg");//照片上傳

            }
        });

        }
    }
    private void initPointer() {
        group.removeAllViews();
        imageViews = new ImageView[list.size()];

        for(int i = 0; i <imageViews.length; i++){
            imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
            //設置控制項的padding屬性
            imageView.setPadding(30, 0, 30, 0);
            imageViews[i] = imageView;
            if(i==0){
                //表示當前圖片
                imageViews[i].setBackgroundResource(R.drawable.test1);
            }else {

                imageViews[i].setBackgroundResource(R.drawable.test2);//page_indicator_unfocused
            }
            group.addView(imageViews[i]);
        }

    }
    public class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        public void onPageSelected(int position) {
            for (int i=0; i < imageViews.length; i++){
                imageViews[position].setBackgroundResource(R.drawable.test1);
                if (position != i){
                    imageViews[i].setBackgroundResource(R.drawable.test2);
                }
            }
        }
        public void onPageScrollStateChanged(int state) {
        }
    }
    private List<String> getList(String paths) {
        list.add(paths);
        return list;
    }

    private String Uri_to_Path(Uri uri, String selection,int c) {
        String path = null;
        if (c==66){
            String wholeID = DocumentsContract.getDocumentId(uri);
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA},MediaStore.Images.Media._ID + "=?", new String[]{wholeID.split(":")[1]}, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    Log.e("ccc耖","resultCode =  "+c);
                    path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }
                cursor.close();
            }
        }
        else{
            Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    Log.e("ccc幹","resultCode =  "+c);
                    path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }
                cursor.close();
            }
        }
        return path;
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

    public take_a_picture getMain() {//取得此Activity
        return this;
    }
    public void init(){
        bundle = this.getIntent().getExtras();
        Queue = Volley.newRequestQueue(this);
        fromalbm= (Button) findViewById(R.id.albm);
        list = new ArrayList<String>();
        mViewPager = (ViewPager)findViewById(R.id.pager);
        group = (ViewGroup) findViewById(R.id.viewGroup);
        mAdapter = new TestAdapter(this);

        Start_p_bt = (Button) findViewById(R.id.Start_picture_bt);
        OK_bt = (Button) findViewById(R.id.ok_bt);
        back = (Button) findViewById(R.id.go_back);
        AndroidAuthSession session = buildSession();
        dropboxAPI = new DropboxAPI<AndroidAuthSession>(session);
        mUrl = "http://" + getResources().getString(R.string.ip) + ":80/upload.php";

        getRequest = new StringRequest(Request.Method.POST,mUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
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
                params.put("date",date);
                params.put("value", String.valueOf(count-dcount));

                return params;
            }
        };
    }

}
class TestAdapter extends PagerAdapter {
    private List<String> mPaths;
    private Context mContext;
    public TestAdapter(Context cx) {
        mContext = cx.getApplicationContext();
    }
    public void change(List<String> paths) {
        mPaths = paths;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mPaths.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object obj) {
        // TODO Auto-generated method stub
        return view == (View) obj;
    }
    @Override
    public Object instantiateItem (ViewGroup container, int position) {
        ImageView iv = new ImageView(mContext);

        try {
            Bitmap bm = BitmapFactory.decodeFile(mPaths.get(position));//載入bitmap
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            iv.setImageBitmap(bm);
        }  catch (OutOfMemoryError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ((ViewPager)container).addView(iv, 0);
        return iv;
    }
    @Override
    public void destroyItem (ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

}
