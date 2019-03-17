package com.alexnassif.mobile.barbro;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alexnassif.mobile.barbro.data.AppDatabase;
import com.alexnassif.mobile.barbro.data.AppExecutors;
import com.alexnassif.mobile.barbro.data.BarBroContract;
import com.alexnassif.mobile.barbro.data.MyDrink;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditDrinkActivity extends AppCompatActivity implements View.OnClickListener {
    private MyDrink drinkId;
    private EditText mNewDrink;
    private EditText mNewIngredients;
    private ImageView mAddImage;
    private Button mSubmit;
    private Button mCancel;
    private String mCurrentPhotoPath;
    private String mNewPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private AppDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_drink);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            drinkId = (MyDrink) intent.getSerializableExtra("drink");
        }
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mNewDrink = (EditText) findViewById(R.id.edit_drink);
        mNewIngredients = (EditText) findViewById(R.id.new_drink_ingredients);
        mAddImage = (ImageView) findViewById(R.id.take_drink_pic);
        mSubmit = (Button) findViewById(R.id.submit_button);
        mCancel = (Button) findViewById(R.id.cancel_button);

        mNewDrink.setText(drinkId.getName());
        mNewIngredients.setText(drinkId.getIngredients());
        mCurrentPhotoPath = drinkId.getPic();
        if (mCurrentPhotoPath != null) {
            Uri takenPhotoUri = Uri.fromFile(new File(mCurrentPhotoPath));
            Glide.with(this).load(takenPhotoUri.getPath()).centerCrop().into(mAddImage);
        }

        mAddImage.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mDb = AppDatabase.getsInstance(getApplicationContext());
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mNewPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.take_drink_pic) {
            dispatchTakePictureIntent();
            //setPic();
        } else if (view.getId() == R.id.cancel_button) {
            finish();
        }

        else if (view.getId() == R.id.submit_button) {
            if (mNewDrink.getText().toString().trim().equals("")) {
                mNewDrink.setError("Cannot be blank");
            }
            if (mNewIngredients.getText().toString().trim().equals(""))
                mNewIngredients.setError("Cannot be blank");
            else {

                String pic = null;
                if(mCurrentPhotoPath != null){
                    pic = mCurrentPhotoPath;
                }
                final MyDrink mydrink = new MyDrink(pic, mNewDrink.getText().toString().trim(), mNewIngredients.getText().toString().trim(), drinkId.getId());

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.myDrinksDao().updateMyDrink(mydrink);
                        finish();
                    }
                });
                /*AsyncQueryHandler putDrink = new AsyncQueryHandler(getContentResolver()) {
                };
                String stringId = Integer.toString(drinkId);
                Uri uri = BarBroContract.MyDrinkEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                ContentValues newValue = new ContentValues();
                newValue.put(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_NAME, mNewDrink.getText().toString().trim());
                newValue.put(BarBroContract.MyDrinkEntry.COLUMN_MYINGREDIENTS, mNewIngredients.getText().toString().trim());
                if (mNewPhotoPath != null) {
                    if(mCurrentPhotoPath != null) {
                        File file = new File(mCurrentPhotoPath);
                        if (file.exists())
                            file.delete();
                    }
                    newValue.put(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_PIC, mNewPhotoPath);
                }
                putDrink.startUpdate(-1, null, uri, newValue, null, null);
                finish();*/

            }

        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.alexnassif.mobile.barbro.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Uri takenPhotoUri = Uri.fromFile(new File(mNewPhotoPath));
            // by this point we have the camera photo on disk
            setPic(takenPhotoUri);
            //Glide.with(getContext()).load(takenPhotoUri.getPath()).centerCrop().into(mAddImage);
            // RESIZE BITMAP, see section below
            // Load the taken image into a preview
            //Toast.makeText(this, takenPhotoUri.toString(), Toast.LENGTH_LONG).show();
        } else { // Result was a failure
            //Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            File file = new File(mNewPhotoPath);
            file.delete();
            mNewPhotoPath = null;
        }

    }
    private void setPic(Uri uri) {
        // Get the dimensions of the View
        int targetW = mAddImage.getWidth();
        int targetH = mAddImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath(), bmOptions);
        mAddImage.setImageBitmap(bitmap);
    }
}

