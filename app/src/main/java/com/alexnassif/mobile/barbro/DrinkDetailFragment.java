package com.alexnassif.mobile.barbro;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexnassif.mobile.barbro.ViewModel.CheckFavoriteViewModel;
import com.alexnassif.mobile.barbro.ViewModel.CheckFavoriteViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModel;
import com.alexnassif.mobile.barbro.ViewModel.FavoriteDetailViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.FavoritesDetailViewModel;
import com.alexnassif.mobile.barbro.data.AppDatabase;
import com.alexnassif.mobile.barbro.data.AppExecutors;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkList;
import com.alexnassif.mobile.barbro.utilities.InjectorUtils;
import com.bumptech.glide.Glide;

public class DrinkDetailFragment extends Fragment {

    private static final String drinkId = "drink";
    private View myView;
    private ImageView mImageView;
    private TextView mDrinkTitle;
    private TextView mIngredients;
    private TextView mRecipe;
    private MenuItem favoriteMenuItem;
    private CheckFavoriteViewModel drinkModel;
    private DrinkDetailViewModel model;
    private boolean faveFlag = false;
    private AppDatabase mDb;
    private DrinkList mCurrentDrink;
    private Drink mDrink;
    private int drink;

    public DrinkDetailFragment() {
        // Required empty public constructor
    }

    public static DrinkDetailFragment newInstance(int drink) {
        DrinkDetailFragment fragment = new DrinkDetailFragment();
        Bundle args = new Bundle();
        args.putInt(drinkId, drink);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            drink = getArguments().getInt(drinkId);
        }
        mDb = AppDatabase.getsInstance(getContext());
        model = ViewModelProviders.of(getActivity()).get(DrinkDetailViewModel.class);
        model.getDrinkLV().observe(this, new Observer<Drink>() {
            @Override
            public void onChanged(Drink drinkId) {
                mDrink = drinkId;
                drink = drinkId.getIdDrink();
                Log.d("drinktitle", drinkId.getStrDrink());
                mDrinkTitle.setText(drinkId.getStrDrink());
                mIngredients.setText(drinkId.drinkIngredients());
                mRecipe.setText(drinkId.getStrInstructions());
                Glide.with(mImageView.getContext()).load(drinkId.getStrDrinkThumb()).into(mImageView);
            }
        });



        drinkModel = ViewModelProviders.of(getActivity()).get(CheckFavoriteViewModel.class);

        drinkModel.checkDrinkFave().observe(this, new Observer<DrinkList>() {
            @Override
            public void onChanged(DrinkList drinkList) {

                if(drinkList != null){

                    mCurrentDrink = drinkList;
                    faveFlag = true;

                }else
                    faveFlag = false;
                getActivity().invalidateOptionsMenu();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.drink_detail_novideo, container, false);
        mDrinkTitle = (TextView) myView.findViewById(R.id.drink_name_novideo);
        mIngredients = (TextView) myView.findViewById(R.id.drink_ingredients_novideo);
        mImageView = (ImageView) myView.findViewById(R.id.drink_pic_novideo);
        mRecipe = (TextView) myView.findViewById(R.id.drink_recipe_novideo);
        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.fave, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        favoriteMenuItem = menu.findItem(R.id.fave_item);

        if(faveFlag)
            favoriteMenuItem.setIcon(R.drawable.ic_fave);
        else {
            favoriteMenuItem.setIcon(R.drawable.ic_non_fave);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.fave_item) {

            if(faveFlag){
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.favoritesDao().deleteFavorite(mCurrentDrink);
                        faveFlag = false;
                        getActivity().invalidateOptionsMenu();
                    }
                });
            }else{
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        DrinkList newFave = new DrinkList(DrinkDetailFragment.this.mDrink.getStrDrink(),
                                DrinkDetailFragment.this.mDrink.getStrDrinkThumb(),
                                String.valueOf(DrinkDetailFragment.this.mDrink.getIdDrink()));
                        mDb.favoritesDao().insertFavorite(newFave);
                        faveFlag = true;
                    }
                });
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
