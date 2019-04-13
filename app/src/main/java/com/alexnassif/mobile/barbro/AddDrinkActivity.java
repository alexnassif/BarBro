package com.alexnassif.mobile.barbro;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.alexnassif.mobile.barbro.data.AppDatabase;
import com.alexnassif.mobile.barbro.data.AppExecutors;
import com.alexnassif.mobile.barbro.data.MyDrink;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alexnassif.mobile.barbro.utilities.GraphicOverlay;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDrinkActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mNewDrink;
    private EditText mNewIngredients;
    private ImageView mAddImage;
    private Button mSubmit;
    private Button mCancel;
    private Button mSearchWeb;
    private String mCurrentPhotoPath;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_BITMAP = 2;
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    private LinearLayout mDrawingPad;
    private TextRecognizer textRecognizer;
    private GestureDetector gestureDetector;
    private static final String TAG = "OcrCaptureActivity";

    private MyDragEventListener mDragListen;
    private ScrollView scrollView;
    private SharedPreferences sp;
    private boolean showTip;
    private static final String show_Tip = "showTip";
    private AppDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_drink);
        //views
        sp = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if(sp.contains(show_Tip)){
            showTip = sp.getBoolean(show_Tip, true);
        }
        else{
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(show_Tip, true);
            editor.apply();
            showTip = true;
        }
        mNewDrink = (EditText) findViewById(R.id.edit_drink);
        mNewIngredients = (EditText) findViewById(R.id.new_drink_ingredients);
        mAddImage = (ImageView) findViewById(R.id.take_drink_pic);
        mSubmit = (Button) findViewById(R.id.submit_button);
        mCancel = (Button) findViewById(R.id.cancel_button);

        mSearchWeb = (Button) findViewById(R.id.searchWeb);
        mDrawingPad = (LinearLayout) findViewById(R.id.view_drawing_pad);
        //listeners
        mCancel.setOnClickListener(this);
        mAddImage.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mSearchWeb.setOnClickListener(this);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gestureDetector = new GestureDetector(this, new AddDrinkActivity.CaptureGestureListener());
        mDragListen = new MyDragEventListener();
        mNewDrink.setOnDragListener(mDragListen);
        mNewIngredients.setOnDragListener(mDragListen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDb = AppDatabase.getsInstance(getApplicationContext());

        textRecognizer = new TextRecognizer.Builder(this).build();
        if (!textRecognizer.isOperational()) {

            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mNewIngredients.getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.take_drink_pic) {

            dispatchTakePictureIntent();
        }
        else if (view.getId() == R.id.cancel_button) {
            finish();
        }
        else if(view.getId() == R.id.submit_button){
            if(mNewDrink.getText().toString().trim().equals("")) {
                mNewDrink.setError("Cannot be blank");
            }
            if(mNewIngredients.getText().toString().trim().equals(""))
                mNewIngredients.setError("Cannot be blank");
            else{
                /*AsyncQueryHandler putDrink = new AsyncQueryHandler(this.getContentResolver()) {
                };
                ContentValues newValue = new ContentValues();
                newValue.put(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_NAME, mNewDrink.getText().toString().trim());
                newValue.put(BarBroContract.MyDrinkEntry.COLUMN_MYINGREDIENTS, mNewIngredients.getText().toString().trim());
                if(mCurrentPhotoPath != null){
                    newValue.put(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_PIC, mCurrentPhotoPath);
                }
                putDrink.startInsert(-1, null, BarBroContract.MyDrinkEntry.CONTENT_URI, newValue);*/
                String pic = null;
                if(mCurrentPhotoPath != null){
                    pic = mCurrentPhotoPath;
                }
                final MyDrink mydrink = new MyDrink(pic, mNewDrink.getText().toString().trim(), mNewIngredients.getText().toString().trim());

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.myDrinksDao().insertMyDrink(mydrink);
                        finish();
                    }
                });



            }

        }
        else if(view.getId() == R.id.searchWeb){

            if(textRecognizer.isOperational()) {

                Intent intent = new Intent(this, SearchWeb.class);
                startActivityForResult(intent, REQUEST_BITMAP);
            }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {

            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = Uri.fromFile(new File(mCurrentPhotoPath));
                // by this point we have the camera photo on disk
                setPic(takenPhotoUri);
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                //Toast.makeText(this, takenPhotoUri.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                File file = new File(mCurrentPhotoPath);
                file.delete();
                mCurrentPhotoPath = null;
            }
        }
        if (requestCode == REQUEST_BITMAP) {

            if (resultCode == RESULT_OK) {
                byte[] bitmapArray = data.getByteArrayExtra("bitmap");

                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);

                if (mGraphicOverlay == null)
                    mGraphicOverlay = new GraphicOverlay<>(getApplicationContext(), null, bitmap);
                else {
                    mGraphicOverlay.clear();
                    mGraphicOverlay.setBitmap(bitmap);
                }

                Frame frame = new Frame.Builder().setBitmap(bitmap).build();

                try {

                   SparseArray<TextBlock> results = textRecognizer.detect(frame);
                   OcrDetector ocrDetector = new OcrDetector(mGraphicOverlay, results);
                    if (mDrawingPad.getChildCount() > 0)
                        mDrawingPad.removeAllViews();
                    mDrawingPad.addView(mGraphicOverlay);
                    mDrawingPad.setVisibility(View.VISIBLE);
                    mGraphicOverlay.setOnDragListener(mDragListen);
                    if (showTip) {
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.scroll_view), "Drag the text in" +
                                        " blue boxes into" +
                                        " drink name or ingredients fields", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Don't Show Again", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putBoolean(show_Tip, false);
                                        editor.apply();
                                    }
                                });

                        snackbar.show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Text Recognizer did not work", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){

        // Let ScrollView handle it
        super.dispatchTouchEvent(ev);

        // Otherwise, I'll handle it
        return gestureDetector.onTouchEvent(ev);
    }


    private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            OcrGraphic graphic = mGraphicOverlay.getGraphicAtLocation(e.getRawX(), e.getRawY());
            TextBlock text = null;
            if (graphic != null) {
                text = graphic.getTextBlock();
                if (text != null && text.getValue() != null) {

                    String textToDrag = text.getValue();
                    ClipData.Item item = new ClipData.Item(textToDrag);

                    // Create a new ClipData using the tag as a label, the plain text MIME type, and
                    // the already-created item. This will create a new ClipDescription object within the
                    // ClipData, and set its MIME type entry to "text/plain"
                    ClipData dragData = new ClipData(textToDrag, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

                    // Instantiates the drag shadow builder.
                    View.DragShadowBuilder myShadow = new MyDragShadowBuilder(mGraphicOverlay, textToDrag);

                    // Starts the drag

                    mGraphicOverlay.startDrag(dragData,  // the data to be dragged
                            myShadow,  // the drag shadow builder
                            null,      // no need to use local data
                            0          // flags (not currently used, set to 0)
                    );

                    scrollView.smoothScrollTo(0, 0);

                } else {
                    Toast.makeText(AddDrinkActivity.this, "Text data is null", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddDrinkActivity.this, "No text detected", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private static class MyDragShadowBuilder extends View.DragShadowBuilder {

        // The drag shadow image, defined as a drawable thing
        private static TextDrawable shadow;

        // Defines the constructor for myDragShadowBuilder
        public MyDragShadowBuilder(View v, String text) {

            // Stores the View parameter passed to myDragShadowBuilder.
            super(v);

            // Creates a draggable image that will fill the Canvas provided by the system.
            shadow = new TextDrawable(text);
        }

        // Defines a callback that sends the drag shadow dimensions and touch point back to the
        // system.
        @Override
        public void onProvideShadowMetrics (Point size, Point touch) {
            // Defines local variables
            int width, height;

            // Sets the width of the shadow to half the width of the original View
            width = getView().getWidth();

            // Sets the height of the shadow to half the height of the original View
            height = getView().getHeight();

            // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
            // Canvas that the system will provide. As a result, the drag shadow will fill the
            // Canvas.
            shadow.setBounds(0, 0, width, height);

            // Sets the size parameter's width and height values. These get back to the system
            // through the size parameter.
            size.set(width, height);

            // Sets the touch point's position to be in the middle of the drag shadow
            touch.set(200, 50);
        }

        // Defines a callback that draws the drag shadow in a Canvas that the system constructs
        // from the dimensions passed in onProvideShadowMetrics().
        @Override
        public void onDrawShadow(Canvas canvas) {

            // Draws the ColorDrawable in the Canvas passed in from the system.

            shadow.draw(canvas);
        }
    }

    protected class MyDragEventListener implements View.OnDragListener {

        // This is the method that the system calls when it dispatches a drag event to the
        // listener.
        public boolean onDrag(View v, DragEvent event) {

            // Defines a variable to store the action type for the incoming event
            final int action = event.getAction();
            final float x = event.getX();
            final float y = event.getY();

            // Handles each of the expected events
            switch(action) {

                case DragEvent.ACTION_DRAG_STARTED:

                    // Determines if this View can accept the dragged data
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        // As an example of what your application might do,
                        // applies a blue color tint to the View to indicate that it can accept
                        // data.


                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        // returns true to indicate that the View can accept the dragged data.
                        return true;

                    }

                    // Returns false. During the current drag and drop operation, this View will
                    // not receive events again until ACTION_DRAG_ENDED is sent.
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:

                    // Applies a green tint to the View. Return true; the return value is ignored.

                    v.setBackgroundColor(Color.GRAY);
                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:

                    return true;

                case DragEvent.ACTION_DRAG_EXITED:

                    // Re-sets the color tint to blue. Returns true; the return value is ignored.

                    v.setBackgroundColor(Color.WHITE);
                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DROP:

                    // Gets the item containing the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    // Gets the text data from the item.

                    // Displays a message containing the dragged data.
                    if(v instanceof EditText){
                        ((EditText) v).append(item.getText());
                    }

                    // Turns off any color tints
                    v.setBackgroundColor(Color.WHITE);
                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Returns true. DragEvent.getResult() will return true.
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:

                    // Turns off any color tinting

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Does a getResult(), and displays what happened.


                    // returns true; the value is ignored.
                    return true;

                // An unknown action type was received.
                default:
                    break;
            }

            return false;
        }
    }


}
