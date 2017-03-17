package com.example.raymond.barbro;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyDrinkDetailActivity extends AppCompatActivity {
    private int drinkId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_drink_detail);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            drinkId = intent.getIntExtra("drink", 1);
        }
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager
                .replace(R.id.my_drink_detail_fragment, MyDrinkDetailFragment.newInstance(drinkId))
                .commit();
    }
}
