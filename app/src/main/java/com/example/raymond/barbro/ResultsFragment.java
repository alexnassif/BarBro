package com.example.raymond.barbro;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
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


public class ResultsFragment extends Fragment implements
        LoaderCallbacks<Cursor>, DrinkAdapter.DrinkAdapterOnClickHandler {

    private static final String ARG_PARAM1 = "param1";
    private boolean mParam1 = false;
    /*
     * This number will uniquely identify our Loader and is chosen arbitrarily. You can change this
     * to any number you like, as long as you use the same variable name.
     */
    private static final int GITHUB_SEARCH_LOADER = 22;
    private static final int FAVE_LOADER = 23;

    private RecyclerView mRecyclerView;

    private DrinkAdapter mDrinkAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    private View myView;
    private AutoCompleteTextView acDrinkTextView;

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

        if(mParam1 == false)
            getLoaderManager().initLoader(GITHUB_SEARCH_LOADER, null, this);
        else
            getLoaderManager().initLoader(FAVE_LOADER, null, this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.results_layout, container, false);
        mRecyclerView = (RecyclerView) myView.findViewById(R.id.recyclerview_drinks);
        //mErrorMessageDisplay = (TextView) myView.findViewById(R.id.tv_error_message_display);
        acDrinkTextView = (AutoCompleteTextView) myView.findViewById(R.id.search_drinks);
        //mLoadingIndicator = (ProgressBar) myView.findViewById(R.id.pb_loading_indicator);
        return myView;
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
       // mErrorMessageDisplay.setVisibility(View.INVISIBLE);
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
        //mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {

        switch (id){
            case GITHUB_SEARCH_LOADER:{
                Uri uriAllDrinks = BarBroContract.BarBroEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        uriAllDrinks,
                        null,
                        null,
                        null,
                        null);}
            case FAVE_LOADER:{
                Uri uriAllDrinks = BarBroContract.BarBroEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        uriAllDrinks,
                        null,
                        BarBroContract.BarBroEntry.COLUMN_FAVORITE + "=?",
                        new String[]{"1"},
                        null);
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }



    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        int drinkName = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_NAME);
        int ingredients = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_INGREDIENTS);
        int drinkPicId = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_PIC);
        Drink[] array = new Drink[data.getCount()];
        int i = 0;
        data.moveToFirst();
        while(!data.isAfterLast()){

            array[i] = new Drink(data.getString(drinkName), data.getString(ingredients), data.getString(drinkPicId));
            i++;
            data.moveToNext();
        }
       // mLoadingIndicator.setVisibility(View.INVISIBLE);
        if(data != null) {
            mDrinkAdapter.swapCursor(data);
            ArrayAdapter<Drink> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, array);
            acDrinkTextView.setAdapter(adapter);
            acDrinkTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Drink drink = (Drink) adapterView.getAdapter().getItem(i);

                    drinkDetail(drink);
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
        //mLoadingIndicator.setVisibility(View.INVISIBLE);
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

}
