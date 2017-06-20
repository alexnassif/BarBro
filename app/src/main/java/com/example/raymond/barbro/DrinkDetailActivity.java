package com.example.raymond.barbro;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.raymond.barbro.data.BarBroContract;


public class DrinkDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int DRINK_SEARCH_LOADER = 1;
    private int drinkId;
    private TextView mDrinkNameView;
    private TextView mDrinkIngredientsView;
    private ImageView mImageView;
    private String videoURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);
        if (savedInstanceState == null) {

            Intent intent = getIntent();
            drinkId = intent.getIntExtra("drink", 0);

        }

        mDrinkNameView = (TextView) findViewById(R.id.drink_name);
        mDrinkIngredientsView = (TextView) findViewById(R.id.drink_ingredients);
        mImageView = (ImageView) findViewById(R.id.drink_pic_activity);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportLoaderManager().initLoader(DRINK_SEARCH_LOADER, null, this);

    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.video_item) {
            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("video", videoURL);
            startActivity(intent);
            return true;
        }
        return true;
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
        int picId = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_PIC);

        mDrinkNameView.setText(data.getString(drinkName));
        mDrinkIngredientsView.setText(data.getString(ingredients));
        Glide.with(mImageView.getContext()).load("http://assets.absolutdrinks.com/drinks/300x400/" + data.getString(picId) +".png").into(mImageView);
        videoURL = data.getString(videoId);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
