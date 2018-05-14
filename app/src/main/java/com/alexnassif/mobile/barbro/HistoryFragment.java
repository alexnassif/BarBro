package com.alexnassif.mobile.barbro;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.Toast;
import com.alexnassif.mobile.barbro.data.BarBroContract;
import com.alexnassif.mobile.barbro.model.Drink;
import com.alexnassif.mobile.barbro.data.HistoryUtils;


public class HistoryFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, DrinkAdapter.DrinkAdapterOnClickHandler {

    private static final int HISTORY_SEARCH_LOADER = 22;
    private static final int DRINK_BY_ID_LOADER = 24;

    private RecyclerView mRecyclerView;

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
    private Menu mMenu;
    private MenuInflater mMenuInflater;
    private boolean isMenu = false;
    private ImageView mArrowExit;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View detailsFrame = getActivity().findViewById(R.id.drink_detail_fragment);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getLoaderManager().initLoader(HISTORY_SEARCH_LOADER, null, this);
        getActivity().setTitle("History");

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
                fragmentManager
                        .replace(R.id.drink_detail_fragment, videoFragment)
                        .commit();
            }
        }
        mArrowExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youtubeLayout.setVisibility(View.GONE);
                mMenu.clear();
                isMenu = false;
                mMenuInflater.inflate(R.menu.history, mMenu);
            }
        });

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
            drinkDetailFragment = DrinkDetailFragment.newInstance(index);
            ft.replace(R.id.drink_detail_fragment, drinkDetailFragment);
            ft.commit();


        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_history, container, false);
        mRecyclerView = (RecyclerView) myView.findViewById(R.id.recyclerview_drinks);

        acDrinkTextView = (AutoCompleteTextView) myView.findViewById(R.id.search_drinks);
        if (!mDualPane) {
            viewHeader = (TextView) myView.findViewById(R.id.header);
            viewDesc = (TextView) myView.findViewById(R.id.desc);
            mMixView = (TextView) myView.findViewById(R.id.desc_view_youtube);
            youtubeLayout = (YouTubeLayout) myView.findViewById(R.id.dragLayout);
            mArrowExit = (ImageView) youtubeLayout.findViewById(R.id.arrowUpExit);
        }
        return myView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {

        switch (id){
            case HISTORY_SEARCH_LOADER:{
                Uri uriAllDrinks = BarBroContract.HistoryEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        uriAllDrinks,
                        null,
                        null,
                        null,
                        null);}
            case DRINK_BY_ID_LOADER:{
                String stringId = Integer.toString(drinkId);
                Uri uri = BarBroContract.BarBroEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
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

        if (loader.getId() == HISTORY_SEARCH_LOADER) {
            if (data.getCount() == 0 && loader.getId() == HISTORY_SEARCH_LOADER) {
                Toast.makeText(getContext(), "No history yet", Toast.LENGTH_LONG).show();
                return;
            }
            mDrinkAdapter.swapCursor(data);
            final int idh = data.getColumnIndex(BarBroContract.HistoryEntry.COLUMN_HISTORYID);
            final int drinkName = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_NAME);
            int ingredients = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_INGREDIENTS);
            int drinkPicId = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_PIC);
            int videoId = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_VIDEO);
            Drink[] array = new Drink[data.getCount()];
            int i = 0;
            data.moveToFirst();
            while (!data.isAfterLast()) {

                Drink drink = new Drink(data.getString(drinkName), data.getString(ingredients), data.getString(drinkPicId));
                drink.setVideo(data.getString(videoId));
                drink.set_id(data.getInt(idh));
                array[i] = drink;
                i++;
                data.moveToNext();
            }

            ArrayAdapter<Drink> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, array);
            acDrinkTextView.setAdapter(adapter);
            acDrinkTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Drink drink = (Drink) adapterView.getAdapter().getItem(i);
                    HistoryUtils.addToHistory(getContext(), drink.get_id());
                    videoURL = drink.getVideo();
                    mCurCheckPosition = drink.get_id();
                    if (mDualPane) {
                        showDetails(drink.get_id());
                    } else {
                        drinkDetail(drink.get_id());

                    }
                    acDrinkTextView.setText("");

                }
            });

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

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history, menu);

        mMenu = menu;
        mMenuInflater = inflater;


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.video_item && mCurCheckPosition != 0) {

            Intent intent = new Intent(getContext(), VideoActivity.class);
            intent.putExtra("video", mCurCheckPosition);
            startActivity(intent);

        }
        else if(id == R.id.history_item){
            AsyncQueryHandler deleteHistory = new AsyncQueryHandler(getContext().getContentResolver()){};
            Uri uriAllDrinks = BarBroContract.HistoryEntry.CONTENT_URI;
            deleteHistory.startDelete(-1, null, uriAllDrinks, null, null);

            Loader<Cursor> loaderM = getLoaderManager().getLoader(HISTORY_SEARCH_LOADER);
            if(loaderM == null)
                getLoaderManager().initLoader(HISTORY_SEARCH_LOADER, null, this);
            else
                getLoaderManager().restartLoader(HISTORY_SEARCH_LOADER, null, this);

        }
        else
            Toast.makeText(getContext(), "No Drink Chosen", Toast.LENGTH_LONG).show();


        return true;
    }
    @Override
    public void onClick(int drink) {

        HistoryUtils.addToHistory(getContext(), drink);
        if (mDualPane) {
            showDetails(drink);
        }
        else
            drinkDetail(drink);

    }
    public void drinkDetail(int drink){
        showVideoIcon();
        mCurCheckPosition = drink;
        drinkId = drink;
        Loader<Cursor> loaderM = getLoaderManager().getLoader(DRINK_BY_ID_LOADER);
        if(loaderM == null)
            getLoaderManager().initLoader(DRINK_BY_ID_LOADER, null, this);
        else
            getLoaderManager().restartLoader(DRINK_BY_ID_LOADER, null, this);
    }
    private void showVideoIcon(){

        if(!isMenu) {
            mMenuInflater.inflate(R.menu.video, mMenu);
            isMenu = true;
        }

    }

}
