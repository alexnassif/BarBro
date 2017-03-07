package com.example.raymond.barbro;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    private String drinkVideoId;
    private VideoView videoView;
    private String videoUrl = "http://assets.absolutdrinks.com/videos/";
    private int videoPoint = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if (savedInstanceState == null) {

            Intent intent = getIntent();
            drinkVideoId = intent.getStringExtra("video");

        }
        if(savedInstanceState != null){
            drinkVideoId = savedInstanceState.getString("video");
            videoPoint = savedInstanceState.getInt("position");

        }
        videoView = (VideoView) findViewById(R.id.video_view_activity);
        if(drinkVideoId != null) {
            videoView.setVideoPath(videoUrl + drinkVideoId);

            ConnectivityManager cm =
                    (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            final boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    if(!isConnected)
                        Toast.makeText(VideoActivity.this, "No Network Connectivity. Cannot Play Video", Toast.LENGTH_LONG).show();
                    return true;
                }
            });

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                /*
                 * add media controller
                 */
                            final MediaController mediaController = new MediaController(VideoActivity.this);
                            videoView.setMediaController(mediaController);
                /*
                 * and set its position on screen
                 */
                            mediaController.setAnchorView(videoView);

                        }
                    });


                }
            });
            videoView.seekTo(videoPoint);
            videoView.start();
        }
        else {
            Toast.makeText(this, "No Video Available for this Drink", Toast.LENGTH_LONG).show();
            videoView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("video", drinkVideoId);
        outState.putInt("position", videoView.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }
}
