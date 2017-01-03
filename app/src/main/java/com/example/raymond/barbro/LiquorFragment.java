package com.example.raymond.barbro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.raymond.barbro.data.Drink;
import com.example.raymond.barbro.utilities.BarJsonUtils;
import com.example.raymond.barbro.utilities.NetworkUtils;

import java.net.URL;

/**
 * Created by raymond on 12/22/16.
 */

/*public class LiquorFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Drink[]>, DrinkAdapter.DrinkAdapterOnClickHandler {

    private static final int GITHUB_SEARCH_LOADER = 22;

    private RecyclerView mRecyclerView;

    private DrinkAdapter mDrinkAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    private View myView;
    private Spinner mLiquorSpinner;
    private String liqType;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDrinkAdapter = new DrinkAdapter(this);
        mRecyclerView.setAdapter(mDrinkAdapter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.liquor_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLiquorSpinner.setAdapter(adapter);
        mLiquorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                liqType = (String) adapterView.getItemAtPosition(i);
                Loader<String> githubSearchLoader = getLoaderManager().getLoader(GITHUB_SEARCH_LOADER);
                if (githubSearchLoader == null) {
                    getLoaderManager().initLoader(GITHUB_SEARCH_LOADER, null, LiquorFragment.this);
                } else {
                    getLoaderManager().restartLoader(GITHUB_SEARCH_LOADER, null, LiquorFragment.this);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.liq_layout, container, false);
        mRecyclerView = (RecyclerView) myView.findViewById(R.id.recyclerview_drinks);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay = (TextView) myView.findViewById(R.id.tv_error_message_display);
        mLiquorSpinner = (Spinner) myView.findViewById(R.id.liquor_spinner);
        mLoadingIndicator = (ProgressBar) myView.findViewById(R.id.pb_loading_indicator);
        return myView;
    }
    private void showJsonDataView() {
        *//* First, make sure the error is invisible *//*
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        *//* Then, make sure the JSON data is visible *//*

    }

    *//**
     * This method will make the error message visible and hide the JSON
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     *//*
    private void showErrorMessage() {
        *//* First, hide the currently visible data *//*

        *//* Then, show the error *//*
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Drink[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Drink[]>(getContext()) {

            Drink[] mDrinksJson = null;
            @Override
            protected void onStartLoading() {

                *//*
                 * When we initially begin loading in the background, we want to display the
                 * loading indicator to the user
                 *//*


                // COMPLETED (2) If mGithubJson is not null, deliver that result. Otherwise, force a load
                *//*
                 * If we already have cached results, just deliver them now. If we don't have any
                 * cached results, force a load.
                 *//*
                if (mDrinksJson != null) {
                    deliverResult(mDrinksJson);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public Drink[] loadInBackground() {

                try {
                    URL drinkUrl = NetworkUtils.buildUrl(liqType);
                    String drinkSearchResults = NetworkUtils.getResponseFromHttpUrl(drinkUrl);
                    Drink[] drinksJson = BarJsonUtils.getSimpleDrinkStringsFromJson(drinkSearchResults);
                    return drinksJson;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            // COMPLETED (3) Override deliverResult and store the data in mGithubJson
            // COMPLETED (4) Call super.deliverResult after storing the data
            @Override
            public void deliverResult(Drink[] drinksJson) {
                mDrinksJson = drinksJson;
                super.deliverResult(drinksJson);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Drink[]> loader, Drink[] data) {

        *//* When we finish loading, we want to hide the loading indicator from the user. *//*
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if(data != null) {
            mDrinkAdapter.setDrinkData(data);
        }

        *//*
         * If the results are null, we assume an error has occurred. There are much more robust
         * methods for checking errors, but we wanted to keep this particular example simple.
         *//*


        if (null == data) {
            showErrorMessage();
        } else {

            showJsonDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Drink[]> loader) {
        *//*
         * We aren't using this method in our example application, but we are required to Override
         * it to implement the LoaderCallbacks<String> interface
         *//*
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(Drink drink) {

        drinkDetail(drink);

    }
    public void drinkDetail(Drink drink){
        Intent intent = new Intent(getContext(), DrinkDetailActivity.class);
        intent.putExtra("drink", drink);
        startActivity(intent);
    }
}*/
