package com.alexnassif.mobile.barbro;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexnassif.mobile.barbro.data.BarBroContract;
import com.bumptech.glide.Glide;

import java.io.File;

public class MyDrinkDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private int drinkId;
    private TextView mMyDrinkName;
    private TextView mMyDrinkIngredients;
    private ImageView mMyDrinkImage;
    private Button mEditButton;
    private String picFile;
    private static final int MY_DRINK_DETAIL_LOADER = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_drink_detail);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            drinkId = intent.getIntExtra("drink", 1);
        }
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mMyDrinkImage = (ImageView) findViewById(R.id.my_drink_pic_view);
        mMyDrinkName = (TextView) findViewById(R.id.my_drink_name_view);
        mMyDrinkIngredients = (TextView) findViewById(R.id.my_ingredients_view);
        mEditButton = (Button) findViewById(R.id.edit_mydrink_button);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyDrinkDetailActivity.this, EditDrinkActivity.class);
                intent.putExtra("drink", drinkId);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportLoaderManager().initLoader(MY_DRINK_DETAIL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case MY_DRINK_DETAIL_LOADER:{
                String stringId = Integer.toString(drinkId);
                Uri uri = BarBroContract.MyDrinkEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                return new CursorLoader(this,
                        uri,
                        null,
                        null,
                        null,
                        null);}
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data.getCount() > 0) {
            data.moveToFirst();

            int drinkName = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_NAME);
            int ingredients = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYINGREDIENTS);
            int picId = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_PIC);

            mMyDrinkName.setText(data.getString(drinkName));
            mMyDrinkIngredients.setText(data.getString(ingredients));
            picFile = data.getString(picId);
            if (picFile != null) {
                Uri takenPhotoUri = Uri.fromFile(new File(picFile));
                Glide.with(this).load(takenPhotoUri.getPath()).into(mMyDrinkImage);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }
}
