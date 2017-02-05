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
        setTitle(drink.getDrinkName());
        mDrinkTitle.setText(drink.getDrinkName());;
        String ingredients = drink.getIngredients();
        mIngredients.setText(ingredients);
        String videoURL = videoUrl + drink.getVideo();
        if(drink.getVideo() != null)
            mDrinkVideo.setVideoPath(videoUrl + drink.getVideo());
        else {
            Toast.makeText(this, "No Video Available for this Drink", Toast.LENGTH_LONG).show();
            mDrinkVideo.setVisibility(View.GONE);
        }
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        mDrinkVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                if(!isConnected)
                    Toast.makeText(getBaseContext(), "No Network Connectivity. Cannot Play Video", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(mDrinkVideo);
        mDrinkVideo.setMediaController(mediaController);
        //mDrinkVideo.start();


    }
}
