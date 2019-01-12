package com.alexnassif.mobile.barbro;

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
    private ImageView mImageView;
    private TextView mDrinkTitle;
    private TextView mIngredients;
    private TextView mRecipe;
    private ImageButton minimize;
    private View layoutview;
    private View cardlayoutview;

    private TextView mDrinkTextView;
    private ImageView mDrinkImageView;

    //ViewModels
    private DrinksViewModel model;
    private DrinkDetailViewModel drinkModel;
    private RandomViewModel randomViewModel;

    private Menu mMenu;
    private MenuInflater mMenuInflater;
    private boolean isMenu = false;


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
        drinkModel = ViewModelProviders.of(getActivity()).get(DrinkDetailViewModel.class);
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
        mDrinkAdapter_Randoms = new SmallDrinkAdapter(getContext(), this);
        minimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardlayoutview.getVisibility() == View.GONE) {
                    cardlayoutview.setVisibility(View.VISIBLE);
                    minimize.setImageResource(android.R.drawable.arrow_up_float);
                }
                else {
                    cardlayoutview.setVisibility(View.GONE);
                    minimize.setImageResource(android.R.drawable.arrow_down_float);
                }
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

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            drinkDetailFragment = drinkDetailFragment.newInstance();
            ft.replace(R.id.drink_detail_fragment, drinkDetailFragment);
            //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

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
        cardlayoutview = myView.findViewById(R.id.card_layout);
        if (!mDualPane) {

            layoutview = myView.findViewById(R.id.novideo_drink_detail);
            mDrinkTitle = (TextView) myView.findViewById(R.id.drink_name_novideo);
            mIngredients = (TextView) myView.findViewById(R.id.drink_ingredients_novideo);
            mImageView = (ImageView) myView.findViewById(R.id.drink_pic_novideo);
            mRecipe = (TextView) myView.findViewById(R.id.drink_recipe_novideo);
        }
        return myView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(!mDualPane){
            mMenu = menu;
            mMenuInflater = inflater;
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.close_item){
            layoutview.setVisibility(View.INVISIBLE);
            mMenu.clear();
            isMenu = !isMenu;
            if(!mParam1) {

                getActivity().setTitle("All Drinks");
            }
            else{

                getActivity().setTitle("Favorites");}
        }

        return true;
    }
    public void drinkDetail(int drink){
        mCurCheckPosition = drink;
        drinkModel.setDrink(drink);
        if(!mDualPane){

            drinkModel.getDrink().observe(this, new Observer<Drink>() {
                @Override
                public void onChanged(Drink drink) {
                    getActivity().setTitle(drink.getStrDrink());
                    layoutview.setVisibility(View.VISIBLE);
                    mDrinkTitle.setText(drink.getStrDrink());
                    mIngredients.setText(drink.drinkIngredients());
                    mRecipe.setText(drink.getStrInstructions());
                    Glide.with(mImageView.getContext()).load(drink.getStrDrinkThumb()).into(mImageView);
                    if(!isMenu) {
                        mMenuInflater.inflate(R.menu.close, mMenu);
                        isMenu = !isMenu;

                    }

                }
            });
        }


    }

    @Override
    public void onClick(int drinkId) {
        //HistoryUtils.addToHistory(getContext(), drink);

        drinkDetail(drinkId);
    }
}
