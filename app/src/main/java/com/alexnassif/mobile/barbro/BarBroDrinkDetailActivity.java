package com.alexnassif.mobile.barbro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexnassif.mobile.barbro.data.BarBroDrink;

import java.util.List;

public class BarBroDrinkDetailActivity extends AppCompatActivity {

    private BarBroDrink drink;
    private TextView mDrinkNameView;
    private TextView mDrinkIngredientsView;
    private TextView mRecipeView;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_bro_drink_detail);

        Intent intent = getIntent();
        drink = (BarBroDrink) intent.getSerializableExtra("drink");

        mDrinkNameView = findViewById(R.id.drink_name_novideo);
        mDrinkIngredientsView = findViewById(R.id.drink_ingredients_novideo);
        mRecipeView = findViewById(R.id.drink_recipe_novideo);
        mImageView = findViewById(R.id.drink_pic_novideo);

        mDrinkNameView.setText(drink.getDrinkName());
        mRecipeView.setText(drink.getInstructions());

        mDrinkIngredientsView.setText(ingredientString(drink.getIngredients(), drink.getMeasurements()));

    }

    private String ingredientString(List<String> ingredients, List<String> measurements){

        String ing = "";

        for(int i = 0; i < ingredients.size(); i++){

            ing += measurements.get(i) + " " + ingredients.get(i) + "\n";
        }

        return ing;
    }
}
