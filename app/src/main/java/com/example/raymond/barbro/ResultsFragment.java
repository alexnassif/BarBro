package com.example.raymond.barbro;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.raymond.barbro.data.BarBroContract;
import com.example.raymond.barbro.data.Drink;
import com.example.raymond.barbro.utilities.BarJsonUtils;
import com.example.raymond.barbro.utilities.NetworkUtils;

import java.net.URL;
/**
 * Created by raymond on 12/9/16.
 */

public class ResultsFragment extends Fragment implements
        LoaderCallbacks<Cursor>, DrinkAdapter.DrinkAdapterOnClickHandler {

    /* A constant to save and restore the URL that is being displayed */
    private static final String SEARCH_QUERY_URL_EXTRA = "query";

    /*
     * This number will uniquely identify our Loader and is chosen arbitrarily. You can change this
     * to any number you like, as long as you use the same variable name.
     */
    private static final int GITHUB_SEARCH_LOADER = 22;

    private RecyclerView mRecyclerView;

    private DrinkAdapter mDrinkAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    private View myView;
    private AutoCompleteTextView acDrinkTextView;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDrinkAdapter = new DrinkAdapter(this);
        mRecyclerView.setAdapter(mDrinkAdapter);
        getLoaderManager().initLoader(GITHUB_SEARCH_LOADER, null, this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.results_layout, container, false);
        mRecyclerView = (RecyclerView) myView.findViewById(R.id.recyclerview_drinks);
        mErrorMessageDisplay = (TextView) myView.findViewById(R.id.tv_error_message_display);
        acDrinkTextView = (AutoCompleteTextView) myView.findViewById(R.id.search_drinks);
        mLoadingIndicator = (ProgressBar) myView.findViewById(R.id.pb_loading_indicator);
        return myView;
    }

    private void makeGithubSearchQuery(View view) {

        URL drinkSearchUrl = NetworkUtils.buildUrl("vodka");

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, drinkSearchUrl.toString());

        /*
         * Now that we've created our bundle that we will pass to our Loader, we need to decide
         * if we should restart the loader (if the loader already existed) or if we need to
         * initialize the loader (if the loader did NOT already exist).
         *
         * We do this by first store the support loader manager in the variable loaderManager.
         * All things related to the Loader go through through the LoaderManager. Once we have a
         * hold on the support loader manager, (loaderManager) we can attempt to access our
         * githubSearchLoader. To do this, we use LoaderManager's method, "getLoader", and pass in
         * the ID we assigned in its creation. You can think of this process similar to finding a
         * View by ID. We give the LoaderManager an ID and it returns a loader (if one exists). If
         * one doesn't exist, we tell the LoaderManager to create one. If one does exist, we tell
         * the LoaderManager to restart it.
         */
        //LoaderManager loaderManager = getLoaderManager();
        Loader<String> githubSearchLoader = getLoaderManager().getLoader(GITHUB_SEARCH_LOADER);
        if (githubSearchLoader == null) {
            getLoaderManager().initLoader(GITHUB_SEARCH_LOADER, queryBundle, this);
        } else {
            getLoaderManager().restartLoader(GITHUB_SEARCH_LOADER, queryBundle, this);
        }
    }

    /**
     * This method will make the View for the JSON data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showJsonDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the JSON data is visible */

    }

    /**
     * This method will make the error message visible and hide the JSON
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */

        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {

        switch (id){
            case GITHUB_SEARCH_LOADER:
                Uri uriAllDrinks = BarBroContract.BarBroEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        uriAllDrinks,
                        null,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }



    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        /* When we finish loading, we want to hide the loading indicator from the user. */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if(data != null) {
            mDrinkAdapter.swapCursor(data);
            ArrayAdapter<Drink> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, new String[]{"vodka", "gin"});
            acDrinkTextView.setAdapter(adapter);
            acDrinkTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String drink = (String) adapterView.getAdapter().getItem(i);

                    drinkDetail(i);
                    acDrinkTextView.setText("");
                }
            });
        }

        /*
         * If the results are null, we assume an error has occurred. There are much more robust
         * methods for checking errors, but we wanted to keep this particular example simple.
         */


        if (null == data) {
            showErrorMessage();
        } else {

            showJsonDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*
         * We aren't using this method in our example application, but we are required to Override
         * it to implement the LoaderCallbacks<String> interface
         */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(int id) {

        drinkDetail(id);

    }
    public void drinkDetail(int id){
        Intent intent = new Intent(getContext(), DrinkDetailActivity.class);
        intent.putExtra("drink", id);
        startActivity(intent);
    }

}
