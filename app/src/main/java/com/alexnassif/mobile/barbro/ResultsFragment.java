package com.alexnassif.mobile.barbro;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModel;
import com.alexnassif.mobile.barbro.ViewModel.DrinksViewModel;
import com.alexnassif.mobile.barbro.ViewModel.RandomViewModel;
import com.alexnassif.mobile.barbro.data.BarBroContract;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkList;
import com.alexnassif.mobile.barbro.data.HistoryUtils;
import com.bumptech.glide.Glide;

import java.util.List;


public class ResultsFragment extends Fragment implements DrinkAdapter.DrinkAdapterOnClickHandler, SmallDrinkAdapter.SmallDrinkAdapterOnClickHandler {

    private static final String ARG_PARAM1 = "param1";
    private boolean mParam1 = false;

    //private RecyclerView mRecyclerView_Randoms;

    private RecyclerView mRecyclerView;
    private SmallDrinkAdapter mDrinkAdapter_Randoms;
    private DrinkAdapter mDrinkAdapter;
    private View myView;
    private AutoCompleteTextView acDrinkTextView;
    private DrinkDetailFragment drinkDetailFragment;

    private boolean mDualPane;
    int mCurCheckPosition = 0;
    private YouTubeLayout youtubeLayout;
    private TextView viewHeader;
    private int drinkId;
    private TextView viewDesc;
    private TextView mMixView;
    private ImageView mArrowExit;
    private ImageButton minimize;

    private TextView mDrinkTextView;
    private ImageView mDrinkImageView;

    //ViewModels
    private DrinksViewModel model;
    private DrinkDetailViewModel drinkModel;
    private RandomViewModel randomViewModel;

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
        model = ViewModelProviders.of(this).get(DrinksViewModel.class);
        drinkModel = ViewModelProviders.of(this).get(DrinkDetailViewModel.class);
        randomViewModel = ViewModelProviders.of(this).get(RandomViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager_randoms
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        //mRecyclerView_Randoms.setLayoutManager(layoutManager_randoms);
        //mRecyclerView_Randoms.setHasFixedSize(true);
        mDrinkAdapter_Randoms = new SmallDrinkAdapter(getContext(), this);
        //mRecyclerView_Randoms.setAdapter(mDrinkAdapter_Randoms);
        minimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(mRecyclerView_Randoms.getVisibility() == View.GONE) {
                    mRecyclerView_Randoms.setVisibility(View.VISIBLE);
                    minimize.setImageResource(android.R.drawable.arrow_up_float);
                }
                else {
                    mRecyclerView_Randoms.setVisibility(View.GONE);
                    minimize.setImageResource(android.R.drawable.arrow_down_float);
                }*/
            }
        });
        View detailsFrame = getActivity().findViewById(R.id.drink_detail_fragment);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(mParam1 == false) {

            getActivity().setTitle("All Drinks");
        }
        else{

            getActivity().setTitle("Favorites");}


        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 1);
            if(mDualPane) {
                drinkDetailFragment = (DrinkDetailFragment) getFragmentManager().getFragment(savedInstanceState, "myFragmentName");
            }
        }

        if (mDualPane) {
            if(mCurCheckPosition == 0)
                mCurCheckPosition = 1;

            //showDetails(mCurCheckPosition);

        }

        if(!mDualPane) {
            mArrowExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    youtubeLayout.setVisibility(View.GONE);
                }
            });
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model.getDrinks().observe(this, new Observer<List<DrinkList>>() {
            @Override
            public void onChanged(List<DrinkList> drinkLists) {

                mDrinkAdapter = new DrinkAdapter(getContext(), ResultsFragment.this);
                mRecyclerView.setAdapter(mDrinkAdapter);
                mDrinkAdapter.swapCursor(drinkLists);
            }
        });

        randomViewModel.getDrinks().observe(this, new Observer<List<Drink>>() {
            @Override
            public void onChanged(List<Drink> drinks) {
                mDrinkAdapter_Randoms = new SmallDrinkAdapter(getContext(), ResultsFragment.this);
                //mRecyclerView_Randoms.setAdapter(mDrinkAdapter_Randoms);
                mDrinkAdapter_Randoms.swapCursor(drinks);

                final Drink drink = drinks.get(0);

                Glide.with(mDrinkImageView.getContext()).load(drink.getStrDrinkThumb()).into(mDrinkImageView);
                mDrinkTextView.setText(drink.getStrDrink());
                mDrinkImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drinkDetail(Integer.parseInt(drink.getIdDrink()));
                    }
                });
            }
        });

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
            getFragmentManager().putFragment(outState, "myFragmentName", drinkDetailFragment);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.results_layout, container, false);
        mRecyclerView = (RecyclerView) myView.findViewById(R.id.recyclerview_drinks);
        //mRecyclerView_Randoms = (RecyclerView) myView.findViewById(R.id.random_recyclerView);
        acDrinkTextView = (AutoCompleteTextView) myView.findViewById(R.id.search_drinks);
        minimize = (ImageButton) myView.findViewById(R.id.minimize_button);
        mDrinkTextView = (TextView) myView.findViewById(R.id.cardview_drinkname);
        mDrinkImageView = (ImageView) myView.findViewById(R.id.cardview_image);
        if (!mDualPane) {
            viewHeader = (TextView) myView.findViewById(R.id.header);
            viewDesc = (TextView) myView.findViewById(R.id.desc);
            mMixView = (TextView) myView.findViewById(R.id.desc_view_youtube);
            youtubeLayout = (YouTubeLayout) myView.findViewById(R.id.dragLayout);
            mArrowExit = (ImageView) myView.findViewById(R.id.arrowUpExit);
            //youtubeLayout.setVisibility(View.GONE);
        }
        return myView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return true;
    }
    public void drinkDetail(int drink){
        drinkId = drink;
        mCurCheckPosition = drink;
        drinkModel.getDrinks(drink).observe(this, new Observer<Drink>() {
            @Override
            public void onChanged(Drink drink) {
                viewHeader.setText(drink.getStrDrink());
                Log.d("drinkid", drink.getIdDrink() + "");
                viewDesc.setText(drink.drinkIngredients());
                mMixView.setText(drink.getStrInstructions());
                youtubeLayout.setVisibility(View.VISIBLE);
                youtubeLayout.maximize();

            }
        });


    }

    @Override
    public void onClick(int drinkId) {
        //HistoryUtils.addToHistory(getContext(), drink);
        drinkModel.getDrinks(drinkId).observe(this, new Observer<Drink>() {
            @Override
            public void onChanged(Drink drink) {

                if(mDualPane){
                    mCurCheckPosition = Integer.parseInt(drink.getIdDrink());
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    drinkDetailFragment = drinkDetailFragment.newInstance(drink);
                    ft.replace(R.id.drink_detail_fragment, drinkDetailFragment);
                    //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
                else {
                    viewHeader.setText(drink.getStrDrink());
                    Log.d("drinkid", drink.getIdDrink() + "");
                    viewDesc.setText(drink.drinkIngredients());
                    mMixView.setText(drink.getStrInstructions());
                    youtubeLayout.setVisibility(View.VISIBLE);
                    youtubeLayout.maximize();
                }

            }
        });

    }
}
