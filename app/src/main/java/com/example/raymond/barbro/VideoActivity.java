package com.example.raymond.barbro;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VideoActivity extends AppCompatActivity {
    private String drinkVideoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if (savedInstanceState == null) {

            Intent intent = getIntent();
            drinkVideoId = intent.getStringExtra("video");

        }
        if(savedInstanceState != null){
            drinkVideoId = savedInstanceState.getString("video");
        }
        VideoFragment video = VideoFragment.newInstance(drinkVideoId);

        // Execute a transaction, replacing any existing fragment
        // with this one inside the frame.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.video_activity_view, video);


        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("video", drinkVideoId);
    }
}
