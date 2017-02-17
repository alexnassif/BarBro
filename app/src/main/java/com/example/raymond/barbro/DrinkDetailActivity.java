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
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.raymond.barbro.data.BarBroContract;
import com.example.raymond.barbro.data.Drink;


public class DrinkDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int DRINK_SEARCH_LOADER = 1;
    private int drinkId;
    private TextView mDrinkNameView;
    private TextView mDrinkIngredientsView;
    private VideoView mVideoView;
    private String videoUrl = "http://assets.absolutdrinks.com/videos/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);
        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.

            Intent intent = getIntent();
            drinkId = intent.getIntExtra("drink", 0);

        }

        mDrinkNameView = (TextView) findViewById(R.id.drink_name);
        mDrinkIngredientsView = (TextView) findViewById(R.id.drink_ingredients);
        mVideoView = (VideoView) findViewById(R.id.drink_video);
        getSupportLoaderManager().initLoader(DRINK_SEARCH_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case DRINK_SEARCH_LOADER: {
                Uri uriAllDrinks = BarBroContract.BarBroEntry.CONTENT_URI;
                return new CursorLoader(this,
                        uriAllDrinks,
                        null,
                        BarBroContract.BarBroEntry._ID + "=?",
                        new String[]{String.valueOf(drinkId)},
                        null);
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();

        int drinkName = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_NAME);
        int ingredients = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_INGREDIENTS);
        int videoId = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_VIDEO);

        mDrinkNameView.setText(data.getString(drinkName));
        mDrinkIngredientsView.setText(data.getString(ingredients));
        String videoURL = data.getString(videoId);
        if(videoURL != null) {
            mVideoView.setVideoPath(videoUrl + videoURL);
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            final boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    if(!isConnected)
                        Toast.makeText(DrinkDetailActivity.this, "No Network Connectivity. Cannot Play Video", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            MediaController mediaController = new
                    MediaController(this);
            mediaController.setAnchorView(mVideoView);
            mVideoView.setMediaController(mediaController);
            mVideoView.start();
        }
        else {
            Toast.makeText(this, "No Video Available for this Drink", Toast.LENGTH_LONG).show();
            mVideoView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
