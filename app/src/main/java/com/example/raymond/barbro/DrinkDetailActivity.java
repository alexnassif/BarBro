package com.example.raymond.barbro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.raymond.barbro.data.Drink;


public class DrinkDetailActivity extends AppCompatActivity {

    private Drink drink;
    private TextView mDrinkTitle;
    private TextView mIngredients;
    private VideoView mDrinkVideo;
    private String videoUrl = "http://assets.absolutdrinks.com/videos/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);
        mDrinkTitle = (TextView) findViewById(R.id.drink_name);
        mIngredients = (TextView) findViewById(R.id.drink_ingredients);
        mDrinkVideo = (VideoView) findViewById(R.id.drink_video);
        Intent intent = getIntent();
        drink = (Drink) intent.getSerializableExtra("drink");

        mDrinkTitle.setText(drink.getDrinkName());;
        String ingredients = drink.getIngredients();
        mIngredients.setText(ingredients);
        mDrinkVideo.setVideoPath(videoUrl + drink.getVideo());

        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(mDrinkVideo);
        mDrinkVideo.setMediaController(mediaController);
        //mDrinkVideo.start();


    }
}
