package com.example.raymond.barbro;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.raymond.barbro.data.BarBroContract;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class EditDrinkFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private EditText mNewDrink;
    private EditText mNewIngredients;
    private ImageView mAddImage;
    private Button mSubmit;
    private Button mCancel;
    private String mCurrentPhotoPath;
    private String mNewPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int MY_DRINKS_LOADER = 1;
    // TODO: Rename and change types of parameters
    private int drinkId;

    public EditDrinkFragment() {
        // Required empty public constructor
    }


    public static EditDrinkFragment newInstance(int drink) {
        EditDrinkFragment fragment = new EditDrinkFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, drink);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drinkId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_drink, container, false);
        mNewDrink = (EditText) view.findViewById(R.id.edit_drink);
        mNewIngredients = (EditText) view.findViewById(R.id.new_drink_ingredients);
        mAddImage = (ImageView) view.findViewById(R.id.take_drink_pic);
        mSubmit = (Button) view.findViewById(R.id.submit_button);
        mCancel = (Button) view.findViewById(R.id.cancel_button);

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAddImage.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        getLoaderManager().initLoader(MY_DRINKS_LOADER, null, this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.take_drink_pic) {
            dispatchTakePictureIntent();
            //setPic();
        } else if (view.getId() == R.id.cancel_button) {
            getActivity().finish();
        }

        else if (view.getId() == R.id.submit_button) {
            if (mNewDrink.getText().toString().trim().equals("")) {
                mNewDrink.setError("Cannot be blank");
            }
            if (mNewIngredients.getText().toString().trim().equals(""))
                mNewIngredients.setError("Cannot be blank");
            else {
                AsyncQueryHandler putDrink = new AsyncQueryHandler(getActivity().getContentResolver()) {
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
                getActivity().finish();

            }

        }

    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mNewPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.raymond.barbro.fileprovider",
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case MY_DRINKS_LOADER: {
                Uri uriAllDrinks = BarBroContract.MyDrinkEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        uriAllDrinks,
                        null,
                        BarBroContract.BarBroEntry._ID + "=?",
                        new String[]{String.valueOf(drinkId)},
                        null);
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();

        int drinkName = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_NAME);
        int ingredients = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYINGREDIENTS);
        int videoId = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_PIC);

        mNewDrink.setText(data.getString(drinkName));
        mNewIngredients.setText(data.getString(ingredients));
        mCurrentPhotoPath = data.getString(videoId);
        if (mCurrentPhotoPath != null) {
            Uri takenPhotoUri = Uri.fromFile(new File(mCurrentPhotoPath));
            Glide.with(getContext()).load(takenPhotoUri.getPath()).centerCrop().into(mAddImage);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
