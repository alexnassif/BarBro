package com.alexnassif.mobile.barbro;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditDrinkActivity extends AppCompatActivity {
    private int drinkId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_drink);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            drinkId = intent.getIntExtra("drink", 1);
        }
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager
                .replace(R.id.edit_drink_fragment, EditDrinkFragment.newInstance(drinkId))
                .commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
    }
}

