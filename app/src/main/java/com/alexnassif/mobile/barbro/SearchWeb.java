package com.alexnassif.mobile.barbro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.ByteArrayOutputStream;


public class SearchWeb extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "OcrCaptureActivity";
    private Button mButtonPreview;
    private WebView mWebView;
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_web);
        i = getIntent(); //gets the intent that called this intent

        mButtonPreview = (Button) findViewById(R.id.click_preview);
        mButtonPreview.setOnClickListener(this);
        mWebView = (WebView) findViewById(R.id.webView);

        mWebView.setWebViewClient(new MyBrowser());
        mWebView.loadUrl("http://www.google.com");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.click_preview){
            mWebView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(mWebView.getDrawingCache());
            mWebView.setDrawingCacheEnabled(false);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            i.putExtra("bitmap", byteArray);
            setResult(Activity.RESULT_OK, i);
            finish();


        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
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
