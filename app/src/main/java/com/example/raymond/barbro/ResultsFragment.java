package com.example.raymond.barbro;


import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.raymond.barbro.data.BarBroContract;
import com.example.raymond.barbro.data.Drink;
import com.example.raymond.barbro.data.HistoryUtils;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;


public class ResultsFragment extends Fragment implements
        LoaderCallbacks<Cursor>, DrinkAdapter.DrinkAdapterOnClickHandler, SmallDrinkAdapter.SmallDrinkAdapterOnClickHandler {

    private static final String ARG_PARAM1 = "param1";
    private boolean mParam1 = false;
    /*
     * This number will uniquely identify our Loader and is chosen arbitrarily. You can change this
     * to any number you like, as long as you use the same variable name.
     */
    private static final int GITHUB_SEARCH_LOADER = 22;
    private static final int FAVE_LOADER = 23;
    private static final int DRINK_BY_ID_LOADER = 24;
    private static final int RANDOM_DRINKS_LOADER = 20;
    private RecyclerView mRecyclerView_Randoms;

    private RecyclerView mRecyclerView;
    private SmallDrinkAdapter mDrinkAdapter_Randoms;
    private DrinkAdapter mDrinkAdapter;
    private View myView;
    private AutoCompleteTextView acDrinkTextView;
    private VideoFragment videoFragment;
    private DrinkDetailFragment drinkDetailFragment;

    private boolean mDualPane;
    int mCurCheckPosition = 0;
    private String videoURL = "pennsylvania.mp4";
    private YouTubeLayout youtubeLayout;
    private TextView viewHeader;
    private int drinkId;
    private TextView viewDesc;
    private TextView mMixView;
    private boolean whichFragment = true;


    public static ResultsFragment newInstance(boolean param1) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getBoolean(ARG_PARAM1);
        }

        setHasOptionsMenu(true);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDrinkAdapter = new DrinkAdapter(getContext(), this);
        mRecyclerView.setAdapter(mDrinkAdapter);
        LinearLayoutManager layoutManager_randoms
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView_Randoms.setLayoutManager(layoutManager_randoms);
        mRecyclerView_Randoms.setHasFixedSize(true);
        mDrinkAdapter_Randoms = new SmallDrinkAdapter(getContext(), this);
        mRecyclerView_Randoms.setAdapter(mDrinkAdapter_Randoms);
        View detailsFrame = getActivity().findViewById(R.id.drink_detail_fragment);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(mParam1 == false) {
            getLoaderManager().initLoader(GITHUB_SEARCH_LOADER, null, this);
            getActivity().setTitle("All Drinks");
        }
        else{
            getLoaderManager().initLoader(FAVE_LOADER, null, this);
            getActivity().setTitle("Favorites");}
        getLoaderManager().initLoader(RANDOM_DRINKS_LOADER, null, this);

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 1);
            if(mDualPane) {
                whichFragment = savedInstanceState.getBoolean("fragment");
                if (whichFragment)
                    drinkDetailFragment = (DrinkDetailFragment) getFragmentManager().getFragment(savedInstanceState, "myFragmentName");
                else
                    videoFragment = (VideoFragment) getFragmentManager().getFragment(savedInstanceState, "myFragmentName");
            }
        }

        if (mDualPane) {
            if(mCurCheckPosition == 0)
                mCurCheckPosition = 1;
            if(whichFragment)
                showDetails(mCurCheckPosition);
            else {
                FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
                //videoFragment = videoFragment.newInstance(videoURL);
                fragmentManager
                        .replace(R.id.drink_detail_fragment, videoFragment)
                        .commit();
            }
        }

    }

    @Override
    public void onDestroy() {
        getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
        if(mDualPane) {
            if (whichFragment)
                getFragmentManager().putFragment(outState, "myFragmentName", drinkDetailFragment);
            else
                getFragmentManager().putFragment(outState, "myFragmentName", videoFragment);

            outState.putBoolean("fragment", whichFragment);
        }
    }
    void showDetails(int index) {
        whichFragment = true;
        mCurCheckPosition = index;

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.

            // Check what fragment is currently shown, replace if needed.
            //DrinkDetailFragment details = DrinkDetailFragment.newInstance(index);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                drinkDetailFragment = drinkDetailFragment.newInstance(index);
                ft.replace(R.id.drink_detail_fragment, drinkDetailFragment);


                //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();


        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.results_layout, container, false);
        mRecyclerView = (RecyclerView) myView.findViewById(R.id.recyclerview_drinks);
        mRecyclerView_Randoms = (RecyclerView) myView.findViewById(R.id.random_recyclerView);
        acDrinkTextView = (AutoCompleteTextView) myView.findViewById(R.id.search_drinks);
        if (!mDualPane) {
            viewHeader = (TextView) myView.findViewById(R.id.header);
            viewDesc = (TextView) myView.findViewById(R.id.desc);
            mMixView = (TextView) myView.findViewById(R.id.desc_view_youtube);
            youtubeLayout = (YouTubeLayout) myView.findViewById(R.id.dragLayout);
            //youtubeLayout.setVisibility(View.GONE);
        }
        return myView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {

        switch (id){
            case RANDOM_DRINKS_LOADER:{
                Uri uriAllDrinks = BarBroContract.BarBroEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        uriAllDrinks,
                        null,
                        null,
                        null,
                        " RANDOM() LIMIT 3");}
            case GITHUB_SEARCH_LOADER:{
                Uri uriAllDrinks = BarBroContract.BarBroEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        uriAllDrinks,
                        null,
                        null,
                        null,
                        BarBroContract.BarBroEntry.COLUMN_DRINK_NAME + " asc");}
            case FAVE_LOADER:{
                Uri uriAllDrinks = BarBroContract.BarBroEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        uriAllDrinks,
                        null,
                        BarBroContract.BarBroEntry.COLUMN_FAVORITE + "=?",
                        new String[]{"1"},
                        BarBroContract.BarBroEntry.COLUMN_DRINK_NAME + " asc");
            }
            case DRINK_BY_ID_LOADER:{
                String stringId = Integer.toString(drinkId);
                Uri uri = BarBroContract.BarBroEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                Log.d("drink_id tag", uri.toString());
                return new CursorLoader(getContext(),
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

        if(loader.getId() == GITHUB_SEARCH_LOADER || loader.getId() == FAVE_LOADER) {
            if(data.getCount() == 0 && loader.getId() == FAVE_LOADER){
                Toast.makeText(getContext(), "No Favorites added yet", Toast.LENGTH_LONG).show();
            }
            final int drinkId = data.getColumnIndex(BarBroContract.BarBroEntry._ID);
            int drinkName = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_NAME);
            int ingredients = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_INGREDIENTS);
            int drinkPicId = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_PIC);
            int videoId = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_VIDEO);
            Drink[] array = new Drink[data.getCount()];
            int i = 0;
            data.moveToFirst();
            while (!data.isAfterLast()) {

                Drink drink = new Drink(data.getString(drinkName), data.getString(ingredients), data.getString(drinkPicId));
                drink.setVideo(data.getString(videoId));
                drink.setDbId(data.getInt(drinkId));
                array[i] = drink;
                i++;
                data.moveToNext();
            }
            // mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (data != null) {
                mDrinkAdapter.swapCursor(data);
                ArrayAdapter<Drink> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, array);
                acDrinkTextView.setAdapter(adapter);
                acDrinkTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Drink drink = (Drink) adapterView.getAdapter().getItem(i);
                        HistoryUtils.addToHistory(getContext(), drink.getDbId());
                        videoURL = drink.getVideo();
                        mCurCheckPosition = drink.getDbId();
                        //drinkDetail(drink.getDbId());
                        if (mDualPane) {
                            showDetails(drink.getDbId());
                        } else
                            drinkDetail(drink.getDbId());
                        acDrinkTextView.setText("");

                    }
                });
            }
        }
        if(loader.getId() == DRINK_BY_ID_LOADER){
            data.moveToFirst();

            int drinkName = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_NAME);
            int ingredients = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_INGREDIENTS);
            int description = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DESCRIPTION);
            int videoId = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_VIDEO);

            viewHeader.setText(data.getString(drinkName));
            viewDesc.setText(data.getString(ingredients));
            mMixView.setText(data.getString(description));
            videoURL = data.getString(videoId);
            youtubeLayout.setVisibility(View.VISIBLE);
            youtubeLayout.maximize();
        }
        if(loader.getId() == RANDOM_DRINKS_LOADER){
            mDrinkAdapter_Randoms.swapCursor(data);
        }

        /*
         * If the results are null, we assume an error has occurred. There are much more robust
         * methods for checking errors, but we wanted to keep this particular example simple.
         */


        if (null == data) {
            Toast.makeText(getContext(), " null data ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*
         * We aren't using this method in our example application, but we are required to Override
         * it to implement the LoaderCallbacks<String> interface
         */
        //mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.video, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.video_item && mCurCheckPosition != 0) {
            if(mDualPane) {
                whichFragment = false;
                FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
                videoFragment = videoFragment.newInstance(videoURL);
                fragmentManager
                        .replace(R.id.drink_detail_fragment, videoFragment)
                        .commit();
            }
            else {
                Intent intent = new Intent(getContext(), VideoActivity.class);
                intent.putExtra("video", videoURL);
                startActivity(intent);
            }
        }
        else
            Toast.makeText(getContext(), "No Drink Chosen", Toast.LENGTH_LONG).show();


        return true;
    }
    @Override
    public void onClick(int drink, String video) {
        /*AsyncQueryHandler historyDrink = new AsyncQueryHandler(getActivity().getContentResolver()) {
        };
        Uri uriHistory = BarBroContract.HistoryEntry.CONTENT_URI;
        ContentValues newValue = new ContentValues();
        newValue.put(BarBroContract.HistoryEntry.COLUMN_HISTORYID, drink);
        historyDrink.startInsert(-1, null, uriHistory, newValue);*/
        HistoryUtils.addToHistory(getContext(), drink);
        mCurCheckPosition = drink;
        videoURL = video;
        if (mDualPane) {
            showDetails(drink);
        }
        else
            drinkDetail(drink);

    }
    public void drinkDetail(int drink){
        drinkId = drink;
        Loader<Cursor> loaderM = getLoaderManager().getLoader(DRINK_BY_ID_LOADER);
        if(loaderM == null)
            getLoaderManager().initLoader(DRINK_BY_ID_LOADER, null, this);
        else
            getLoaderManager().restartLoader(DRINK_BY_ID_LOADER, null, this);
//        Intent intent = new Intent(getContext(), DrinkDetailActivity.class);
//        intent.putExtra("drink", drink);
//        startActivity(intent);
    }

    @Override
    public void onClick(int drink) {
        drinkDetail(drink);
    }
}
