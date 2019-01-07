package com.alexnassif.mobile.barbro;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.alexnassif.mobile.barbro.data.AppDatabase;
import com.alexnassif.mobile.barbro.data.AppExecutors;
import com.alexnassif.mobile.barbro.data.MyDrink;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.util.Log;
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
import java.util.List;


public class MyDrinkFragment extends Fragment implements MyDrinkAdapter.MyDrinkAdapterOnClickHandler {


    private RecyclerView mRecyclerView;

    private MyDrinkAdapter mDrinkAdapter;
    private View myView;
    private AutoCompleteTextView acDrinkTextView;
    private SharedPreferences sp;
    private boolean showTip;
    private static final String show_Tip = "showTipMyDrink";
    private AppDatabase mDb;

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
        mDb = AppDatabase.getsInstance(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
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
                = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDrinkAdapter = new MyDrinkAdapter(getContext(), this);
        mRecyclerView.setAdapter(mDrinkAdapter);

        getActivity().setTitle("My Drinks");

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                /*MyDrink drink = (MyDrink) viewHolder.itemView.getTag();
                String stringId = Integer.toString(drink.getDbId());
                Uri uri = BarBroContract.MyDrinkEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                if(drink.getId() != null){
                    File file = new File(drink.getId());
                    if(file.exists()) {
                        file.delete();
                    }

                }*/

                AsyncQueryHandler deleteDrink = new AsyncQueryHandler(getActivity().getContentResolver()) {

                    @Override
                    protected void onDeleteComplete(int token, Object cookie, int result) {
                        super.onDeleteComplete(token, cookie, result);
                        mRecyclerView.removeAllViewsInLayout();
                    }
                };
                //deleteDrink.startDelete(-1, null, uri, null, null);

            }
        }).attachToRecyclerView(mRecyclerView);


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<MyDrink> mydrinks = mDb.myDrinksDao().loadMyDrinks();
                Log.d("mydrinkssize", mydrinks.get(0).getName());
            }
        });

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
    public void onClick(int drink) {

        drinkDetail(drink);

    }

    public void drinkDetail(int drink){

        Intent intent = new Intent(getContext(), MyDrinkDetailActivity.class);
        intent.putExtra("drink", drink);
        startActivity(intent);
    }

}
