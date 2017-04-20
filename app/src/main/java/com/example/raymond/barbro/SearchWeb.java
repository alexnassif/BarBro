package com.example.raymond.barbro;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.raymond.barbro.utilities.GraphicOverlay;

import com.example.raymond.barbro.utilities.SearchDrinkView;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class SearchWeb extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "OcrCaptureActivity";
    private Button mButtonPreview;
    private WebView mWebView;
    private SparseArray<TextBlock> results;
    LinearLayout mDrawingPad;
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    TextRecognizer textRecognizer;
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_web);
       // mGraphicOverlay = (GraphicOverlay<OcrGraphic>) findViewById(R.id.graphicOverlay);
        mButtonPreview = (Button) findViewById(R.id.click_preview);
        mButtonPreview.setOnClickListener(this);
        mWebView = (WebView) findViewById(R.id.webView);
        mDrawingPad=(LinearLayout)findViewById(R.id.view_drawing_pad);

        mWebView.setWebViewClient(new MyBrowser());
        mWebView.loadUrl("http://www.google.com");
        gestureDetector = new GestureDetector(this, new CaptureGestureListener());
        Context context = getApplicationContext();

        // A text recognizer is created to find text.  An associated processor instance
        // is set to receive the text recognition results and display graphics for each text block
        // on screen.
        textRecognizer = new TextRecognizer.Builder(context).build();
        if (!textRecognizer.isOperational()) {
            // Note: The first time that an app using a Vision API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any text,
            // barcodes, or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.click_preview){
            mWebView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(mWebView.getDrawingCache());
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            results = textRecognizer.detect(frame);
            mGraphicOverlay = new GraphicOverlay<>(getApplicationContext(), null, bitmap);
            mWebView.setDrawingCacheEnabled(false);
            mWebView.setVisibility(View.GONE);
            mButtonPreview.setVisibility(View.GONE);
            OcrDetector ocrDetector = new OcrDetector(mGraphicOverlay, results);
            if(mDrawingPad.getChildCount() != 0){
            mDrawingPad.removeAllViewsInLayout();}
            mDrawingPad.addView(mGraphicOverlay);


        }
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {;

        boolean c = gestureDetector.onTouchEvent(e);

        return c || super.onTouchEvent(e);
    }
    private boolean onTap(float rawX, float rawY) {
        OcrGraphic graphic = mGraphicOverlay.getGraphicAtLocation(rawX, rawY);
        TextBlock text = null;
        if (graphic != null) {
            text = graphic.getTextBlock();
            if (text != null && text.getValue() != null) {

                Log.d("block text", text.getValue());

            }
            else {
                Log.d(TAG, "text data is null");
            }
        }
        else {
            Log.d(TAG,"no text detected");
        }
        return text != null;
    }

    private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }
}
