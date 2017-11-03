package com.example.user.aaa;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Watch_picture extends AppCompatActivity {
    public List<String> list;
    public com.example.user.aaa.TestAdapter1 mAdapter;
    public ViewPager mViewPager;
    private ImageView[] imageViews;
    private ImageView imageView;
    private ViewGroup group;
    public String path =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/";
    private Button ref_bt;
    TextView testtv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_picture);
        init();
        final Bundle bundle = this.getIntent().getExtras();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        final Date curDate = new Date(System.currentTimeMillis());
        final String date = formatter.format(curDate);

        ref_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        String scount = bundle.getString("count");//
        for(int c = 1 ;c <= Integer.parseInt(scount) ;c++) {
            File file = new File((path + date + "_" + c).toString()+".jpg");
            if(file.exists()) {
                testtv.setText((path + date + "_" + c).toString());
                mAdapter.change(getList(file.getPath()));
                mViewPager.setAdapter(mAdapter);
                initPointer();
                mViewPager.addOnPageChangeListener(new GuidePageChangeListener());
            }
        }
    }
    private List<String> getList(String paths) {
        list.add(paths);
        return list;
    }
    private String Uri_to_Path(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
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
    public void init(){
        testtv = (TextView) findViewById(R.id.test_picture_tv);
        list = new ArrayList<String>();
        mViewPager = (ViewPager)findViewById(R.id.pager);
        group = (ViewGroup) findViewById(R.id.viewGroup);
        mAdapter = new TestAdapter1(this);
        ref_bt = (Button) findViewById(R.id.Refresh_bt);
    }

}
class TestAdapter1 extends PagerAdapter {
    private List<String> mPaths;

    private Context mContext;

    public TestAdapter1(Context cx) {
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
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth() , bm.getHeight(), matrix, true);//
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
