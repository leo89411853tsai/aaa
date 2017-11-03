package com.example.user.aaa;

import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class getRecord extends ListActivity {
    private static final String PATH = new String("./storage/emulated/0/Download/");
    private List<String> songs = new ArrayList<>();
    private MediaPlayer mp = new MediaPlayer();
    private Button back;
    @Override
    protected void onCreate(Bundle icicle) {

        try {
            super.onCreate(icicle);
            setContentView(R.layout.getrecord);
            init();
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getRecord.this,Messenger.class);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public void updateSongList() {
        File home = new File(PATH);
        if (home.listFiles( new Mp3Filter()).length > 0) {
            for (File file : home.listFiles( new Mp3Filter())) {
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
    public void init(){
        updateSongList();
        back = (Button) findViewById(R.id.back);
    }
}

