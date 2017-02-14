package com.example.raymond.barbro;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.raymond.barbro.data.Drink;


public class DrinkDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            Intent intent = getIntent();
            int drinkId = intent.getIntExtra("drink", 0);
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, DrinkDetailFragment.newInstance(drinkId)).commit();
        }


    }
}
