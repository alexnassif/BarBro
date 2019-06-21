package com.alexnassif.mobile.barbro.controllers;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexnassif.mobile.barbro.R;
import com.alexnassif.mobile.barbro.data.MyDrink;
import com.bumptech.glide.Glide;

import java.io.File;

public class MyDrinkDetailActivity extends AppCompatActivity {
    private MyDrink drinkId;
    private TextView mMyDrinkName;
    private TextView mMyDrinkIngredients;
    private ImageView mMyDrinkImage;
    private Button mEditButton;
    private String picFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_drink_detail);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            drinkId = (MyDrink) intent.getSerializableExtra("drink");
        }
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mMyDrinkImage = (ImageView) findViewById(R.id.my_drink_pic_view);
        mMyDrinkName = (TextView) findViewById(R.id.my_drink_name_view);
        mMyDrinkIngredients = (TextView) findViewById(R.id.my_ingredients_view);
        mEditButton = (Button) findViewById(R.id.edit_mydrink_button);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyDrinkDetailActivity.this, EditDrinkActivity.class);
                intent.putExtra("drink", drinkId);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMyDrinkName.setText(drinkId.getName());
        mMyDrinkIngredients.setText(drinkId.getIngredients());
        picFile = drinkId.getPic();
        if (picFile != null) {
            Uri takenPhotoUri = Uri.fromFile(new File(picFile));
            Glide.with(this).load(takenPhotoUri.getPath()).into(mMyDrinkImage);
        }
    }

}
