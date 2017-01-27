package com.example.raymond.barbro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.raymond.barbro.data.Drink;

import java.util.ArrayList;

public class DrinkDetailActivity extends AppCompatActivity {

    private Drink drink;
    private ImageView mDrinkImageView;
    private TextView mDrinkTitle;
    private TextView mIngredients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);
        mDrinkImageView = (ImageView) findViewById(R.id.drink_detail_image);
        mDrinkTitle = (TextView) findViewById(R.id.drink_name);
        mIngredients = (TextView) findViewById(R.id.drink_ingredients);
        Intent intent = getIntent();
        drink = (Drink) intent.getSerializableExtra("drink");

        mDrinkTitle.setText(drink.getDrinkName());;
        String ingredients = drink.getIngredients();
        mIngredients.setText(ingredients);
        Glide.with(this).load("http://assets.absolutdrinks.com/drinks/300x400/" + drink.getId() +".png").into(mDrinkImageView);


    }
}
