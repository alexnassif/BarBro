package com.alexnassif.mobile.barbro;


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

import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModel;
import com.alexnassif.mobile.barbro.ViewModel.FavoritesViewModel;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkList;
import com.bumptech.glide.Glide;


public class DrinkDetailActivity extends AppCompatActivity {

    private int drink;
    private TextView mDrinkNameView;
    private TextView mDrinkIngredientsView;
    private ImageView mImageView;
    private DrinkDetailViewModel drinkDetailViewModel;
    private FavoritesViewModel drinkModel;
    private MenuItem favoriteMenuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);

        Intent intent = getIntent();
        drink = intent.getIntExtra("drink", 0);
        Log.d("drinkdetailint" ,drink + "");

        mDrinkNameView = (TextView) findViewById(R.id.drink_name);
        mDrinkIngredientsView = (TextView) findViewById(R.id.drink_ingredients);
        mImageView = (ImageView) findViewById(R.id.drink_pic_activity);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        drinkDetailViewModel = ViewModelProviders.of(this).get(DrinkDetailViewModel.class);
        drinkDetailViewModel.setDrink(drink);
        drinkDetailViewModel.getDrink().observe(this, new Observer<Drink>() {
            @Override
            public void onChanged(Drink drink) {
                mDrinkNameView.setText(drink.getStrDrink());
                mDrinkIngredientsView.setText(drink.getStrInstructions());
                Glide.with(mImageView.getContext()).load(drink.getStrDrinkThumb()).into(mImageView);
            }
        });
        drinkModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        drinkModel.getFave(drink).observe(this, new Observer<DrinkList>() {
            @Override
            public void onChanged(DrinkList drinkList) {

                if(drinkList != null){

                    favoriteMenuItem.setIcon(R.drawable.ic_fave);
                }

            }
        });


    }


    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.fave, menu);
        favoriteMenuItem = menu.findItem(R.id.fave_item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.fave_item) {

        }
        return true;
    }
}
