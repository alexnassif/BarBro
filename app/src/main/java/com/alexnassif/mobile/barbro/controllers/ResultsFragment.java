package com.alexnassif.mobile.barbro.controllers;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.alexnassif.mobile.barbro.Adapters.DrinkAdapter;
import com.alexnassif.mobile.barbro.R;
import com.alexnassif.mobile.barbro.ViewModel.CheckFavoriteViewModel;
import com.alexnassif.mobile.barbro.ViewModel.CheckFavoriteViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModel;
import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.DrinksViewModel;
import com.alexnassif.mobile.barbro.ViewModel.DrinksViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.RandomViewModel;
import com.alexnassif.mobile.barbro.ViewModel.RandomViewModelFactory;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkList;
import com.alexnassif.mobile.barbro.utilities.InjectorUtils;
import com.bumptech.glide.Glide;

import java.util.List;


public class ResultsFragment extends Fragment implements DrinkAdapter.DrinkAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private DrinkAdapter mDrinkAdapter;
    private View myView;
    private AutoCompleteTextView acDrinkTextView;
    private DrinkDetailFragment drinkDetailFragment;

    private boolean mDualPane;
    int mCurCheckPosition = 0;
    private ImageButton minimize;

    private TextView mDrinkTextView;
    private ImageView mDrinkImageView;

    //ViewModels
    private DrinksViewModel model;
    private DrinkDetailViewModel drinkModel;
    private RandomViewModel randomViewModel;
    private CheckFavoriteViewModel faveViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DrinksViewModelFactory factory = InjectorUtils.provideDrinksViewModelFactory(getContext().getApplicationContext());
        model = ViewModelProviders.of(this, factory).get(DrinksViewModel.class);
        DrinkDetailViewModelFactory detailFactory = InjectorUtils.provideDrinkDetailViewModelFactory(getContext().getApplicationContext());
        drinkModel = ViewModelProviders.of(getActivity(), detailFactory).get(DrinkDetailViewModel.class);
        RandomViewModelFactory randomFactory = InjectorUtils.provideRandomDrinkFactory(getContext().getApplicationContext());
        randomViewModel = ViewModelProviders.of(this, randomFactory).get(RandomViewModel.class);
        CheckFavoriteViewModelFactory checkFaveFactory = InjectorUtils.provideCheckFavoriteViewModelFactory(getContext().getApplicationContext());
        faveViewModel = ViewModelProviders.of(getActivity(), checkFaveFactory).get(CheckFavoriteViewModel.class);

        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        minimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomViewModel.loadRandomDrink();
            }
        });
        View detailsFrame = getActivity().findViewById(R.id.drink_detail_fragment);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        getActivity().setTitle("All Drinks");


        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 1);
            if(mDualPane) {
                drinkDetailFragment = (DrinkDetailFragment) getFragmentManager().getFragment(savedInstanceState, "myFragmentName");
            }
        }

        if (mDualPane) {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            drinkDetailFragment = drinkDetailFragment.newInstance(mCurCheckPosition);
            ft.replace(R.id.drink_detail_fragment, drinkDetailFragment);
            //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDrinkAdapter = new DrinkAdapter(getContext(), ResultsFragment.this);
        mRecyclerView.setAdapter(mDrinkAdapter);
        model.getDrinks().observe(this, new Observer<List<DrinkList>>() {
            @Override
            public void onChanged(List<DrinkList> drinkLists) {

                mDrinkAdapter.swapCursor(drinkLists);
                DrinkList[] array = new DrinkList[drinkLists.size()];
                array = drinkLists.toArray(array);
                ArrayAdapter<DrinkList> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, array);
                acDrinkTextView.setAdapter(adapter);

                acDrinkTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        DrinkList drink = (DrinkList) adapterView.getAdapter().getItem(i);
                        drinkDetail(Integer.parseInt(drink.getIdDrink()));
                        acDrinkTextView.setText("");

                    }
                });
            }
        });

        randomViewModel.getDrinks().observe(this, new Observer<List<Drink>>() {
            @Override
            public void onChanged(List<Drink> drinks) {

                final Drink drink = drinks.get(0);

                Glide.with(mDrinkImageView.getContext()).load(drink.getStrDrinkThumb()).into(mDrinkImageView);
                mDrinkTextView.setText(drink.getStrDrink());
                mDrinkImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drinkDetail(drink.getIdDrink());
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
        acDrinkTextView = (AutoCompleteTextView) myView.findViewById(R.id.search_drinks);
        minimize = (ImageButton) myView.findViewById(R.id.minimize_button);
        mDrinkTextView = (TextView) myView.findViewById(R.id.cardview_drinkname);
        mDrinkImageView = (ImageView) myView.findViewById(R.id.cardview_image);

        return myView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
    private void drinkDetail(int drinkId){
        mCurCheckPosition = drinkId;
        drinkModel.setDrink(drinkId);
        faveViewModel.setDrink(drinkId);
        if(!mDualPane){

            Intent drinkdetailIntent = new Intent(getContext(), DrinkDetailActivity.class);
            drinkdetailIntent.putExtra("drink", drinkId);
            startActivity(drinkdetailIntent);

        }


    }

    @Override
    public void onClick(DrinkList drinkId) {

        drinkDetail(Integer.parseInt(drinkId.getIdDrink()));
    }
}