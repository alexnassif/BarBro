package com.example.raymond.barbro;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.raymond.barbro.data.BarBroContract;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDrinkActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mNewDrink;
    private EditText mNewIngredients;
    private ImageView mAddImage;
    private Button mSubmit;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    public final String APP_TAG = "MyCustomApp";
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
            //setPic();
        }
        else if(view.getId() == R.id.submit_button){
            if(mNewDrink.getText().toString().trim().equals("")) {
                mNewDrink.setError("Cannot be blank");
            }
            if(mNewIngredients.getText().toString().trim().equals(""))
                mNewIngredients.setError("Cannot be blank");
            else{
                AsyncQueryHandler putDrink = new AsyncQueryHandler(this.getContentResolver()) {
                };
                ContentValues newValue = new ContentValues();
                newValue.put(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_NAME, mNewDrink.getText().toString().trim());
                newValue.put(BarBroContract.MyDrinkEntry.COLUMN_MYINGREDIENTS, mNewIngredients.getText().toString().trim());
                putDrink.startInsert(-1, null, BarBroContract.MyDrinkEntry.CONTENT_URI, newValue);
            }
            mNewDrink.setText("");
            mNewIngredients.setText("");
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
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Toast.makeText(this, mCurrentPhotoPath, Toast.LENGTH_LONG).show();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.raymond.barbro.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Uri takenPhotoUri = getPhotoFileUri(mCurrentPhotoPath);
            // by this point we have the camera photo on disk
            setPic(takenPhotoUri);
            // RESIZE BITMAP, see section below
            // Load the taken image into a preview
            Toast.makeText(this, takenPhotoUri.toString(), Toast.LENGTH_LONG).show();
        } else { // Result was a failure
            Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }

    }

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(fileName));
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
}
