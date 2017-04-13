package com.example.raymond.barbro;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class SearchWeb extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "OcrCaptureActivity";
    private Button mButtonPreview;
    private ImageView mImagePreview;
    private WebView mWebView;
    private TextRecognizer textRecognizer;
    private SparseArray<TextBlock> results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_web);

        mButtonPreview = (Button) findViewById(R.id.click_preview);
        mButtonPreview.setOnClickListener(this);
        mImagePreview = (ImageView) findViewById(R.id.drink_preview);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new MyBrowser());
        mWebView.loadUrl("http://www.google.com");

        textRecognizer = new TextRecognizer.Builder(this).build();
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
            mWebView.setDrawingCacheEnabled(false);
            mImagePreview.setImageBitmap(bitmap);
            mWebView.setVisibility(View.GONE);
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            results = textRecognizer.detect(frame);

            for (int i = 0; i < results.size(); ++i) {
                TextBlock item = results.valueAt(i);
                Log.d("textblock result", item.getValue());
            }
        }
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
