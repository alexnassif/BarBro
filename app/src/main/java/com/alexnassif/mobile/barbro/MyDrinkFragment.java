package com.alexnassif.mobile.barbro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.alexnassif.mobile.barbro.ViewModel.MyDrinksViewModel;
import com.alexnassif.mobile.barbro.data.AppDatabase;
import com.alexnassif.mobile.barbro.data.AppExecutors;
import com.alexnassif.mobile.barbro.data.MyDrink;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

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

                MyDrink drink = (MyDrink) viewHolder.itemView.getTag();

                if(drink.getPic() != null){
                    File file = new File(drink.getPic());
                    if(file.exists()) {
                        file.delete();
                    }

                }

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<MyDrink> myDrinks = mDrinkAdapter.getmDrinkData();
                        mDb.myDrinksDao().deleteMyDrink(myDrinks.get(position));
                    }
                });

            }
        }).attachToRecyclerView(mRecyclerView);

        setupViewModel();

    }

    private void setupViewModel(){

        MyDrinksViewModel viewModel = ViewModelProviders.of(this).get(MyDrinksViewModel.class);
        viewModel.getMyDrinks().observe(this, new Observer<List<MyDrink>>() {
            @Override
            public void onChanged(List<MyDrink> myDrinks) {
                mDrinkAdapter.swapCursor(myDrinks);
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
    public void onClick(MyDrink drink) {

        //drinkDetail(drink);

    }

    public void drinkDetail(int drink){

        Intent intent = new Intent(getContext(), MyDrinkDetailActivity.class);
        intent.putExtra("drink", drink);
        startActivity(intent);
    }

}
