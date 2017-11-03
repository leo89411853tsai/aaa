package com.example.user.aaa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class YouTubePlay extends YouTubeBaseActivity
        implements com.google.android.youtube.player.YouTubePlayer.OnInitializedListener {

    public static final String DEVELOPER_KEY = "AIzaSyCe0DBQCl7enn6RwPvsG2VwGsp9kzrL3Eg";
    private static String VIDEO_ID = "";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private RequestQueue Queue;
    private StringRequest getRequest;
    private String mUrl;

    YouTubePlayerFragment myYouTubePlayerFragment;
    TextView tvyu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_player);
        init();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());
        final String date = formatter.format(curDate);
        getRequest = new StringRequest(Request.Method.POST,mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String m) {
                        String[] aftermo = m.split("https://youtu.be/");
                        VIDEO_ID = aftermo[1].trim();
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
                params.put("pw",date);
                return params;
            }
        };
        Queue.add(getRequest);
        myYouTubePlayerFragment = (YouTubePlayerFragment)getFragmentManager().findFragmentById(R.id.youtubeplayerfragment);
        myYouTubePlayerFragment.initialize(DEVELOPER_KEY, this);
    }
    public void onInitializationFailure(com.google.android.youtube.player.YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason){

        if(errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this,RECOVERY_DIALOG_REQUEST).show();
        }
        else {
            String errorMessage = String.format("There was an error initializing the YouTubePlayer (%1$s)",errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
    public void onInitializationSuccess(com.google.android.youtube.player.YouTubePlayer.Provider provider, com.google.android.youtube.player.YouTubePlayer player, boolean wasRestored) {

        if (!wasRestored) {
            player.cueVideo(VIDEO_ID);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == RECOVERY_DIALOG_REQUEST){
            getYouTubePlayerProvider().initialize(DEVELOPER_KEY, this);
        }
    }
    protected com.google.android.youtube.player.YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView)findViewById(R.id.youtubeplayerfragment);
    }
    public void init(){
        mUrl = "http://"+getResources().getString(R.string.ip)+":80/dlyoutu.php";
        tvyu = (TextView)findViewById(R.id.tvyu);
        Queue = Volley.newRequestQueue(this);
    }
}
