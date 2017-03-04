package com.example.raymond.barbro;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.raymond.barbro.data.BarBroContract;
import com.example.raymond.barbro.data.Drink;

import java.io.File;


public class MyDrinkFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, MyDrinkAdapter.MyDrinkAdapterOnClickHandler {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int MY_DRINK_LOADER = 23;

    private RecyclerView mRecyclerView;

    private MyDrinkAdapter mDrinkAdapter;
    private View myView;
    private AutoCompleteTextView acDrinkTextView;

    public MyDrinkFragment() {
        // Required empty public constructor
    }

    public static MyDrinkFragment newInstance(String param1, String param2) {
        MyDrinkFragment fragment = new MyDrinkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDrinkAdapter = new MyDrinkAdapter(getContext(), this);
        mRecyclerView.setAdapter(mDrinkAdapter);


        getLoaderManager().initLoader(MY_DRINK_LOADER, null, this);
        getActivity().setTitle("My Drinks");

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                Drink drink = (Drink) viewHolder.itemView.getTag();

                String stringId = Integer.toString(drink.getDbId());
                Uri uri = BarBroContract.MyDrinkEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                if(drink.getId() != null){
                    File file = new File(drink.getId());
                    if(file.exists())
                        file.delete();

                }

                getContext().getContentResolver().delete(uri, null, null);

                getLoaderManager().restartLoader(MY_DRINK_LOADER, null, MyDrinkFragment.this);

            }
        }).attachToRecyclerView(mRecyclerView);


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


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case MY_DRINK_LOADER:{
                Uri uriAllDrinks = BarBroContract.MyDrinkEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        uriAllDrinks,
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
        mDrinkAdapter.swapCursor(data);
        Toast.makeText(getActivity(), "number of drinks " + mDrinkAdapter.getItemCount(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(int drink) {

        Intent intent = new Intent(getContext(), MyDrinkDetailActivity.class);
        intent.putExtra("drink", drink);
        startActivity(intent);

    }

}
