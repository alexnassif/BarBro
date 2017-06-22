package com.alexnassif.mobile.barbro;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.alexnassif.mobile.barbro.data.BarBroContract;
import com.alexnassif.mobile.barbro.data.Drink;

import java.io.File;


public class MyDrinkFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, MyDrinkAdapter.MyDrinkAdapterOnClickHandler {

    private static final int MY_DRINK_LOADER = 23;

    private RecyclerView mRecyclerView;

    private MyDrinkAdapter mDrinkAdapter;
    private View myView;
    private AutoCompleteTextView acDrinkTextView;
    private SharedPreferences sp;
    private boolean showTip;
    private static final String show_Tip = "showTipMyDrink";

    public MyDrinkFragment() {
        // Required empty public constructor
    }

    public static MyDrinkFragment newInstance(String param1, String param2) {
        MyDrinkFragment fragment = new MyDrinkFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sp = getContext().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        if(sp.contains(show_Tip)){
            showTip = sp.getBoolean(show_Tip, true);
        }
        else{
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(show_Tip, true);
            editor.apply();
            showTip = true;
        }
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
                    if(file.exists()) {
                        file.delete();
                    }

                }

                AsyncQueryHandler deleteDrink = new AsyncQueryHandler(getContext().getContentResolver()) {

                    @Override
                    protected void onDeleteComplete(int token, Object cookie, int result) {
                        super.onDeleteComplete(token, cookie, result);
                        getLoaderManager().restartLoader(MY_DRINK_LOADER, null, MyDrinkFragment.this);

                    }
                };
                deleteDrink.startDelete(-1, null, uri, null, null);

            }
        }).attachToRecyclerView(mRecyclerView);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_mydrink, container, false);
        mRecyclerView = (RecyclerView) myView.findViewById(R.id.recyclerview_drinks);
        acDrinkTextView = (AutoCompleteTextView) myView.findViewById(R.id.search_drinks);
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

        if(data.getCount() == 0){
            Toast.makeText(getContext(), "You haven't added your own recipe yet", Toast.LENGTH_LONG).show();
        } else {
            int drinkId = data.getColumnIndex(BarBroContract.MyDrinkEntry._ID);
            int drinkName = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_NAME);
            int ingredients = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYINGREDIENTS);
            int drinkPicId = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_PIC);
            Drink[] array = new Drink[data.getCount()];
            int i = 0;
            data.moveToFirst();
            while (!data.isAfterLast()) {

                Drink drink = new Drink(data.getString(drinkName), data.getString(ingredients), data.getString(drinkPicId));
                drink.setDbId(data.getInt(drinkId));
                array[i] = drink;
                i++;
                data.moveToNext();
            }

            mDrinkAdapter.swapCursor(data);
            ArrayAdapter<Drink> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, array);
            acDrinkTextView.setAdapter(adapter);
            acDrinkTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Drink drink = (Drink) adapterView.getAdapter().getItem(i);
                    drinkDetail(drink.getDbId());
                    acDrinkTextView.setText("");

                }
            });
            if(showTip) {
                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(R.id.fragmentHistory), "Swipe to delete",
                                Snackbar.LENGTH_LONG)
                        .setAction("Don't Show Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean(show_Tip, false);
                                editor.apply();
                            }
                        });

                snackbar.show();
            }


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(int drink) {

        drinkDetail(drink);

    }

    public void drinkDetail(int drink){

        Intent intent = new Intent(getContext(), MyDrinkDetailActivity.class);
        intent.putExtra("drink", drink);
        startActivity(intent);
    }

}
