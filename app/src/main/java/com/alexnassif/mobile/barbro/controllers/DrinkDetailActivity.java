package com.alexnassif.mobile.barbro.controllers;


import android.content.Intent;
import android.content.pm.ActivityInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexnassif.mobile.barbro.R;
import com.alexnassif.mobile.barbro.ViewModel.CheckFavoriteViewModel;
import com.alexnassif.mobile.barbro.ViewModel.CheckFavoriteViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModel;
import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModelFactory;
import com.alexnassif.mobile.barbro.data.AppDatabase;
import com.alexnassif.mobile.barbro.data.AppExecutors;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkList;
import com.alexnassif.mobile.barbro.utilities.InjectorUtils;
import com.bumptech.glide.Glide;


public class DrinkDetailActivity extends AppCompatActivity {

    private int drink;
    private TextView mDrinkNameView;
    private TextView mDrinkIngredientsView;
    private TextView mRecipeView;
    private ImageView mImageView;
    private DrinkDetailViewModel drinkDetailViewModel;
    private CheckFavoriteViewModel drinkModel;
    private MenuItem favoriteMenuItem;
    private boolean faveFlag = false;
    private AppDatabase mDb;
    private DrinkList mCurrentDrink;
    private Drink mDrink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);

        Intent intent = getIntent();
        drink = intent.getIntExtra("drink", 0);
        Log.d("drinkdetailint" ,drink + "");

        mDrinkNameView = (TextView) findViewById(R.id.drink_name_novideo);
        mDrinkIngredientsView = (TextView) findViewById(R.id.drink_ingredients_novideo);
        mRecipeView = (TextView) findViewById(R.id.drink_recipe_novideo);
        mImageView = (ImageView) findViewById(R.id.drink_pic_novideo);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDb = AppDatabase.getsInstance(this);
        DrinkDetailViewModelFactory detailFactory = InjectorUtils.provideDrinkDetailViewModelFactory(this.getApplicationContext());
        drinkDetailViewModel = ViewModelProviders.of(this, detailFactory).get(DrinkDetailViewModel.class);
        drinkDetailViewModel.setDrink(drink);
        drinkDetailViewModel.getDrinkLV().observe(this, new Observer<Drink>() {
            @Override
            public void onChanged(Drink drink) {
                mDrink = drink;
                mDrinkNameView.setText(drink.getStrDrink());
                mRecipeView.setText(drink.getStrInstructions());
                mDrinkIngredientsView.setText(drink.drinkIngredients());
                Glide.with(mImageView.getContext()).load(drink.getStrDrinkThumb()).into(mImageView);
            }
        });


        CheckFavoriteViewModelFactory checkFaveFactory = InjectorUtils.provideCheckFavoriteViewModelFactory(this.getApplicationContext());
        drinkModel = ViewModelProviders.of(this, checkFaveFactory).get(CheckFavoriteViewModel.class);
        drinkModel.setDrink(drink);
        drinkModel.checkDrinkFave().observe(this, new Observer<DrinkList>() {
            @Override
            public void onChanged(DrinkList drinkList) {

                if(drinkList != null){

                    mCurrentDrink = drinkList;
                    faveFlag = true;
                    invalidateOptionsMenu();

                }

            }
        });


    }


    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.fave, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        favoriteMenuItem = menu.findItem(R.id.fave_item);

        if(faveFlag)
            favoriteMenuItem.setIcon(R.drawable.ic_fave);
        else {
            favoriteMenuItem.setIcon(R.drawable.ic_non_fave);
        }

        return super.onPrepareOptionsMenu(menu);
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
                        invalidateOptionsMenu();
                    }
                });
            }else{
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        DrinkList newFave = new DrinkList(DrinkDetailActivity.this.mDrink.getStrDrink(),
                                DrinkDetailActivity.this.mDrink.getStrDrinkThumb(),
                                String.valueOf(DrinkDetailActivity.this.mDrink.getIdDrink()));
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