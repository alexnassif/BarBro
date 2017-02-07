package com.example.raymond.barbro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddDrinkActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mNewDrink;
    private EditText mNewIngredients;
    private ImageView mAddImage;
    private Button mSubmit;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_drink);
        mNewDrink = (EditText) findViewById(R.id.edit_drink);
        mNewIngredients = (EditText) findViewById(R.id.new_drink_ingredients);
        mAddImage = (ImageView) findViewById(R.id.take_drink_pic);
        mSubmit = (Button) findViewById(R.id.submit_button);
        mAddImage.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.take_drink_pic) {
            dispatchTakePictureIntent();
        }
        else if(view.getId() == R.id.submit_button){
            if(mNewDrink.getText().toString().trim().equals("")) {
                mNewDrink.setError("Cannot be blank");
            }
            if(mNewIngredients.getText().toString().trim().equals(""))
                mNewIngredients.setError("Cannot be blank");
            else{
                Toast.makeText(this, "works", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mAddImage.setImageBitmap(imageBitmap);
        }
    }
}
