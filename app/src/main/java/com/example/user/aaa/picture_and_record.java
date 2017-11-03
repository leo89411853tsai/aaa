package com.example.user.aaa;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class picture_and_record extends ListActivity {
    public String path =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/";
    private ImageView iv;
    private Bitmap bitmap = null;
    private static final String PATH = new String("./storage/emulated/0/Download/");
    private List<String> songs = new ArrayList<>();
    private MediaPlayer mp = new MediaPlayer();
    public List<String> list;
    public com.example.user.aaa.TestAdapter2 mAdapter;
    public ViewPager mViewPager;
    private ImageView[] imageViews;
    private ImageView imageView;
    private ViewGroup group;
    public Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_and_record);
        init();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        final Date curDate = new Date(System.currentTimeMillis());
        final String date = formatter.format(curDate);
        String scount = bundle.getString("count");//
        for(int c = 1 ;c <= Integer.parseInt(scount) ;c++) {
            File file = new File((path + date + "_" + c).toString()+".jpg");
            if(file.exists()) {
                mAdapter.change(getList(file.getPath()));
                mViewPager.setAdapter(mAdapter);
                initPointer();
                mViewPager.addOnPageChangeListener(new GuidePageChangeListener());
            }
        }
    }
    public void updateSongList() {
        File home = new File(PATH);
        if (home.listFiles( new getRecord.Mp3Filter()).length > 0) {
            for (File file : home.listFiles( new getRecord.Mp3Filter())) {
                songs.add(file.getName());
            }

            ArrayAdapter<String> songList = new ArrayAdapter<>(this, R.layout.song_item, songs);
            setListAdapter(songList);
        }
    }
    protected void onListItemClick(ListView l, View v, int position, long id) {
        try {
            mp.reset();
            mp.setDataSource(PATH + songs.get(position));
            mp.prepare();
            mp.start();
        } catch(IOException e) {
            Log.v(getString(R.string.app_name), e.getMessage());
        }
    }
    static class Mp3Filter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".amr"));
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
    public void init(){
        bundle = this.getIntent().getExtras();
        updateSongList();
        list = new ArrayList<String>();
        mViewPager = (ViewPager)findViewById(R.id.pager);
        group = (ViewGroup) findViewById(R.id.viewGroup);
        mAdapter = new TestAdapter2(this);

    }

}
class TestAdapter2 extends PagerAdapter {
    private List<String> mPaths;

    private Context mContext;

    public TestAdapter2(Context cx) {
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