package com.example.raymond.barbro;

import android.content.Intent;
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

        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager
                .replace(R.id.edit_drink_fragment, EditDrinkFragment.newInstance(drinkId))
                .commit();
    }
}

