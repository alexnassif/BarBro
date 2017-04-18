package com.example.raymond.barbro.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.raymond.barbro.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

/**
 * Created by alex on 4/14/17.
 */

public class SearchDrinkView extends View {
    private static final String TAG = "OcrCaptureActivity";
    Paint       mPaint;
    Context context;
    //MaskFilter  mEmboss;
    //MaskFilter  mBlur;
    Bitmap mBitmap;
    Canvas  mCanvas;
    Paint mBitmapPaint;
    Bitmap myBitmap;
    private int height;
    private int width;
    private float initialTop;
    private float initialBottom;
    private float initialRight;
    private float initialLeft;
    private RectF rect;
    private TextRecognizer textRecognizer;
    private SparseArray<TextBlock> results;

    public SearchDrinkView(Context context, Bitmap b) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaint.setAntiAlias(true);
        context = context;
        mBitmapPaint = new Paint();
        mBitmapPaint.setARGB(128, 255, 255, 255);
        myBitmap = Bitmap.createBitmap(b);
        width = myBitmap.getWidth();
        height = myBitmap.getHeight();

        initialTop = 10;
        initialLeft = 10;
        initialRight = width - 10;
        initialBottom = initialTop + 100;
       // Log.d("iB and iright", initialBottom + " " + initialRight);
        rect = new RectF(initialLeft, initialTop, initialRight, initialBottom);
        textRecognizer = new TextRecognizer.Builder(getContext()).build();
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
            boolean hasLowStorage = context.registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(context, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, context.getString(R.string.low_storage_error));
            }
        }
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mBitmap = myBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Log.d("iB2 and iright2", mBitmap.getHeight() + " " + mBitmap.getWidth());
        //mCanvas = new Canvas(mBitmap);

    }
    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.draw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawRect(rect, mBitmapPaint);
        //canvas.drawPath(mPath, mPaint);
        invalidate();
    }

    private float mX, mY;

    private void touch_start(float x, float y) {

        mX = x;
        mY = y;
        //Log.d("mx and my", mX + " " + mY);
    }

    private void touch_move(float x, float y) {
        float dx = x - mX;
        float dy = y - mY;

        mX = x;
        mY = y;
        initialTop += dy;
        initialLeft += dx;
        initialBottom += dy;
        initialRight += dx;
        Log.d("initRight&iniBottom", initialRight + " " + initialBottom);
        Log.d("dx1 and dy1", dx + " " + dy);
        if(initialTop < 0f && dy < 0) {
            dy = Math.abs(initialTop) + dy;
            initialTop = 0;
        }
        if(initialLeft < 0f && dx < 0) {
            dx = Math.abs(initialLeft) + dx;
           initialLeft = 0;
        }
        /*if(initialRight > mBitmap.getWidth()) {

            dx = mBitmap.getWidth() - dx;
            initialRight = mBitmap.getWidth();
        }
        if(initialBottom > mBitmap.getHeight()) {
            dy = mBitmap.getHeight() - dy;
            initialBottom = mBitmap.getHeight();
        }*/
        rect.offset(dx, dy);
    }
    private void touch_up(float x, float y) {
        Bitmap bitmap = Bitmap.createBitmap(mBitmap, (int) initialLeft, (int)initialTop, width - (int)initialLeft - 10, (int)initialTop + 10);
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        results = textRecognizer.detect(frame);
        for (int i = 0; i < results.size(); ++i) {
            TextBlock item = results.valueAt(i);
            Log.d("textblock result", item.getValue());
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up(x, y);
                invalidate();
                break;
        }
        return true;
    }
}
