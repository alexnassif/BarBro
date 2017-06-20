package com.example.raymond.barbro;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.raymond.barbro.data.BarBroContract;

public class VideoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private int drinkVideoId;
    private VideoView videoView;
    private String videoUrl = "http://assets.absolutdrinks.com/videos/";
    private int videoPoint = 0;
    private ProgressBar progressBar;
    private static final int DRINK_BY_ID_LOADER = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if (savedInstanceState == null) {

            Intent intent = getIntent();
            drinkVideoId = intent.getIntExtra("video", 0);

        }
        if (savedInstanceState != null) {
            drinkVideoId = savedInstanceState.getInt("video");
            videoPoint = savedInstanceState.getInt("position");

        }
        videoView = (VideoView) findViewById(R.id.video_view_activity);
        progressBar = (ProgressBar) findViewById(R.id.video_progressbar);
        getSupportLoaderManager().initLoader(DRINK_BY_ID_LOADER, null, this);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                if (!isConnected)
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
                        progressBar.setVisibility(View.GONE);

                    }
                });


            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public void onPause() {
        super.onPause();
        videoPoint = videoView.getCurrentPosition(); //stopPosition is an int
        videoView.pause();
    }
    @Override
    public void onResume() {
        super.onResume();
        videoView.seekTo(videoPoint);
        videoView.start();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt("video", drinkVideoId);
        outState.putInt("position", videoView.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DRINK_BY_ID_LOADER: {
                String stringId = Integer.toString(drinkVideoId);
                Uri uri = BarBroContract.BarBroEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                return new CursorLoader(this,
                        uri,
                        null,
                        null,
                        null,
                        null);
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == DRINK_BY_ID_LOADER) {
            data.moveToFirst();
            int videoId = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_VIDEO);

            String videoURL = data.getString(videoId);

            if (videoURL != null) {
                videoView.setVideoPath(videoUrl + videoURL);
                videoView.seekTo(videoPoint);
                videoView.start();
            } else {
                Toast.makeText(this, "No Video Available for this Drink", Toast.LENGTH_LONG).show();
                finish();
            }


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
