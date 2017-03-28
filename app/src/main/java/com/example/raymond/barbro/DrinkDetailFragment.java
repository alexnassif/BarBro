package com.example.raymond.barbro;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.raymond.barbro.data.BarBroContract;
import com.example.raymond.barbro.data.Drink;



public class DrinkDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private String drinkObj = "drinkId";
    private View myView;
    private int drinkId;
    private ImageView mImageView;
    private TextView mDrinkTitle;
    private TextView mIngredients;
    private static final int DRINK_SEARCH_LOADER = 1;
    private String videoURL;

    public DrinkDetailFragment() {
        // Required empty public constructor
    }


    public static DrinkDetailFragment newInstance(int drinkId) {
        DrinkDetailFragment fragment = new DrinkDetailFragment();
        Bundle args = new Bundle();
        args.putInt("drinkId", drinkId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            drinkId = getArguments().getInt(drinkObj);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.drink_detail_novideo, container, false);
        mDrinkTitle = (TextView) myView.findViewById(R.id.drink_name_novideo);
        mIngredients = (TextView) myView.findViewById(R.id.drink_ingredients_novideo);
        mImageView = (ImageView) myView.findViewById(R.id.drink_pic_novideo);
        // mDrinkVideo = (VideoView) myView.findViewById(R.id.drink_video);
        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DRINK_SEARCH_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DRINK_SEARCH_LOADER: {
                Uri uriAllDrinks = BarBroContract.BarBroEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
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
        int drinkPic = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_PIC);
        int videoId = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_VIDEO);

        mDrinkTitle.setText(data.getString(drinkName));
        mIngredients.setText(data.getString(ingredients));
        Glide.with(mImageView.getContext()).load("http://assets.absolutdrinks.com/drinks/300x400/" + data.getString(drinkPic) + ".png").into(mImageView);
        videoURL = data.getString(videoId);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
